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

public class Level0 extends SuperPacmanArea {
    public static final String LEVEL_PATHNAME = "superpacman/level0";
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }

    @Override
    protected void createArea() {
        // Door
        registerActor(new Door(Level1.LEVEL_PATHNAME, Level1.PLAYER_SPAWN_POSITION, Logic.TRUE,
                               this, Orientation.UP, new DiscreteCoordinates(5, 9), new DiscreteCoordinates(6, 9)));

        // Key
        Key key = new Key(this, new DiscreteCoordinates(3, 4));

        // Gates
        registerActor(key);
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(5, 8), Logic.FALSE, key));
        registerActor(new Gate(this, Orientation.LEFT, new DiscreteCoordinates(6, 8), Logic.FALSE, key));
    }

    @Override
    public String getTitle() {
        return LEVEL_PATHNAME;
    }

}
