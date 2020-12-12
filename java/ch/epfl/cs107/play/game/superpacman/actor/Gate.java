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
    private Key singleLogicKey;
    private Key [] doubleLogicKey = new Key[2];

    private boolean checkIfTwoKeys;
    private Logic signal;

    /**
     * Constructor for gate that changes signals when the key has been picked up
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation) the desired orientation of the gate
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param signal (Logic) Signal of the gate, On/Off, By default is Logic.FALSE
     * @param singleLogicKey (Key) Key that is linked to the gate.
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal, Key singleLogicKey) {
        super(area, orientation, position);
        this.signal = signal;
        this.singleLogicKey = singleLogicKey;

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

    /**
     * Constructor for gate that changes signals when all pellets have been eaten
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation) the desired orientation of the gate
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param signal (Logic) Signal of the gate, On/Off, By default is Logic.FALSE
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);
        this.signal = signal;

        createGateSprite(orientation);
    }

    /**
     * Constructor for gate that changes signals when both keys have been picked up
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation) the desired orientation of the gate
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param signal (Logic) Signal of the gate, On/Off, By default is Logic.FALSE
     * @param key1 (Key) First key that is linked to the gate.
     * @param key2 (Key) Second key that is linked to the gate.
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal, Key key1, Key key2) {
        super(area, orientation, position);
        this.signal = signal;
        this.doubleLogicKey[0] = key1;
        this.doubleLogicKey[1] = key2;
        this.checkIfTwoKeys = true;
        createGateSprite(orientation);
    }

    /**
     * update method changing the signal of the gate
     * whenever the corresponding condition has been satisfied
     * 1. key has been picked up
     * 2. both keys have been picked up
     * 3. all pellets in the level have been eaten
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        //@TODO: this is how it checks if all pellets are eaten for the gates it changes the signal to TRUE
        if (Pellet.getNbrOfPelletsEaten() == Pellet.getTotalPellets()) {
            signal = Logic.TRUE;

        } else {
            if (checkIfTwoKeys) {
                if (doubleLogicKey[0].getSignal().isOn() && doubleLogicKey[1].getSignal().isOn()) {
                    signal = Logic.TRUE;
                }
            } else {
                if (singleLogicKey != null && singleLogicKey.getSignal().isOn()) {
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

    /**+
     * Draws the gate in the case when the signal is Logic.FALSE
     * @param canvas target, not null
     */
    @Override
    //@TODO and here it checks, if the signal is On then you do not draw the sprite
    public void draw(Canvas canvas) {
        if (signal.isOff()) {
            sprite.draw(canvas);
        }
    }
}
