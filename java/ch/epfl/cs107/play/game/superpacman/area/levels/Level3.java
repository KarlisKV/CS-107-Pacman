/*
 *	Author:      Leonard Cseres
 *	Date:        02.12.20
 *	Time:        23:35
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level3 extends SuperPacmanArea {
    public static final String LEVEL_PATHNAME = "superpacman/level3";
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
        registerActor(new Foreground(this));
        // In Area Tunnels (Doors)
        createDoorTunnel(DOOR_TOP_RIGHT.left(), Orientation.RIGHT, DOOR_TOP_LEFT);
        createDoorTunnel(DOOR_TOP_LEFT.right(), Orientation.LEFT, DOOR_TOP_RIGHT);
        createDoorTunnel(DOOR_BOTTOM_RIGHT.left(), Orientation.RIGHT, DOOR_BOTTOM_LEFT);
        createDoorTunnel(DOOR_BOTTOM_LEFT.right(), Orientation.LEFT, DOOR_BOTTOM_RIGHT);

        // Door
        registerActor(new Door(LevelEPFL.LEVEL_PATHNAME, LevelEPFL.PLAYER_SPAWN_POSITION, Logic.TRUE,
                               this, Orientation.UP, new DiscreteCoordinates(18, 4), new DiscreteCoordinates(19, 4)));
    }

    /**
     * Create tunnel for the pacman to go through
     * @param otherSideCoordinates (DiscreteCoordinates) coordinate where the pacman will exit the tunnel
     * @param orientation          (Orientation) of the pacman
     * @param position             (DiscreteCoordinates) coordinate of the tunnel
     */
    private void createDoorTunnel(DiscreteCoordinates otherSideCoordinates, Orientation orientation,
                                  DiscreteCoordinates position) {
        registerActor(new Door(LEVEL_PATHNAME, otherSideCoordinates, Logic.TRUE, this, orientation, position));
    }

    @Override
    public String getTitle() {
        return LEVEL_PATHNAME;
    }
}
