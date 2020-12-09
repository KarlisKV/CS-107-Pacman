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

public class LevelEPFL extends SuperPacmanArea {
    public static final String LEVEL_PATHNAME = "superpacman/levelEPFL";
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 3);

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }


    @Override
    protected void createArea() {
        // Door
        registerActor(new Door(Level2.LEVEL_PATHNAME, Level2.PLAYER_SPAWN_POSITION, Logic.TRUE,
         this, Orientation.DOWN, new DiscreteCoordinates(14, 10), new DiscreteCoordinates(15, 10)));
        // Gates
        // registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), Logic.FALSE));
        // registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), Logic.FALSE));
        Key key1 = new Key(this, new DiscreteCoordinates(1, 1));
        Key key2 = new Key(this, new DiscreteCoordinates(28, 1));
        Key key3 = new Key(this, new DiscreteCoordinates(1, 9));
        Key key4 = new Key(this, new DiscreteCoordinates(28, 9));
        // Gates
        registerActor(key1);
        registerActor(key2);
        registerActor(key3);
        registerActor(key4);

        registerActor(new Gate(this, Orientation.LEFT, new DiscreteCoordinates(14, 10), Logic.FALSE, key1, key2));
        registerActor(new Gate(this, Orientation.LEFT, new DiscreteCoordinates(15, 10), Logic.FALSE, key1, key2));
        registerActor(new Gate(this, Orientation.LEFT, new DiscreteCoordinates(1, 2), Logic.FALSE, key4));
        registerActor(new Gate(this, Orientation.UP, new DiscreteCoordinates(2, 1), Logic.FALSE, key4));
        registerActor(new Gate(this, Orientation.DOWN, new DiscreteCoordinates(27, 1), Logic.FALSE, key3));
        registerActor(new Gate(this, Orientation.LEFT, new DiscreteCoordinates(28, 2), Logic.FALSE, key3));
        // Gates

    }

    @Override
    public String getTitle() {
        return LEVEL_PATHNAME;
    }

}

