package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Key;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Pellet;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Gate extends AreaEntity {
    private static final String GATE_PATHNAME = "superpacman/gateGlow";
    private static final int DEPTH_GATE = -3500;
    private Sprite sprite;
    // TODO: Give descriptif name to keys (ex: key -> singleLogicKey, Key[] doubleLogicKey = new Key[2])
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

        createGateSprite(orientation);
    }

    /**
     * Method to create gate Sprite with specific parameters
     * @param orientation the desired Orientation of the gate
     */
    private void createGateSprite(Orientation orientation) {
        //this sets the gate's orientation correctly
        int yPixelOffset = 0;
        if (orientation == Orientation.RIGHT || orientation == Orientation.LEFT) {
            yPixelOffset = 64;
        }
        sprite = new RPGSprite(GATE_PATHNAME, 1, 1, this, new RegionOfInterest(0, yPixelOffset, 64, 64));
        sprite.setDepth(DEPTH_GATE);

    }

    //constructor for the gate, i have no clue if this is how we should do it tbh
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);
        this.signal = signal;

        createGateSprite(orientation);
    }

    //constructor for case with 2 keys
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal, Key key1, Key key2) {
        super(area, orientation, position);
        this.signal = signal;
        this.key1 = key1;
        this.key2 = key2;
        this.checkIfTwoKeys = true;

        createGateSprite(orientation);
    }

    /**
     * +
     * update method changing the signal of the gate
     * whenever the key has been collected
     * @param deltaTime time
     */
    @Override
    public void update(float deltaTime) {

        if (Pellet.getNbrOfPelletsEaten() == Pellet.getTotalPellets()) {
            signal = Logic.TRUE;
        } else {
            if (checkIfTwoKeys) {
                if (key1.getSignal().isOn() && key2.getSignal().isOn()) {
                    signal = Logic.TRUE;
                }
            } else {
                if (key != null && key.getSignal().isOn()) {
                    signal = Logic.TRUE;
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
        return signal.isOff() && !MenuStateManager.isDebugMode();
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
