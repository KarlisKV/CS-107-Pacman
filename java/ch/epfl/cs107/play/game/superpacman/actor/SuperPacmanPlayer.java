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
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {

    /// Animation duration in frame number
    private static final int ANIMATION_DURATION = 10;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private static final int SPRITE_SIZE = 14;
    private final Sprite[][] sprites;
    private final SuperPacmanPlayerHandler playerHandler = new SuperPacmanPlayerHandler();
    private final Animation[] animations;
    //    private static final int MAX_HP = 5;
//    private int currentHp = 3;
//    private int score = 0;
//    private ImageGraphics glow;
    private final SuperPacmanPlayerStatusGUI gui = new SuperPacmanPlayerStatusGUI(3, 5, 69);
    private final Glow glow = new Glow(this, Glow.GlowColors.YELLOW);
    private Orientation desiredOrientation = DEFAULT_ORIENTATION;
    private int currentOrientation;

    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, DEFAULT_ORIENTATION, coordinates);
        sprites = RPGSprite.extractSprites("superpacman/pacmanSmall",
                                           4,
                                           1,
                                           1,
                                           this,
                                           SPRITE_SIZE,
                                           SPRITE_SIZE,
                                           new Orientation[]{Orientation.DOWN, Orientation.LEFT, Orientation.UP,
                                                             Orientation.RIGHT});
        // TODO: fix animation speed - too fast
        animations = Animation.createAnimations(ANIMATION_DURATION / 2, sprites);
        resetMotion();

    }

    @Override
    public void update(float deltaTime) {

        Keyboard keyboard = getOwnerArea().getKeyboard();
        setDesiredOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        setDesiredOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
        setDesiredOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        setDesiredOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        List<DiscreteCoordinates> jumpedCell =
                Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));

        animationUpdate(deltaTime);
        if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, jumpedCell)) {
            orientate(desiredOrientation);
            // TODO: Refactor animation code to make it more readable...
            switch (desiredOrientation) {
                case DOWN:
                    currentOrientation = 2;
                    break;
                case LEFT:
                    currentOrientation = 3;
                    break;
                case UP:
                    currentOrientation = 0;
                    break;
                case RIGHT:
                    currentOrientation = 1;
                    break;
                default:
                    currentOrientation = 0;
                    break;
            }
        }
        if (!isDisplacementOccurs()) {
            move(ANIMATION_DURATION);
        }
        super.update(deltaTime);
    }

    private void setDesiredOrientation(Orientation orientation, Button b) {
        if (b.isDown()) {
            desiredOrientation = orientation;
        }
    }

    private void animationUpdate(float deltaTime) {
        if (isDisplacementOccurs()) {
            animations[currentOrientation].update(deltaTime);
        } else {
            animations[currentOrientation].reset();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        gui.draw(canvas);
        glow.draw(canvas);
        animations[currentOrientation].draw(canvas);
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
    }
}