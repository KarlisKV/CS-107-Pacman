/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        19:20
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

public abstract class Ghost extends MovableAreaEntity implements Interactor {
    protected static final int GHOST_SCORE = 500;
    private static final int ANIMATION_DURATION = 12;
    private static final int BACK_TO_HOME_ANIMATION_DURATION = 6;
    private static final float FRIGHTENED_TIME = 30;
    private static final float RESTART_TIME = 1;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private static final int NORMAL_GLOW = 0;
    private static final int FRIGHTENED_GLOW = 1;
    private static boolean restartAll = false;

    private final Animation[] normalAnimation;
    private final Animation frightenedAnimation;
    private final Animation[] backToHomeAnimation;

    private final DiscreteCoordinates homePosition;
    private final Glow[] glows = new Glow[2];

    private final GhostInteractionHandler ghostHandler;
    private float restartCount = RESTART_TIME;
    private boolean hasRestarted = false;
    private int movementDuration = ANIMATION_DURATION;
    private DiscreteCoordinates lastPlayerPosition;
    private DiscreteCoordinates scatterPosition;
    private Orientation currentOrientation = DEFAULT_ORIENTATION;
    private float frightenedCount = FRIGHTENED_TIME;
    private boolean frightened = false;
    private boolean chase;
    private boolean backToHome = false;
    private boolean playerInView = false;
    private Queue<Orientation> path = null;
    private DiscreteCoordinates targetPos = null;
    private Path graphicPath = null;
    private boolean stateUpdate = false;

