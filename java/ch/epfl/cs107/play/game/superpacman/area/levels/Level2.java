/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Arcade;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
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
        registerActor(new Arcade(getWindow(), true, false, false));
        registerActor(new Door("superpacman/level3", Level3.PLAYER_SPAWN_POSITION, Logic.TRUE,
                               this, Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));
        registerActor(new Key(this, new DiscreteCoordinates(3, 16)));
        registerActor(new Key(this, new DiscreteCoordinates(26,16)));
        registerActor(new Key(this, new DiscreteCoordinates(2,8)));
        registerActor(new Key(this, new DiscreteCoordinates(27,8)));
    }

    @Override
    public String getTitle() {
        return "superpacman/level2";
    }
}
