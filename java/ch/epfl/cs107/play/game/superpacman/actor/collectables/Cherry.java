/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        04:29
 */


package ch.epfl.cs107.play.game.superpacman.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Cherry extends CollectableAreaEntity {
    private static final int POINTS = 200;
    private final Sprite sprite;

    /**
     * Default MovableAreaEntity constructor
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public Cherry(Area area, DiscreteCoordinates position) {
        super(area, position);
        // temporary path name for "Cherry Sprite", to create new classes for every
        sprite = new Sprite("superpacman/cherry", 1, 1, this);
        sprite.setDepth(DEPTH_COLLECTABLES);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
