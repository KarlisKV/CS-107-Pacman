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
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {

    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 6;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
    private final Sprite sprite;
    private Orientation desiredOrientation = null;


    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, DEFAULT_ORIENTATION, coordinates);
        sprite = new Sprite("yellowDot", 1.f, 1.f, this);
        orientate(DEFAULT_ORIENTATION); // TODO: check if useful
        resetMotion();
    }

    @Override
    public void update(float deltaTime) {

        Keyboard keyboard = getOwnerArea().getKeyboard();
        setDesiredOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        setDesiredOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
        setDesiredOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        setDesiredOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        super.update(deltaTime);
    }


    private void setDesiredOrientation(Orientation orientation, Button b) {
        if (b.isDown()) {
            desiredOrientation = orientation;

            if (isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, getCurrentCells())) {
                orientate(desiredOrientation);
                move(ANIMATION_DURATION);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {


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

    }
}