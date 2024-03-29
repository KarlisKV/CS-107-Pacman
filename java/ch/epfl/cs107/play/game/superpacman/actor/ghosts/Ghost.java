/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        19:20
 */


package ch.epfl.cs107.play.game.superpacman.actor.ghosts;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SoundUtility;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDepth;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanSound;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

public abstract class Ghost extends MovableAreaEntity implements Interactor {
    // Default attributes
    public static final int GHOST_BASE_SCORE = 200;
    private static final float RESET_TIME = 4;
    private static final float EATEN_TIME = 2;
    private static final int BACK_TO_HOME_ANIMATION_DURATION = 5; // base 5
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private static final int NORMAL_GLOW = 0;
    private static final int FRIGHTENED_GLOW = 1;
    private static final int TRANSITION_GLOW = 2;
    private static final float TRANSITION_BLINK_SPEED = 0.25f;
    private static final String[] SCORE_COMBOS_PATHNAMES =
            {"superpacman/score200", "superpacman/score400", "superpacman/score800", "superpacman/score1600"};
    private static final int SPRITE_SIZE = 16;
    private static final SoundAcoustics RETREATING_SOUND = SuperPacmanSound.RETREATING.sound;
    private static SoundUtility ghostSoundUtility;
    private final int fieldOfView;
    // Visuals & Sound
    private final Animation[] normalAnimation;
    private final Animation transitionAnimation;
    private final Animation[] backToHomeAnimation;
    private final Sprite[] scores;
    private final Glow[] glows = new Glow[3];
    // Class management
    private final GhostInteractionHandler ghostHandler;
    // Ghost key attributes
    private final DiscreteCoordinates homePosition;
    private final boolean chase = false;
    private final Animation frightenedAnimation;
    // Difficulty parameters
    private int animationDuration = 16;  // default value for normal difficulty
    private float frightenTime = 10;     // default value for normal difficulty
    private float stateUpdateTime = 2;   // default value for normal difficulty
    // Game state
    private boolean reset = false;
    private boolean paused = false;
    private boolean gameOver = false;
    private float pauseTime = 0;
    private float timer = 0;
    private boolean timerIsFinished = false;
    // Movement
    private Orientation currentOrientation = DEFAULT_ORIENTATION;
    private int movementDuration = animationDuration;
    private DiscreteCoordinates scatterPosition;
    private boolean isFrightened = false;
    private float frightenedTimeCounter = 0;
    private boolean playerInView = false;
    private boolean isEaten = false;
    private boolean stateUpdate = true;
    private float stateCount;
    private boolean hasReset = false;
    private boolean blink = false;
    private float blinkCount;
    private boolean comboOccurring;
    private int comboIndex;
    // Orientation pathing
    private Queue<Orientation> path = null;
    private DiscreteCoordinates targetPos = null;
    private DiscreteCoordinates lastPlayerPosition = null;
    private Orientation lastPlayerOrientation = null;

