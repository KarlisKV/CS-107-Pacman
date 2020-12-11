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

public class LevelEPFL extends SuperPacmanArea {
    public static final String LEVEL_PATHNAME = "superpacman/levelEPFL";
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 3);

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }


    @Override
    protected void createArea() {

        //Registers the door for the level
        registerActor(new Door(Level2.LEVEL_PATHNAME, Level2.PLAYER_SPAWN_POSITION, Logic.TRUE,
         this, Orientation.DOWN, new DiscreteCoordinates(14, 10), new DiscreteCoordinates(15, 10)));

        //Create HashMap with the list of keys
        HashMap<String, Key> keyList = new HashMap<>();

        keyList.put("keyTopLeft", createKey(new DiscreteCoordinates(1, 1)));
        keyList.put("keyTopRight", createKey(new DiscreteCoordinates(28, 9)));
        keyList.put("keyBottomLeft", createKey(new DiscreteCoordinates(1, 9)));
        keyList.put("keyBottomRight", createKey(new DiscreteCoordinates(28, 1)));

        // Register all keys from map
        for (Key key : keyList.values()) {
            registerActor(key);
        }

        //Gates for passage to next level
        createRightOrientatedGate(new DiscreteCoordinates(14, 9), keyList.get("keyTopLeft"),
                keyList.get("keyBottomRight"));
        createRightOrientatedGate(new DiscreteCoordinates(15, 9), keyList.get("keyBottomLeft"),
                keyList.get("keyBottomRight"));

        //Gates for unlocking keys
        createGate(Orientation.LEFT, new DiscreteCoordinates(1, 2), keyList.get("keyTopRight"));
        createGate(Orientation.DOWN, new DiscreteCoordinates(2, 1), keyList.get("keyTopRight"));
        createGate(Orientation.DOWN, new DiscreteCoordinates(27, 1), keyList.get("keyTopLeft"));
        createGate(Orientation.LEFT, new DiscreteCoordinates(28, 2), keyList.get("keyTopLeft"));

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
     * @param position (DiscreteCoordinates) coordinates of the gate
     * @param key (Key) that is linked to the corresponding gate
     */
    private void createGate(Orientation orientation, DiscreteCoordinates position, Key key) {
        registerActor(new Gate(this, orientation, position, Logic.FALSE, key));
    }

    /**
     * Method for creating gate with two keys
     * @param position (DiscreteCoordinates) coordinates of the gate
     * @param key1 (Key) that is linked to the corresponding gate
     * @param key2 (Key) that is linked to the corresponding gate
     */
    private void createRightOrientatedGate(DiscreteCoordinates position, Key key1, Key key2) {
        registerActor(new Gate(this, Orientation.RIGHT, position, Logic.FALSE, key1, key2));

    }

    @Override
    public String getTitle() {
        return LEVEL_PATHNAME;
    }

}

