/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.area.levels;

import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2 extends SuperPacmanArea {
    private static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);

    @Override
    protected void createArea() {
        super.createArea();
        registerActor(new SuperPacmanPlayer(this, PLAYER_SPAWN_POSITION));
    }

    @Override
    public String getTitle() {
        return "superpacman/level2";
    }
}