    /**
     * Constructor for Ghost
     * @param area            (Area): Owner area. Not null
     * @param homePosition    (Coordinate): Initial position of the entity. Not null
     * @param scatterPosition (Coorinate): The target position while in scatter mode
     * @param spriteName      (String): The name of the Ghost sprite
     * @param glowColor       (Glow.GlowColors): The glow color for the Ghost sprite
     */
    protected Ghost(Area area, DiscreteCoordinates homePosition, DiscreteCoordinates scatterPosition, String spriteName,
                    Glow.GlowColors glowColor, int fieldOfView) {
        super(area, DEFAULT_ORIENTATION, homePosition);
        ghostHandler = new GhostInteractionHandler();
        this.homePosition = homePosition;
        this.scatterPosition = scatterPosition;
        this.fieldOfView = fieldOfView;

        scores = new Sprite[4];
        for (int i = 0; i < scores.length; ++i) {
            scores[i] = new Sprite(SCORE_COMBOS_PATHNAMES[i], 1.5f, 1.5f, this);
            scores[i].setAnchor(new Vector(0, 1.0f));
            scores[i].setDepth(SuperPacmanDepth.COMBO_TEXT.value);
        }

        // Frighted Animation
        Sprite[] frightenedSprites = createGhostSprites("superpacman/ghost.afraid");
        frightenedAnimation = new Animation(animationDuration / 2, frightenedSprites);

        // Frighted transition Animation
        Sprite[] transitionSprites = createGhostSprites("superpacman/ghost.afraid.transition");
        transitionAnimation = new Animation(animationDuration / 2, transitionSprites);


        // Normal Animation
        Sprite[][] sprites = createOrientableGhostSprites(spriteName);
        normalAnimation = Animation.createAnimations(animationDuration / 2, sprites);

        // BackToHome Animation
        Sprite[][] eyesSprites = createOrientableGhostSprites("superpacman/ghost.eyes");
        backToHomeAnimation = Animation.createAnimations(0, eyesSprites);

        // GLOW
        glows[NORMAL_GLOW] = new Glow(this, sprites[0][0], glowColor, 4.0f, 0.9f);
        glows[FRIGHTENED_GLOW] = new Glow(this, sprites[0][0], Glow.GlowColors.BLUE, 4.0f, 0.9f);
        glows[TRANSITION_GLOW] = new Glow(this, sprites[0][0], Glow.GlowColors.WHITE, 4.0f, 0.9f);

        // SOUND
        ghostSoundUtility = new SoundUtility(new SoundAcoustics[]{RETREATING_SOUND}, false);
    }

    /**
     * Method to create an array of Sprites with specific parameters
     * @param pathname the path name in the the file system
     * @return an array of "frames" of the specified Sprite
     */
    public Sprite[] createGhostSprites(String pathname) {
        Sprite[] sprites =
                RPGSprite.extractSprites(pathname, 2, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE);
        for (Sprite sprite : sprites) {
            sprite.setDepth(SuperPacmanDepth.GHOSTS.value);
        }
        return sprites;
    }

    /**
     * Method to create a 2D array or orientated Sprites with specific parameters
     * @param pathname the path name in the the file system
     * @return a 2D array of "frames" of the specified Sprite for every Orientation
     */
    public Sprite[][] createOrientableGhostSprites(String pathname) {
        Sprite[][] sprites =
                RPGSprite.extractSprites(pathname, 2, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE,
                                         new Orientation[]{Orientation.UP, Orientation.RIGHT,
                                                           Orientation.DOWN, Orientation.LEFT});
        for (Sprite[] spriteFrames : sprites) {
            for (Sprite sprite : spriteFrames) {
                sprite.setDepth(SuperPacmanDepth.GHOSTS.value);
            }
        }
        return sprites;
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public static SoundUtility getGhostSoundManager() {
        return ghostSoundUtility;
    }

    protected void setGameOver() {
        this.gameOver = true;
    }

    protected boolean isEaten() {
        return isEaten;
    }

    protected void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        if (!isEaten) {
            movementDuration = animationDuration;
        }
    }

    protected boolean reachedHome() {
        return homePosition.equals(getCurrentMainCellCoordinates());
    }

    protected void setFrightenTime(float frightenTime) {
        this.frightenTime = frightenTime;
    }

    protected void setStateUpdateTime(float stateUpdateTime) {
        this.stateUpdateTime = stateUpdateTime;
    }

    protected DiscreteCoordinates getScatterPosition() {
        return scatterPosition;
    }

    protected void setScatterPosition(DiscreteCoordinates scatterPosition) {
        this.scatterPosition = scatterPosition;
    }

    protected DiscreteCoordinates getLastPlayerPosition() {
        return lastPlayerPosition;
    }

    protected Orientation getLastPlayerOrientation() {
        return lastPlayerOrientation;
    }

    protected boolean isPlayerInView() {
        return playerInView;
    }

    protected void setPlayerInView(boolean playerInView) {
        // update the state
        stateUpdate = true;
        this.playerInView = playerInView;
    }

    public boolean isFrightened() {
        return isFrightened;
    }

    protected boolean isStateUpdate() {
        return stateUpdate;
    }

    /**
     * Method to set the ghost as Eaten, sending him back to homePosition
     */
    public void setEaten() {
        // state check
        stateUpdate = true;
        ghostSoundUtility.play(RETREATING_SOUND, true);
        pause(EATEN_TIME);
        setFrightened(false);
        blink = false;
        path = null;
        movementDuration = BACK_TO_HOME_ANIMATION_DURATION;
        frightenedTimeCounter = 0;
        isEaten = true;
    }

