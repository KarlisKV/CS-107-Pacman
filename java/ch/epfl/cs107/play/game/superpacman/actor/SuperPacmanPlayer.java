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
import ch.epfl.cs107.play.game.superpacman.SoundUtility;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Cake;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Key;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Pellet;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.PowerPellet;
import ch.epfl.cs107.play.game.superpacman.actor.ghosts.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDepth;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanSound;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperPacmanPlayer extends Player {
    // Sounds
    public static final SoundAcoustics SIREN_SOUND = SuperPacmanSound.SIREN.sound;
    public static final SoundAcoustics POWER_PELLET_SOUND = SuperPacmanSound.POWER_PELLET.sound;
    public static final SoundAcoustics MUNCH_SOUND = SuperPacmanSound.MUNCH.sound;
    private static final SoundAcoustics DEATH_SOUND = SuperPacmanSound.PACMAN_DEATH.sound;
    private static final SoundAcoustics EAT_FRUIT_SOUND = SuperPacmanSound.EAT_FRUIT.sound;
    private static final SoundAcoustics EAT_GHOST_SOUND = SuperPacmanSound.EAT_GHOST.sound;
    private static final SoundAcoustics COLLECT_KEY_SOUND = SuperPacmanSound.COLLECT_KEY.sound;
    // Default attributes
    private static final int ANIMATION_DURATION = 10;  // base 10
    private static final int DEBUG_SPEED_MODE_ANIMATION_DURATION = 4;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private static final int SPRITE_SIZE = 14;
    private static final int MAX_HP = 5;
    private static SoundUtility playerSoundUtility;
    // Player key attributes
    private static int comboCount = 0;
    // Player state
    private static boolean dead = false;
    private static boolean canUserMove = false;
    private final DiscreteCoordinates playerSpawnPosition;
    // Visuals & Sound
    private final Animation[] animation;
    // Time
    private final Map<String, Float> areaTimerHistory = new HashMap<>();
    private final Animation deathAnimation;
    private final Glow glow;
    // Position & Orientation
    private final SuperPacmanPlayerHandler playerHandler = new SuperPacmanPlayerHandler();
    private int currentHp = 5;
    private final SuperPacmanPlayerStatusGUI gui = new SuperPacmanPlayerStatusGUI(currentHp, MAX_HP);
    private float areaTimer = 0;
    private int score = 0;
    private float timer = 3;
    private Orientation desiredOrientation = null;
    private Orientation currentOrientation = DEFAULT_ORIENTATION;
    private boolean gameOver = false;
    private boolean collision = false;
    /**
     * Constructor for SuperPacmanPlayer
     * @param owner       (Area): Owner Area, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, DEFAULT_ORIENTATION, coordinates);
        playerSpawnPosition = coordinates;

        // ANIMATIONS
        // Normal animation
        Sprite[][] sprites =
                RPGSprite.extractSprites("superpacman/pacmanSmall", 4, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE,
                                         new Orientation[]{Orientation.DOWN, Orientation.LEFT, Orientation.UP,
                                                           Orientation.RIGHT});
        for (Sprite[] spriteFrames : sprites) {
            for (Sprite sprite : spriteFrames) {
                sprite.setDepth(SuperPacmanDepth.PLAYER.value);
            }
        }
        animation = Animation.createAnimations(ANIMATION_DURATION / 2, sprites);
        // Death animation
        Sprite[] deadSprites =
                RPGSprite.extractSprites("superpacman/deadPacman", 12, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE);
        for (Sprite sprite : deadSprites) {
            sprite.setDepth(SuperPacmanDepth.PLAYER_DEATH.value);
        }

        deathAnimation = new Animation(ANIMATION_DURATION - 1, deadSprites, false);

        // GLOW
        glow = new Glow(this, sprites[0][0], Glow.GlowColors.YELLOW, 4.0f, 0.8f);

        // SOUNDS
        playerSoundUtility = new SoundUtility(
                new SoundAcoustics[]{SIREN_SOUND, POWER_PELLET_SOUND, DEATH_SOUND, MUNCH_SOUND, EAT_FRUIT_SOUND,
                                     EAT_GHOST_SOUND, COLLECT_KEY_SOUND}, true);

        playerSoundUtility.play(SIREN_SOUND);
        resetMotion();
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public static SoundUtility getPlayerSoundUtility() {
        return playerSoundUtility;
    }

    public static int getMaxHp() {
        return MAX_HP;
    }

    public static boolean isDead() {
        return dead;
    }

    private void setDead(boolean isDead) {
        SuperPacmanPlayer.dead = isDead;
        if (isDead) {
            playerSoundUtility.play(DEATH_SOUND, true);
        } else if (!gameOver) {
            playerSoundUtility.play(SIREN_SOUND, true);
        } else {
            playerSoundUtility.stopAll();
            playerSoundUtility.setAudioPaused(true);
        }
    }

    public static int getComboCount() {
        return comboCount;
    }

    public static void resetComboCount() {
        SuperPacmanPlayer.comboCount = 0;
    }

    public Map<String, Float> getAreaTimerHistory() {
        return areaTimerHistory;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public static boolean canUserMove() {
        return canUserMove;
    }

    public static void setCanUserMove(boolean canUserMove) {
        SuperPacmanPlayer.canUserMove = canUserMove;
    }

    public void updateScore(int score) {
        this.score += score * (comboCount + 1) * SuperPacmanAreaBehavior.getInitDifficulty().multiplicationFactor;
    }

    @Override
    public void bip(Audio audio) {
        playerSoundUtility.bip(audio);
    }

    @Override
    public void update(float deltaTime) {
        gui.update(currentHp, score, comboCount, areaTimer, areaTimerHistory.values());
        updateAnimation(deltaTime);
        ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().update(deltaTime);

        // Death
        if (dead) {
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

        // Set desired Orientation
        if (canUserMove && !gameOver) {
            areaTimer += deltaTime;
            Keyboard keyboard = getOwnerArea().getKeyboard();
            setDesiredOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            setDesiredOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
            setDesiredOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            setDesiredOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }

        // Set desired Orientation
        if (desiredOrientation != null && !dead && !gameOver) {
            List<DiscreteCoordinates> jumpedCell =
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));

            // Orientate the player
            if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, jumpedCell)) {
                orientate(desiredOrientation);
                currentOrientation = desiredOrientation;
                collision = false;
            }

            // Move the player
            if (!isDisplacementOccurs()) {
                if (!MenuStateManager.isSpeedMode()) {
                    move(ANIMATION_DURATION);
                } else {
                    move(DEBUG_SPEED_MODE_ANIMATION_DURATION);
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
        if (dead) {
            deathAnimation.update(deltaTime);
        } else {
            deathAnimation.reset();
        }
    }

    /**
     * Restart SuperPacmanPlayer, resetting all attributes to initial values, and loosing 1 life
     */
    public void reset() {
        if (currentHp > 0) {
            --currentHp;
        }
        desiredOrientation = null;
        currentOrientation = DEFAULT_ORIENTATION;
        glow.reset();
        DiscreteCoordinates intiPos = ((SuperPacmanArea) getOwnerArea()).getPlayerSpawnPosition();
        if (getEnteredCells() != null) {
            getOwnerArea().leaveAreaCells(this, getEnteredCells());
        }
        getOwnerArea().enterAreaCells(this, Collections.singletonList(intiPos));
        setCurrentPosition(intiPos.toVector());

        boolean gameWon = Pellet.areaAllPelletsCleared() && ((SuperPacmanArea) getOwnerArea()).isEndingLevel();
        if (currentHp == 0 || MenuStateManager.isEndGame() || gameWon) {
            areaTimerHistory.put(getOwnerArea().getTitle(), areaTimer);
            gameOver = true;
            canUserMove = false;

        } else {
            canUserMove = true;
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

    /**
     * Method to restart player, resetting attributes
     */
    public void restart() {
        currentHp = MAX_HP;
        score = 0;
        areaTimerHistory.clear();
        areaTimer = 0;
        gameOver = false;
    }

    @Override
    public void draw(Canvas canvas) {
        gui.draw(canvas);
        if (!gameOver) {
            glow.draw(canvas);
            if (!dead) {
                animation[currentOrientation.ordinal()].draw(canvas);
            } else {
                deathAnimation.draw(canvas);
            }
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
        return !dead;
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
        return !dead;
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
            if (door.isDestinationSameArea()) {
                setPlayerPosition(door.getOtherSideCoordinates());
            } else {
                playerSoundUtility.play(SIREN_SOUND, true);
                comboCount = 0;
                areaTimerHistory.put(getOwnerArea().getTitle(), areaTimer);
                areaTimer = 0;
                setIsPassingADoor(door);
            }
        }

        @Override
        public void interactWith(Ghost ghost) {
            if (ghost.isFrightened()) {
                getOwnerArea().getCamera().shake(2.5f, 8);
                ghost.setEaten();
                playerSoundUtility.play(EAT_GHOST_SOUND);
                if (comboCount < 4) {
                    ++comboCount;
                } else {
                    comboCount = 4;
                }

                score += Ghost.GHOST_BASE_SCORE * comboCount;

            } else {
                if (!MenuStateManager.isGodMode()) {
                    if (currentHp - 1 == 0) {
                        ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().pauseGhosts();
                        canUserMove = false;
                    }
                    setDead(true);
                    ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().resetGhosts();
                }
            }
        }


        @Override
        public void interactWith(Key key) {
            playerSoundUtility.play(COLLECT_KEY_SOUND);
            key.collect();
            key.setSignalOn();
            updateScore(key.getPoints());
        }

        @Override
        public void interactWith(Cake cake) {
            playerSoundUtility.play(EAT_FRUIT_SOUND);
            cake.collect();
            updateScore(cake.getPoints());
        }

        @Override
        public void interactWith(Pellet pellet) {
            playerSoundUtility.play(MUNCH_SOUND);
            pellet.collect();
            updateScore(pellet.getPoints());
        }

        @Override
        public void interactWith(PowerPellet powerPellet) {
            playerSoundUtility.play(POWER_PELLET_SOUND, true);
            powerPellet.collect();
            ((SuperPacmanArea) getOwnerArea()).getGhostsManagement().frightenGhosts();
        }

        @Override
        public void interactWith(Wall wall) {
            if (getOwnerArea().getCamera() != null) {
                if (!collision) {
                    getOwnerArea().getCamera().shake(0.2f, 5);
                }
                collision = true;
            }
        }
    }
}