/*
 *	Author:      Leonard Cseres
 *	Date:        30.11.20
 *	Time:        03:15
 */


package ch.epfl.cs107.play.game.superpacman.actor.ghosts;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Inky extends Ghost {
    private static final String SPRITE_NAME = "superpacman/ghost.inky";
    private static final int SPRITE_SIZE = 16;
    private static final int FIELD_OF_VIEW = 5;
    private static final int MAX_DISTANCE_WHEN_SCARED = 5;
    private static final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private static final DiscreteCoordinates SCATTER_POS = new DiscreteCoordinates(24, 24);

    public Inky(Area area, DiscreteCoordinates homePosition) {
        super(area, homePosition, homePosition, SPRITE_NAME, SPRITE_SIZE, Glow.GlowColors.LIGHT_BLUE, FIELD_OF_VIEW);
        setScatterPosition(SCATTER_POS);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        return getRandomValidPosition(
                getCellsFromRange(getScatterPosition(), MAX_DISTANCE_WHEN_SCARED, false));
    }

    @Override
    protected DiscreteCoordinates getTargetWhilePlayerInVew() {
        return getLastPlayerPosition();
    }

    @Override
    protected DiscreteCoordinates getTargetWhileChaseMode() {
        // Returning null on purpose, Orientation will be chose randomly
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetDefault() {
        return getRandomValidPosition(
                getCellsFromRange(getScatterPosition(), MAX_DISTANCE_WHEN_NOT_SCARED, false));
    }

}
