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
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Key;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Pellet;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.PowerPellet;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.menus.MenuItems;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {
    // Sounds
    protected static final SoundAcoustics SIREN_SOUND = SuperPacmanSound.SIREN.sound;
    protected static final SoundAcoustics POWER_PELLET_SOUND = SuperPacmanSound.POWER_PELLET.sound;
    private static final SoundAcoustics DEATH_SOUND = SuperPacmanSound.PACMAN_DEATH.sound;
    private static final SoundAcoustics MUNCH_SOUND = SuperPacmanSound.MUNCH.sound;
    private static final SoundAcoustics EAT_FRUIT_SOUND = SuperPacmanSound.EAT_FRUIT.sound;
    private static final SoundAcoustics EAT_GHOST_SOUND = SuperPacmanSound.EAT_FRUIT.sound;
    private static final SoundAcoustics COLLECT_KEY_SOUND = SuperPacmanSound.COLLECT_KEY.sound;
    // Animation duration in frame number
    private static final int ANIMATION_DURATION = 10; // base 10
    private static final int DEBUG_ANIMATION_DURATION = 4;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private static final int SPRITE_SIZE = 14;
    private static final int MAX_HP = 5;
    private static boolean stopAllAudio = false;
    private static int comboCount = 0;
    private final Animation[] animation;
    private final Animation deathAnimation;
    private final SuperPacmanPlayerHandler playerHandler = new SuperPacmanPlayerHandler();
    private final Glow glow;
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION;
    private int currentHp = 5;
    private final SuperPacmanPlayerStatusGUI gui = new SuperPacmanPlayerStatusGUI(currentHp, MAX_HP);
    private int score = 0;
    private Orientation desiredOrientation = null;
    private Orientation currentOrientation = DEFAULT_ORIENTATION;
    private boolean canUserMove = false;
    private boolean isDead = false;
    private float timer = 3;
    private boolean collision = false;

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
        for (Sprite[] spriteFrames : sprites) {
            for (Sprite sprite : spriteFrames) {
                sprite.setDepth(600);
            }
        }
        animation = Animation.createAnimations(ANIMATION_DURATION / 2 + 2, sprites);
        // Death animation
        Sprite[] deadSprites =
                RPGSprite.extractSprites("superpacman/deadPacman", 12, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE);
        for (Sprite sprite : deadSprites) {
            sprite.setDepth(50);
        }

        deathAnimation = new Animation(ANIMATION_DURATION - 1, deadSprites, false);

        // GLOW
        glow = new Glow(this, sprites[0][0], Glow.GlowColors.YELLOW, 4.0f, 0.8f);

        // SOUNDS
        SIREN_SOUND.shouldBeStarted();

        resetMotion();
    }

    public static boolean isStopAllAudio() {
        return stopAllAudio;
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public static int getComboCount() {
        return comboCount;
    }

    public static void resetComboCount() {
        SuperPacmanPlayer.comboCount = 0;
    }

    public boolean canUserMove() {
        return canUserMove;
    }

    public void setCanUserMove(boolean canUserMove) {
        this.canUserMove = canUserMove;
    }

    protected void updateScore(int score) {
        this.score += score;
    }

    @Override
    public void bip(Audio audio) {
        if (stopAllAudio) {
            SoundAcoustics.stopAllSounds(audio);
            stopAllAudio = false;
        }
        if (canUserMove) {
            SIREN_SOUND.bip(audio);
            MUNCH_SOUND.bip(audio);
            EAT_FRUIT_SOUND.bip(audio);
            DEATH_SOUND.bip(audio);
            EAT_GHOST_SOUND.bip(audio);
            POWER_PELLET_SOUND.bip(audio);
            COLLECT_KEY_SOUND.bip(audio);
        }
    }

    @Override
    public void update(float deltaTime) {
        gui.update(currentHp, score);
        updateAnimation(deltaTime);
        ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().update(deltaTime);

        if (isDead) {
            glow.fadeOut(0.02f);
            resetMotion();
            canUserMove = false;
            timer -= deltaTime;
            if (timer <= 0) {
                reset();
                setDead(false);
                timer = 3;
            }
        }
        if (canUserMove) {
            Keyboard keyboard = getOwnerArea().getKeyboard();
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
                collision = false;
            }
            if (!isDisplacementOccurs()) {
                if (!MenuItems.isDebugMode()) {
                    move(ANIMATION_DURATION);
                } else {
                    move(DEBUG_ANIMATION_DURATION);
                }
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
        canUserMove = true;
        desiredOrientation = null;
        currentOrientation = DEFAULT_ORIENTATION;
        glow.reset();
        DiscreteCoordinates intiPos = ((SuperPacmanArea) getOwnerArea()).getPlayerSpawnPosition();
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(intiPos));
        setCurrentPosition(intiPos.toVector());

    }

    private void setDead(boolean isDead) {
        this.isDead = isDead;
        if (isDead) {
            setStopAllAudio();
            DEATH_SOUND.shouldBeStarted();
        } else {
            if (!stopAllAudio) {
                SIREN_SOUND.shouldBeStarted();
            }
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

    protected static void setStopAllAudio() {
        SuperPacmanPlayer.stopAllAudio = true;
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

    private void setPlayerPosition(DiscreteCoordinates destination) {
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(destination));
        setCurrentPosition(destination.toVector());
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
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(currentOrientation.toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return !isDead;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
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
            if (getOwnerArea().getTitle().equals(door.getDestination())) {
                setPlayerPosition(door.getOtherSideCoordinates());
            } else {
                if (!stopAllAudio) {
                    SIREN_SOUND.shouldBeStarted();
                }
                setStopAllAudio();
                setIsPassingADoor(door);
            }
        }

        @Override
        public void interactWith(Ghost ghost) {
            if (ghost.isFrightened()) {
                getOwnerArea().getCamera().shake(2.5f, 8);
                EAT_GHOST_SOUND.shouldBeStarted();
                ghost.setEaten();
                if (comboCount < 4) {
                    ++comboCount;
                } else {
                    comboCount = 4;
                }

                score += Ghost.GHOST_BASE_SCORE * comboCount;

            } else {
                setDead(true);
                ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().resetGhosts();
            }
        }

        //added 12/04 Karlis
        @Override
        public void interactWith(Key key) {
            COLLECT_KEY_SOUND.shouldBeStarted();
            key.collect();
            updateScore(key.getPoints());
        }

        @Override
        public void interactWith(Cherry cherry) {
            EAT_FRUIT_SOUND.shouldBeStarted();
            cherry.collect();
            updateScore(cherry.getPoints());
        }

        @Override
        public void interactWith(Pellet pellet) {
            MUNCH_SOUND.shouldBeStarted();
            pellet.collect();
            updateScore(pellet.getPoints());
        }

        @Override
        public void interactWith(PowerPellet powerPellet) {
            setStopAllAudio();
            POWER_PELLET_SOUND.shouldBeStarted();
            powerPellet.collect();
            ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().frightenGhosts();
        }

        @Override
        public void interactWith(Wall wall) {
            if (getOwnerArea().getCamera() != null) {
                if (!collision) {
                    getOwnerArea().getCamera().shake(0.1f, 5);
                }
                collision = true;
            }
        }
    }
}