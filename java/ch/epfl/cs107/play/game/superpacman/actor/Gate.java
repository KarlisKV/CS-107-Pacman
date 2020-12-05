package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Key;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.menus.MenuItems;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Gate extends AreaEntity {
    private static final String GATE_PATHNAME = "superpacman/gate";
    private static final int DEPTH_GATE = -2000;
    private final Sprite sprite;
    private Key key;
    private Key key1;
    private Key key2;
    private boolean checkIfTwoKeys;
    private Logic signal;

    /**
     * +
     * @param area        area
     * @param orientation of the gate
     * @param position    of the gate
     * @param signal      stating whether the gate is on or off
     * @param key         key which is attached to the concrete gate
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal, Key key) {

        super(area, orientation, position);
        this.signal = signal;
        this.key = key;
        //this sets the gate's orientation correctly
        if (orientation == Orientation.RIGHT || orientation == Orientation.LEFT) {
            sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, 64, 64, 64));
        } else {
            sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, 0, 64, 64));
        }
        sprite.setDepth(DEPTH_GATE);

    }
    //constructor for the gate, i have no clue if this is how we should do it tbh
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area,orientation, position);
        this.signal = signal;


        //this sets the gate's orientation correctly
        if(orientation == Orientation.RIGHT || orientation == Orientation.LEFT) {

            sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, 64, 64, 64));
            sprite.setDepth(-1000);
        }
        else {
            sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, 0, 64, 64));
            sprite.setDepth(-1000);
        }
    }
    //construcctor for case with 2 keys
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal, Key key1, Key key2) {
        super(area,orientation, position);
        this.signal = signal;
        this.key1 = key1;
        this.key2 = key2;
        this.checkIfTwoKeys = true;

        //this sets the gate's orientation correctly
        if(orientation == Orientation.RIGHT || orientation == Orientation.LEFT) {

            sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, 64, 64, 64));
            sprite.setDepth(-1000);
        }
        else {
            sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, 0, 64, 64));
            sprite.setDepth(-1000);
        }
    }

    /**
     * +
     * update method changing the signal of the gate
     * whenever the key has been collected
     * @param deltaTime time
     */
    public void update(float deltaTime) {
        if(((SuperPacmanArea) getOwnerArea()).getEatenPellets() >= ((SuperPacmanArea) getOwnerArea()).getTotalPellets()) {
            signal = Logic.TRUE;
        }
        else {
            if(checkIfTwoKeys) {
                if(key1.getSignal().isOn() && key2.getSignal().isOn()) {
                    signal = Logic.TRUE;
                }
            }
            else {
                if(key != null) {
                    if(key.getSignal().isOn()) {
                        signal = Logic.TRUE;
                    }
                }
            }
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    public boolean takeCellSpace() {
        return signal.isOff();
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        //No interaction
    }

    // draw it only when the key is not picked up
    @Override
    public void draw(Canvas canvas) {
        if (signal.isOff()) {
            sprite.draw(canvas);
        }
    }
}
