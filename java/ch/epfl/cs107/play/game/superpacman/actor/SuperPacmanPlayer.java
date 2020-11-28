/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:41
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {

    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 10;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private final Sprite sprite;
    private Orientation desiredOrientation = DEFAULT_ORIENTATION;
    private SuperPacmanPlayerHandler playerHandler = new SuperPacmanPlayerHandler();


    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, DEFAULT_ORIENTATION, coordinates);
        sprite = new Sprite("superpacman/bonus", 1.f, 1.f, this);
        resetMotion();
    }

    @Override
    public void update(float deltaTime) {

        Keyboard keyboard = getOwnerArea().getKeyboard();
        setDesiredOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        setDesiredOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
        setDesiredOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        setDesiredOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        List<DiscreteCoordinates> jumpedCell = Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));

        if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, jumpedCell)) {
            orientate(desiredOrientation);
        }
        if (!isDisplacementOccurs()){
            move(ANIMATION_DURATION);
        }

        super.update(deltaTime);
    }

    private void setDesiredOrientation(Orientation orientation, Button b) {
        if (b.isDown()) {
            desiredOrientation = orientation;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
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
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
    }

    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }
    }
}