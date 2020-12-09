/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level1 extends SuperPacmanArea {
    public static final String LEVEL_PATHNAME = "superpacman/level1";
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }


    @Override
    protected void createArea() {
        // Door
       registerActor(new Door(Level2.LEVEL_PATHNAME, Level2.PLAYER_SPAWN_POSITION, Logic.TRUE,
                               this, Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));
        // Gates
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), Logic.FALSE));
        registerActor(new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), Logic.FALSE));
    }

    @Override
    public String getTitle() {
        return LEVEL_PATHNAME;
    }

}
