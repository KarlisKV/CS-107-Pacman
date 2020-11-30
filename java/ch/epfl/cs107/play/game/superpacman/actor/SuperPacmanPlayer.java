/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:41
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level0;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level1;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level2;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
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
    private final Sprite[][] sprites;
    private final SuperPacmanPlayerHandler playerHandler = new SuperPacmanPlayerHandler();
    private final Animation[] animations;
    private int currentHp = 5;
    private int score = 0;
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION;
    private final SuperPacmanPlayerStatusGUI gui = new SuperPacmanPlayerStatusGUI(currentHp, MAX_HP, score);
    private final Glow glow;
    private Orientation desiredOrientation = null;
    private Orientation currentOrientation = DEFAULT_ORIENTATION;

    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, DEFAULT_ORIENTATION, coordinates);
        PLAYER_SPAWN_POSITION = coordinates;
        sprites = RPGSprite.extractSprites("superpacman/pacmanSmall", 4, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE,
                                           new Orientation[]{Orientation.DOWN, Orientation.LEFT, Orientation.UP,
                                                             Orientation.RIGHT});
        animations = Animation.createAnimations(ANIMATION_DURATION / 2 + 2, sprites);
        glow = new Glow(this, sprites[0][0], Glow.GlowColors.YELLOW, 5.0f, 0.5f);
        resetMotion();

    }
    private void reset() {
        if (currentHp > 0) {
            --currentHp;
        }
        resetMotion();
        // TODO: find better method
        DiscreteCoordinates intiPos = PLAYER_SPAWN_POSITION;
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
        getOwnerArea().unregisterActor(this);
        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(intiPos));
        setCurrentPosition(intiPos.toVector());
        getOwnerArea().registerActor(this);
        desiredOrientation = null;
        currentOrientation = DEFAULT_ORIENTATION;
    }

    @Override
    public void update(float deltaTime) {
        gui.update(currentHp, MAX_HP, score);
        updateAnimation(deltaTime);

        Keyboard keyboard = getOwnerArea().getKeyboard();
        setDesiredOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        setDesiredOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
        setDesiredOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        setDesiredOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        if (desiredOrientation != null) {
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

    private void setDesiredOrientation(Orientation orientation, Button b) {
        if (b.isDown()) {
            desiredOrientation = orientation;
        }
    }

    private void updateAnimation(float deltaTime) {
        if (isDisplacementOccurs()) {
            animations[currentOrientation.ordinal()].update(deltaTime);
        } else {
            animations[currentOrientation.ordinal()].reset();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        SuperPacman.getArcade().draw(canvas);
        gui.draw(canvas);
        glow.draw(canvas);
        animations[currentOrientation.ordinal()].draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(playerHandler);
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
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(Ghost ghost) {
            if (ghost.isFrightened()) {
                ghost.reset();
                score += Ghost.GHOST_SCORE;
            } else {
                reset();
                Ghost.setRestartAll(true);
            }
        }
    }
}