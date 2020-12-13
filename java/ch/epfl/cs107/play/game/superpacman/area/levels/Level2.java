/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Key;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.HashMap;

public class Level2 extends SuperPacmanArea {
    public static final String LEVEL_PATHNAME = "superpacman/level2";
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);

    @Override
    public boolean isEndingLevel() {
        return false;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }

    @Override
    protected void createArea() {
        // Door
        registerActor(new Door(Level3.LEVEL_PATHNAME, Level3.PLAYER_SPAWN_POSITION, Logic.TRUE,
                               this, Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));

        //Create HashMap with the list of keys
        HashMap<String, Key> keyList = new HashMap<>();

        keyList.put("keyTopLeft", createKey(new DiscreteCoordinates(3, 16)));
        keyList.put("keyTopRight", createKey(new DiscreteCoordinates(26, 16)));
        keyList.put("keyBottomLeft", createKey(new DiscreteCoordinates(2, 8)));
        keyList.put("keyBottomRight", createKey(new DiscreteCoordinates(27, 8)));
        // Register all keys from map
        for (Key key : keyList.values()) {
            registerActor(key);
        }

        // Create and register gates
        createGate(Orientation.RIGHT, new DiscreteCoordinates(8, 14), keyList.get("keyTopLeft"));
        createGate(Orientation.DOWN, new DiscreteCoordinates(5, 12), keyList.get("keyTopLeft"));
        createGate(Orientation.RIGHT, new DiscreteCoordinates(8, 10), keyList.get("keyTopLeft"));
        createGate(Orientation.RIGHT, new DiscreteCoordinates(8, 8), keyList.get("keyTopLeft"));

        createGate(Orientation.RIGHT, new DiscreteCoordinates(21, 14), keyList.get("keyTopRight"));
        createGate(Orientation.DOWN, new DiscreteCoordinates(24, 12), keyList.get("keyTopRight"));
        createGate(Orientation.RIGHT, new DiscreteCoordinates(21, 10), keyList.get("keyTopRight"));
        createGate(Orientation.RIGHT, new DiscreteCoordinates(21, 8), keyList.get("keyTopRight"));

        createRightOrientatedGate(new DiscreteCoordinates(10, 2), keyList.get("keyBottomLeft"),
                                  keyList.get("keyBottomRight"));
        createRightOrientatedGate(new DiscreteCoordinates(19, 2), keyList.get("keyBottomLeft"),
                                  keyList.get("keyBottomRight"));
        createRightOrientatedGate(new DiscreteCoordinates(12, 8), keyList.get("keyBottomLeft"),
                                  keyList.get("keyBottomRight"));
        createRightOrientatedGate(new DiscreteCoordinates(17, 8), keyList.get("keyBottomLeft"),
                                  keyList.get("keyBottomRight"));

        createRightOrientatedGate(new DiscreteCoordinates(14, 3));
        createRightOrientatedGate(new DiscreteCoordinates(15, 3));

    }

    /**
     * Method for key creation
     * @param position (DiscreteCoordinates) position of the key
     * @return created key
     */
    private Key createKey(DiscreteCoordinates position) {
        return new Key(this, position);
    }

    /**
     * Method for creating gate with one key
     * @param orientation (Orientation) of the gate
     * @param position    (DiscreteCoordinates) coordinates of the gate
     * @param key         (Key) that is linked to the corresponding gate
     */
    private void createGate(Orientation orientation, DiscreteCoordinates position, Key key) {
        registerActor(new Gate(this, orientation, position, Logic.FALSE, key));

    }

    /**
     * Method for creating gate with two keys
     * @param position (DiscreteCoordinates) coordinates of the gate
     * @param key1     (Key) that is linked to the corresponding gate
     * @param key2     (Key) that is linked to the corresponding gate
     */

    private void createRightOrientatedGate(DiscreteCoordinates position, Key key1, Key key2) {
        registerActor(new Gate(this, Orientation.RIGHT, position, Logic.FALSE, key1, key2));

    }

    /**
     * Method for creating gate whose signal depends on whether all pellets are picked up
     * @param position (DiscreteCoordinates) coordinates of the gate
     */
    private void createRightOrientatedGate(DiscreteCoordinates position) {
        registerActor(new Gate(this, Orientation.RIGHT, position, Logic.FALSE));
    }

    @Override
    public String getTitle() {
        return LEVEL_PATHNAME;
    }
}