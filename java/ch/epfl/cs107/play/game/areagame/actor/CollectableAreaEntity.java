/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        04:26
 */


package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public abstract class CollectableAreaEntity extends MovableAreaEntity {

    /**
     * Default MovableAreaEntity constructor
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public CollectableAreaEntity(Area area, DiscreteCoordinates position) {
        super(area, Orientation.UP, position);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        v.interactWith(this);

    }

    public int getPoints() {
        return 0;
    }

    public void collect() {
        getOwnerArea().unregisterActor(this);
    }

    public void setCollectablePosition(float x, float y) {
        setCurrentPosition(new Vector(x, y));
    }

}