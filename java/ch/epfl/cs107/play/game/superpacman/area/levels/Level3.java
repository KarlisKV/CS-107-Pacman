/*
 *	Author:      Leonard Cseres
 *	Date:        02.12.20
 *	Time:        23:35
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Arcade;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level3 extends SuperPacmanArea {
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(19, 33);
    private static final DiscreteCoordinates DOOR_TOP_LEFT = new DiscreteCoordinates(2, 25);
    private static final DiscreteCoordinates DOOR_TOP_RIGHT = new DiscreteCoordinates(35, 25);
    private static final DiscreteCoordinates DOOR_BOTTOM_LEFT = new DiscreteCoordinates(2, 13);
    private static final DiscreteCoordinates DOOR_BOTTOM_RIGHT = new DiscreteCoordinates(35, 13);


    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return PLAYER_SPAWN_POSITION;
    }

    @Override
    protected void createArea() {
        registerActor(new Arcade(getWindow(), true, false, false));
        registerActor(new Foreground(this));
        registerActor(new Door("superpacman/level3", DOOR_TOP_RIGHT.left(), Logic.TRUE, this, Orientation.RIGHT,
                               DOOR_TOP_LEFT));
        registerActor(new Door("superpacman/level3", DOOR_TOP_LEFT.right(), Logic.TRUE, this, Orientation.LEFT,
                               DOOR_TOP_RIGHT));
        registerActor(new Door("superpacman/level3", DOOR_BOTTOM_RIGHT.left(), Logic.TRUE, this, Orientation.RIGHT,
                               DOOR_BOTTOM_LEFT));
        registerActor(new Door("superpacman/level3", DOOR_BOTTOM_LEFT.right(), Logic.TRUE, this, Orientation.LEFT,
                               DOOR_BOTTOM_RIGHT));
    }

    @Override
    public String getTitle() {
        return "superpacman/level3";
    }
}