    /**
     * Method to stop update of the ghost
     * @param time the amount of seconds to stop the Ghost
     */
    protected void pause(float time) {
        paused = true;
        pauseTime = time;
        ghostSoundUtility.setAudioPaused(true);
    }

    public void setFrightened(boolean frightened) {
        if (!isEaten) {
            // update the state
            stateUpdate = true;
            this.isFrightened = frightened;
            if (frightened) {
                blink = false;
                blinkCount = 0;
                frightenedTimeCounter = 0;
                // Switch direction
                currentOrientation = currentOrientation.opposite();
            }
        }
    }

    @Override
    public void bip(Audio audio) {
        ghostSoundUtility.bip(audio);
    }

    @Override
    public void update(float deltaTime) {
        updatePauseTimer(deltaTime);

        if (reset) {
            if (!hasReset) {
                reset();
            }
            if (timerIsFinished) {
                hasReset = false;
                reset = false;
            }
        }

        if (!paused && !gameOver) {
            updateAnimation(deltaTime);
            // Orientate the ghost
            if (!isDisplacementOccurs()) {
                if (getNextOrientation() != null) {
                    currentOrientation = getNextOrientation();
                } else {
                    currentOrientation = getRandomValidOrientation();
                }
                orientate(currentOrientation);
            }

            if (isFrightened) {
                frightenedTimeCounter += deltaTime;
                if (frightenedTimeCounter >= frightenTime) {
                    frightenedTimeCounter = 0;
                    setFrightened(false);
                    blink = false;
                } else {
                    // Make the ghost blink
                    if (frightenedTimeCounter >= frightenTime - 3) {
                        toggleBlink(deltaTime);
                    }
                }
            }
            if (isEaten && reachedHome()) {
                currentOrientation = DEFAULT_ORIENTATION;
                movementDuration = animationDuration;
                isEaten = false;
            }

            // Move the ghost
            if (!isDisplacementOccurs()) {
                move(movementDuration);
            }
        }

        // Rest and update state if needed
        stateCount += deltaTime;
        if (stateCount >= stateUpdateTime) {
            stateCount = 0;
            stateUpdate = true;
        } else {
            stateUpdate = false;
        }
        setPlayerInView(false);
        super.update(deltaTime);
    }

    /**
     * Method to start, stop, and check if timer is finished
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    private void updatePauseTimer(float deltaTime) {
        if (pauseTime != 0) {
            timer = pauseTime;
            pauseTime = 0;
        }
        if (timer >= 1) {
            timerIsFinished = false;
            timer -= deltaTime;
        } else {
            timerIsFinished = true;
            paused = false;
            ghostSoundUtility.setAudioPaused(false);
        }
    }

    /**
     * Reset Ghost, resetting all attributes to initial values
     */
    public void reset() {
        pause(RESET_TIME);
        resetMotion();
        resetAnimations();
        path = null;
        targetPos = null;
        isEaten = false;
        lastPlayerPosition = null;
        setFrightened(false);
        setPlayerInView(false);
        blink = false;
        blinkCount = 0;
        movementDuration = animationDuration;
        frightenedTimeCounter = 0;
        setGhostPosition(homePosition);
        hasReset = true;
    }

    /**
     * Method to update the animations
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    private void updateAnimation(float deltaTime) {
        normalAnimation[currentOrientation.ordinal()].update(deltaTime);
        frightenedAnimation.update(deltaTime);
        transitionAnimation.update(deltaTime);
    }

    /**
     * Method to move Ghost according to current state
     * @return Orientation from path.poll()
     */
    private Orientation getNextOrientation() {
        if (reachedDestination(targetPos) || isStateUpdate()) {
            if (isEaten) {
                return moveToTarget(homePosition);
            } else {
                if (isFrightened) {
                    return moveToTarget(getTargetWhileFrightened());
                }
                if (playerInView) {
                    return moveToTarget(getTargetWhilePlayerInVew());
                }
                if (chase) {
                    return moveToTarget(getTargetWhileChaseMode());
                } else {
                    return moveToTarget(getTargetDefault());
                }
            }
        }
        return moveToTarget(targetPos);
    }

