/*
 *	Author:      Leonard Cseres
 *	Date:        30.11.20
 *	Time:        03:15
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Inky extends Ghost {
    private static final String SPRITE_NAME = "superpacman/ghost.inky";
    private static final int SPRITE_SIZE = 16;
    private static final int MAX_DISTANCE_WHEN_SCARED = 5;
    private static final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private static final DiscreteCoordinates SCATTER_POS = new DiscreteCoordinates(24, 24);

    public Inky(Area area, DiscreteCoordinates position) {
        super(area, position, SPRITE_NAME, SPRITE_SIZE, Glow.GlowColors.LIGHT_BLUE);
        setScatterPosition(SCATTER_POS);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        return getRandomValidElement(
                getCellsFromRange(getScatterPosition(), MAX_DISTANCE_WHEN_SCARED, false));
    }

    @Override
    protected DiscreteCoordinates getTargetWhilePlayerInVew() {
        return getLastPlayerPosition();
    }

    @Override
    protected DiscreteCoordinates getTargetWhileChaseMode() {
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetDefault() {
        return getRandomValidElement(
                getCellsFromRange(getScatterPosition(), MAX_DISTANCE_WHEN_NOT_SCARED, false));
    }

}
