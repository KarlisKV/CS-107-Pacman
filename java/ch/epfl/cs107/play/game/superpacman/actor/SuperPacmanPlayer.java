/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:41
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level0;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level1;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level2;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {
    // Animation duration in frame number
    private static final int ANIMATION_DURATION = 10;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private static final int SPRITE_SIZE = 14;
    private static final int MAX_HP = 5;
    private final SuperPacmanPlayerHandler playerHandler = new SuperPacmanPlayerHandler();
    private final Animation[] animation;
    private final Animation deathAnimation;
    private final SoundAcoustics sirenSound;
    private final SoundAcoustics powerPelletSound;
    private final SoundAcoustics deathSound;
    private final SoundAcoustics munchSound;
    private final SoundAcoustics eatFruitSound;
    private final SoundAcoustics eatGhostSound;
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION;
    private final Glow glow;
    private Keyboard keyboard;
    private Audio audio;
    private int currentHp = 5;
    private final SuperPacmanPlayerStatusGUI gui = new SuperPacmanPlayerStatusGUI(currentHp, MAX_HP);
    private int score = 0;
    private Orientation desiredOrientation = null;
    private Orientation currentOrientation = DEFAULT_ORIENTATION;
    private boolean canUserMove = false;
    private boolean isDead = false;
    private float timer = 3;
    /**
     * Constructor for SuperPacmanPlayer
     * @param owner       (Area): Owner Area, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, DEFAULT_ORIENTATION, coordinates);
        PLAYER_SPAWN_POSITION = coordinates;

        // ANIMATIONS
        // Normal animation
        Sprite[][] sprites =
                RPGSprite.extractSprites("superpacman/pacmanSmall", 4, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE,
                                         new Orientation[]{Orientation.DOWN, Orientation.LEFT, Orientation.UP,
                                                           Orientation.RIGHT});
        animation = Animation.createAnimations(ANIMATION_DURATION / 2 + 2, sprites);
        // Death animation
        Sprite[] deadSprites =
                RPGSprite.extractSprites("superpacman/deadPacman", 12, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE);
        deathAnimation = new Animation(ANIMATION_DURATION - 1, deadSprites, false);

        // GLOW
        glow = new Glow(this, sprites[0][0], Glow.GlowColors.YELLOW, 5.0f, 0.5f);

        // SOUNDS
        sirenSound = new SoundAcoustics(ResourcePath.getSounds("superpacman/siren_1"), 1.f, false, false, true, false);
        sirenSound.shouldBeStarted();
        powerPelletSound =
                new SoundAcoustics(ResourcePath.getSounds("superpacman/power_pellet"), 1.f, false, false, true, true);

        deathSound = new SoundAcoustics(ResourcePath.getSounds("superpacman/death_1"), 1.f, false, false, false, true);
        munchSound = new SoundAcoustics(ResourcePath.getSounds("superpacman/munch_1"), 1.f, false, false, false, false);
        eatFruitSound =
                new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_fruit"), 1.f, false, false, false, false);
        eatGhostSound =
                new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_ghost"), 1.f, false, false, false, false);
        resetMotion();
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public boolean canUserMove() {
        return canUserMove;
    }

    public void setCanUserMove(boolean canUserMove) {
        this.canUserMove = canUserMove;
    }

    public void updateScore(int score) {
        this.score += score;
    }

    @Override
    public void bip(Audio audio) {
        // TODO: find better way
        this.audio = audio;
        if (canUserMove) {
            sirenSound.bip(audio);
        }
        deathSound.bip(audio);
        munchSound.bip(audio);
        eatFruitSound.bip(audio);
        eatGhostSound.bip(audio);
        powerPelletSound.bip(audio);
    }

    @Override
    public void update(float deltaTime) {
        gui.update(currentHp, score);
        updateAnimation(deltaTime);

        if (isDead) {
            glow.fadeOut(0.02f);
            resetMotion();
            canUserMove = false;
            timer -= deltaTime;
            if (timer <= 0) {
                reset();
                timer = 3;
            }
        }
        if (canUserMove) {
            keyboard = getOwnerArea().getKeyboard();
            setDesiredOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            setDesiredOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
            setDesiredOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            setDesiredOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }

        if (desiredOrientation != null && !isDead) {
            List<DiscreteCoordinates> jumpedCell =
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));

            if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, jumpedCell)) {
                orientate(desiredOrientation);
                currentOrientation = desiredOrientation;
            }
            if (!isDisplacementOccurs()) {
                move(ANIMATION_DURATION);
            }
        }
        super.update(deltaTime);
    }

    /**
     * Method to update the animations
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    private void updateAnimation(float deltaTime) {
        if (isDisplacementOccurs()) {
            animation[currentOrientation.ordinal()].update(deltaTime);
        } else {
            animation[currentOrientation.ordinal()].reset();
        }
        if (isDead) {
            deathAnimation.update(deltaTime);
        } else {
            deathAnimation.reset();
        }
    }

    /**
     * Restart SuperPacmanPlayer, resetting all attributes to initial values, and loosing 1 life
     */
    private void reset() {
        if (currentHp > 0) {
            --currentHp;
        }
        setDead(false);
        canUserMove = true;
        desiredOrientation = null;
        currentOrientation = DEFAULT_ORIENTATION;
        glow.reset();
        // TODO: find better way of achieving this
        DiscreteCoordinates intiPos;
        switch (getOwnerArea().getTitle()) {
            case "superpacman/level0":
                intiPos = Level0.PLAYER_SPAWN_POSITION;
                break;
            case "superpacman/level1":
                intiPos = Level1.PLAYER_SPAWN_POSITION;
                break;
            case "superpacman/level2":
                intiPos = Level2.PLAYER_SPAWN_POSITION;
                break;
            default:
                intiPos = PLAYER_SPAWN_POSITION;
        }
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(intiPos));
        setCurrentPosition(intiPos.toVector());

    }

    private void setDead(boolean isDead) {
        this.isDead = isDead;
        if (isDead) {
            SoundAcoustics.stopAllSounds(audio);
            deathSound.shouldBeStarted();
        } else {
            sirenSound.shouldBeStarted();
        }
    }

    /**
     * Method to set the desiered orientation if the key is pressed down
     * @param orientation the Orientation linked with the key
     * @param key         keyboard key
     */
    private void setDesiredOrientation(Orientation orientation, Button key) {
        if (key.isDown()) {
            desiredOrientation = orientation;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        gui.draw(canvas);
        glow.draw(canvas);
        if (!isDead) {
            animation[currentOrientation.ordinal()].draw(canvas);
        } else {
            deathAnimation.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return !isDead;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return !isDead;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(playerHandler);
    }

    /**
     * Interaction handler class for SuperPacmanPlayer
     */
    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(Ghost ghost) {
            if (ghost.isFrightened()) {
                getOwnerArea().getCamera().shake();
                eatGhostSound.shouldBeStarted();
                score += Ghost.GHOST_SCORE;
                ghost.setEaten();

            } else {
                setDead(true);
                for (Ghost ghostInstance : SuperPacmanArea.getGhosts()) {
                    ghostInstance.reset();
                }
            }
        }

        @Override
        public void interactWith(Cherry cherry) {
            eatFruitSound.shouldBeStarted();
            cherry.collect();
            updateScore(cherry.getPoints());
        }

        @Override
        public void interactWith(Pellet pellet) {
            munchSound.shouldBeStarted();
            pellet.collect();
            updateScore(pellet.getPoints());
        }

        @Override
        public void interactWith(PowerPellet powerPellet) {
            powerPelletSound.shouldBeStarted();
            powerPellet.collect();
            for (Ghost ghostInstance : SuperPacmanArea.getGhosts()) {
                ghostInstance.setFrightened(true);
            }
        }

    }
}