    /**
     * Method to toggle to true and false the boolean blink with a specific time interval
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    public void toggleBlink(float deltaTime) {
        blinkCount += deltaTime;
        if (blinkCount > TRANSITION_BLINK_SPEED) {
            blinkCount = 0;
            this.blink = !this.blink;
        }
    }

    /**
     * Method to know if ghost reached the target position
     * @param targetPos target position of path
     * @return (true) if the current position equals the target position
     */
    private boolean reachedDestination(DiscreteCoordinates targetPos) {
        return targetPos != null && targetPos.equals(getCurrentMainCellCoordinates());
    }

    /**
     * Method to rest all animation of Ghost
     */
    private void resetAnimations() {
        for (Animation animation : normalAnimation) {
            animation.reset();
        }
        for (Animation animation : backToHomeAnimation) {
            animation.reset();
        }
        frightenedAnimation.reset();
        transitionAnimation.reset();
    }


    /**
     * Method to move Ghost towards the target position
     * @param targetPos target position of path
     * @return the next Orientation from the path or a random orientation if null
     */
    private Orientation moveToTarget(DiscreteCoordinates targetPos) {
        if (!invalidPath(targetPos)) {
            if (path == null || path.isEmpty()) {
                this.targetPos = targetPos;
                path = SuperPacmanAreaBehavior.areaGraph.shortestPath(getCurrentMainCellCoordinates(), targetPos);
            }

            if (isMoveLegal(path.peek())) {
                return path.poll();
            } else {
                path = null;
            }
        }
        return getClosestValidOrientation(targetPos);
    }

    /**
     * Abstract methods to get target while in a specific state
     * @return the target position
     */
    protected abstract DiscreteCoordinates getTargetWhileFrightened();

    protected abstract DiscreteCoordinates getTargetWhilePlayerInVew();

    protected abstract DiscreteCoordinates getTargetWhileChaseMode();

    protected abstract DiscreteCoordinates getTargetDefault();

    /**
     * Method to check if the desired next Orientation is legal
     * @param orientation the next Orientation
     * @return (true) if the move is legal
     */
    private boolean isMoveLegal(Orientation orientation) {
        return (getValidOrientations().contains(orientation));
    }

    /**
     * Method to get a random Orientation from the valid orientations
     * @return a random valid Orientation
     */
    private Orientation getRandomValidOrientation() {
        List<Orientation> possibleOrientations = getValidOrientations();
        // Handle dead end situation
        if (possibleOrientations.isEmpty()) {
            return currentOrientation.opposite();
        }
        int randomInt = RandomGenerator.getInstance().nextInt(possibleOrientations.size());
        return possibleOrientations.get(randomInt);
    }

