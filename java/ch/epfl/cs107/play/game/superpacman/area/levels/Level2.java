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

public class Level2 extends SuperPacmanArea {
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }

    @Override
    protected void createArea() {

        registerActor(new Door("superpacman/level3", Level3.PLAYER_SPAWN_POSITION, Logic.TRUE,
                               this, Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));
        //keys
        Key key1 = new Key(this, new DiscreteCoordinates(3, 16));
        Key key2 = new Key(this, new DiscreteCoordinates(26, 16));
        Key key3 = new Key(this, new DiscreteCoordinates(2, 8));
        Key key4 = new Key(this, new DiscreteCoordinates(27, 8));
        registerActor(key1);
        registerActor(key2);
        registerActor(key3);
        registerActor(key4);

        //gates
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 14), Logic.FALSE, key1));
        registerActor(new Gate(this, Orientation.DOWN, new DiscreteCoordinates(5, 12), Logic.FALSE, key1));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 10), Logic.FALSE, key1));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 8), Logic.FALSE, key1));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 14), Logic.FALSE, key2));
        registerActor(new Gate(this, Orientation.DOWN, new DiscreteCoordinates(24, 12), Logic.FALSE, key2));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 10), Logic.FALSE, key2));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 8), Logic.FALSE, key2));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10, 2), Logic.FALSE, key3, key4));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19, 2), Logic.FALSE, key3, key4));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12, 8), Logic.FALSE, key3, key4));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17, 8), Logic.FALSE, key3, key4));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), Logic.FALSE));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), Logic.FALSE));

    }

    @Override
    public String getTitle() {
        return "superpacman/level2";
    }
}