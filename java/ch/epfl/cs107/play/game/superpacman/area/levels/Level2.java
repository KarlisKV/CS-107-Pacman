/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2 extends SuperPacmanArea {
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);

    @Override
    protected void createArea() {
    }

    @Override
    public String getTitle() {
        return "superpacman/level2";
    }
}