    /**
     * Method to get all valid Orientation in the current cell
     * The rules inclue:
     * - no moving backwards, except if dead end
     * - no moving into walls
     * @return a List with all valid orientations
     */
    public List<Orientation> getValidOrientations() {
        List<Orientation> possibleOrientations = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            // Check if ghost can move in any directions
            Orientation orientation = Orientation.fromInt(i);
            List<DiscreteCoordinates> jumpedCell =
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(orientation.toVector()));
            if (getOwnerArea().canEnterAreaCells(this, jumpedCell) && orientation != currentOrientation.opposite()) {
                possibleOrientations.add(orientation);
            }
        }
        if (possibleOrientations.isEmpty()) {
            possibleOrientations.add(currentOrientation.opposite());
        }
        return possibleOrientations;
    }

    /**
     * Method to determine the closest valid Orientation the a target position
     * @param targetPos target position of the ghost
     * @return the closest valid Orientation, if none found, then returns a random valid Orientation
     */
    private Orientation getClosestValidOrientation(DiscreteCoordinates targetPos) {
        float minDistance = Float.MAX_VALUE;
        Orientation closestOrientation = getRandomValidOrientation();

        if (targetPos != null) {
            for (Orientation validOrientation : getValidOrientations()) {
                float distance = getCurrentMainCellCoordinates().toVector().add(validOrientation.toVector()).dist(targetPos.toVector());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestOrientation = validOrientation;
                }
            }
        }
        return closestOrientation;
    }

    /**
     * Method to get a random valid position from List of DiscreteCoordinates
     * @param discreteCoordinates the List of coordinates
     * @return a random DiscreteCoordinates from the List
     */
    protected DiscreteCoordinates getRandomValidPosition(List<DiscreteCoordinates> discreteCoordinates) {
        int randomInt;
        final int TIMEOUT = 200;
        int count = 0;
        do {
            ++count;
            randomInt = RandomGenerator.getInstance().nextInt(discreteCoordinates.size());
        } while (invalidPath(discreteCoordinates.get(randomInt)) && count < TIMEOUT);

        return discreteCoordinates.get(randomInt);
    }

    /**
     * Method to check if path to targetPos is invalid
     * @param targetPos target position of path
     * @return (true) if the path is invalid
     */
    protected boolean invalidPath(DiscreteCoordinates targetPos) {
        return SuperPacmanAreaBehavior.areaGraph.shortestPath(getCurrentMainCellCoordinates(), targetPos) == null ||
                targetPos == null;
    }

    @Override
    public void draw(Canvas canvas) {
        // Path
        if (MenuStateManager.isDebugMode() && path != null) {
            Path graphicPath = new Path(this.getPosition(), new LinkedList<>(path));
            graphicPath.setDepth(SuperPacmanDepth.GHOST_PATHING.value);
            graphicPath.draw(canvas);
        }
        if (isFrightened) {
            // Frightened animation
            if (!blink) {
                frightenedAnimation.draw(canvas);
                glows[FRIGHTENED_GLOW].draw(canvas);
            } else {
                // Blinking animation
                transitionAnimation.draw(canvas);
                glows[TRANSITION_GLOW].draw(canvas);
            }
        } else if (isEaten) {
            // Combo text
            if (paused) {
                if (!comboOccurring && SuperPacmanPlayer.getComboCount() - 1 >= 0) {
                    comboIndex = SuperPacmanPlayer.getComboCount() - 1;
                    comboOccurring = true;
                }
                scores[comboIndex].draw(canvas);
            }
            // Eye animation
            backToHomeAnimation[currentOrientation.ordinal()].draw(canvas);
        } else {
            // Normal Animation
            normalAnimation[currentOrientation.ordinal()].draw(canvas);
            glows[NORMAL_GLOW].draw(canvas);
            comboOccurring = false;
        }
    }

    /**
     * Method to set ghost into a new position, leaving the current cells
     * @param destination the new position
     */
    private void setGhostPosition(DiscreteCoordinates destination) {
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
        return !isEaten && !paused && !reset;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCellsFromRange(getCurrentMainCellCoordinates(), fieldOfView, false);
    }

    /**
     * Method to get List of cells in a specific range
     * @param initPos  the center position of the range
     * @param range    the range from the initPos
     * @param onlyEdge (true) get only the cells on the edge of the range
     * @return a List of all cells in the specified range
     */
    protected List<DiscreteCoordinates> getCellsFromRange(DiscreteCoordinates initPos, int range, boolean onlyEdge) {
        List<DiscreteCoordinates> cellsInView = new ArrayList<>();
        for (int x = -range; x <= range; x = onlyEdge ? x + (range * 2) : ++x) {
            for (int y = -range; y <= range; ++y) {
                cellsInView.add(initPos.jump(x, y));
            }
        }
        if (onlyEdge) {
            for (int y = -range; y <= range; y += (range * 2)) {
                for (int x = -range; x <= range; ++x) {
                    cellsInView.add(initPos.jump(x, y));
                }
            }
        }

        return cellsInView;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return !isEaten && !paused && !reset;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(ghostHandler);
    }

    /**
     * Interaction handler class for Ghost
     */
    private class GhostInteractionHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer player) {
            lastPlayerPosition = player.getCurrentCells().get(0);
            lastPlayerOrientation = player.getOrientation();
            if (!playerInView) {
                setPlayerInView(true);
            }
        }

        @Override
        public void interactWith(Door door) {
            if (door.isDestinationSameArea()) {
                setGhostPosition(door.getOtherSideCoordinates());
            }
        }
    }
}