    /**
     * Default MovableAreaEntity constructor
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public Ghost(Area area, DiscreteCoordinates position, String spriteName, int spriteSize,
                 Glow.GlowColors glowColor) {
        super(area, DEFAULT_ORIENTATION, position);
        homePosition = position;
        ghostHandler = new GhostInteractionHandler();

        // Frighted Animation
        Sprite[] frightenedSprites =
                RPGSprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16);
        frightenedAnimation = new Animation(ANIMATION_DURATION / 2, frightenedSprites);
        // Normal Animation
        Sprite[][] sprites = RPGSprite.extractSprites(spriteName, 2, 1, 1, this, spriteSize, spriteSize,
                                                      new Orientation[]{Orientation.UP, Orientation.RIGHT,
                                                                        Orientation.DOWN, Orientation.LEFT});
        normalAnimation = Animation.createAnimations(ANIMATION_DURATION / 2, sprites);
        // BackToHome sprites
        Sprite[][] eyesSprites = RPGSprite.extractSprites("superpacman/ghost.eyes", 1, 1, 1, this, 16, 16,
                                                          new Orientation[]{Orientation.UP, Orientation.RIGHT,
                                                                            Orientation.DOWN, Orientation.LEFT});
        backToHomeAnimation = Animation.createAnimations(0, eyesSprites);

        // Glow for spires
        glows[NORMAL_GLOW] = new Glow(this, sprites[0][0], glowColor, 5.0f, 0.6f);
        glows[FRIGHTENED_GLOW] = new Glow(this, sprites[0][0], Glow.GlowColors.BLUE, 5.0f, 0.6f);
        resetMotion();

    }

    public static void setRestartAll(boolean restartAll) {
        Ghost.restartAll = restartAll;
    }

    public DiscreteCoordinates getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(DiscreteCoordinates targetPos) {
        this.targetPos = targetPos;
    }

    public Queue<Orientation> getPath() {
        return path;
    }

    public void setPath(Queue<Orientation> path) {
        this.path = path;
    }

    public boolean isStateUpdate() {
        return stateUpdate;
    }

    public DiscreteCoordinates getLastPlayerPosition() {
        return lastPlayerPosition;
    }

    protected boolean isPlayerInView() {
        return playerInView;
    }

    public void setPlayerInView(boolean playerInView) {
        stateUpdate = playerInView != this.playerInView;
        this.playerInView = playerInView;
    }

    protected boolean isFrightened() {
        return frightened;
    }

    public void setFrightened(boolean frightened) {
        stateUpdate = frightened != this.frightened;
        this.frightened = frightened;
    }

    public void setBackToHome(boolean backToHome) {
        path = null;
        this.backToHome = backToHome;
    }

    @Override
    public void update(float deltaTime) {
        updateAnimation(deltaTime);

        if (restartAll) {
            restartCount -= deltaTime;
            if (!hasRestarted) {
                restart();
            } else if (restartCount <= 0) {
                restartCount = RESTART_TIME;
                hasRestarted = false;
                restartAll = false;
            }
        }

        if (!isDisplacementOccurs()) {
            currentOrientation = getNextOrientation();
            orientate(currentOrientation);
        }

        if (frightened) {
            if (frightenedCount == FRIGHTENED_TIME) {
                orientate(currentOrientation.opposite());
            }
            frightenedCount -= deltaTime;
            if (frightenedCount <= 0) {
                frightenedCount = FRIGHTENED_TIME;
                setFrightened(false);
            }
        }
        if (backToHome && reachedDestination(homePosition)) {
            currentOrientation = DEFAULT_ORIENTATION;
            movementDuration = ANIMATION_DURATION;
            setBackToHome(false);
        }

        if (!isDisplacementOccurs()) {
            move(movementDuration);
        }

        setPlayerInView(false);
        super.update(deltaTime);
    }

    private Orientation getNextOrientation() {
        if (backToHome) {
            return moveToTarget(homePosition);
        } else if (playerInView) {
            return moveToTarget(getTargetWhilePlayerInVew());
        } else if (frightened) {
            return moveToTarget(getTargetWhileFrightened());
        } else if (chase) {
            return moveToTarget(getTargetWhileChaseMode());
        } else {
            return moveToTarget(getTargetDefault());
        }
    }

    abstract protected DiscreteCoordinates getTargetWhileFrightened();

    abstract protected DiscreteCoordinates getTargetWhilePlayerInVew();

    abstract protected DiscreteCoordinates getTargetWhileChaseMode();

    abstract protected DiscreteCoordinates getTargetDefault();


    protected Orientation getRandomOrientation() {
        List<Orientation> possibleOrientations = getPossibleOrientations();
        if (possibleOrientations.isEmpty()) {
            return currentOrientation.opposite();
        }
        int randomInt = RandomGenerator.getInstance().nextInt(possibleOrientations.size());
        return possibleOrientations.get(randomInt);
    }

    protected List<Orientation> getPossibleOrientations() {
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

    protected boolean isMoveLegal(Orientation orientation) {
        return (getPossibleOrientations().contains(orientation));
    }

    protected Orientation moveToTarget(DiscreteCoordinates targetPos) {
        if (!invalidPath(targetPos)) {
            if (path == null || path.isEmpty() || stateUpdate || reachedDestination(this.targetPos)) {
                this.targetPos = targetPos;
                path = SuperPacmanAreaBehavior.areaGraph.shortestPath(getCurrentMainCellCoordinates(), targetPos);
            }

            if (isMoveLegal(path.peek())) {
                return path.poll();
            } else {
                path = null;
            }
        }
        return getRandomOrientation();
    }

    protected boolean reachedDestination(DiscreteCoordinates targetPos) {
        return targetPos != null && targetPos.equals(getCurrentMainCellCoordinates());
    }

    protected boolean invalidPath(DiscreteCoordinates targetPos) {
        return SuperPacmanAreaBehavior.areaGraph.shortestPath(getCurrentMainCellCoordinates(), targetPos) == null ||
                targetPos == null;
    }

    protected DiscreteCoordinates getRandomValidElement(List<DiscreteCoordinates> discreteCoordinates) {
        int randomInt;
        do {
            randomInt = RandomGenerator.getInstance().nextInt(discreteCoordinates.size());
        } while (invalidPath(discreteCoordinates.get(randomInt)));

        return discreteCoordinates.get(randomInt);
    }

    protected void reset() {
        setBackToHome(true);
        movementDuration = BACK_TO_HOME_ANIMATION_DURATION;
        setFrightened(false);
        frightenedCount = FRIGHTENED_TIME;
    }

    protected void restart() {
        // TODO: fix bug only 1 ghost teleporting to home
        System.out.println("restart");
        resetMotion();
        path = null;
        targetPos = null;
        backToHome = false;
        lastPlayerPosition = null;
        setFrightened(false);
        movementDuration = ANIMATION_DURATION;
        frightenedCount = FRIGHTENED_TIME;
        getOwnerArea().unregisterActor(this);
        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(homePosition));
        setCurrentPosition(homePosition.toVector());
        getOwnerArea().registerActor(this);
        hasRestarted = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (path != null) {
            graphicPath = new Path(this.getPosition(), new LinkedList<>(path));
            graphicPath.draw(canvas);
        }
        if (frightened) {
            frightenedAnimation.draw(canvas);
            glows[FRIGHTENED_GLOW].draw(canvas);
        } else if (backToHome) {
            backToHomeAnimation[currentOrientation.ordinal()].draw(canvas);
        } else {
            normalAnimation[currentOrientation.ordinal()].draw(canvas);
            glows[NORMAL_GLOW].draw(canvas);
        }
    }

    private void updateAnimation(float deltaTime) {
        normalAnimation[currentOrientation.ordinal()].update(deltaTime);
        frightenedAnimation.update(deltaTime);
    }

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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCellsFromRange(getCurrentMainCellCoordinates(), 5, false);
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(ghostHandler);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return !backToHome && !restartAll;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    private class GhostInteractionHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer player) {
            lastPlayerPosition = player.getCurrentCells().get(0);
            setPlayerInView(true);
        }
    }
}
