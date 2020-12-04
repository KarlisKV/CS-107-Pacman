/*
 *	Author:      Leonard Cseres
 *	Date:        30.11.20
 *	Time:        12:56
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Pinky extends Ghost {
    private static final String SPRITE_NAME = "superpacman/ghost.pinky";
    private static final int SPRITE_SIZE = 16;
    private static final int MIN_AFRAID_DISTANCE = 5;
    private static final int RANDOM_POS_RANGE = 20;
    private static final int MAX_RANDOM_ATTEMPT = 2000000000;
    private static final int FIELD_OF_VIEW = 5;
    private int count;

    public Pinky(Area area, DiscreteCoordinates homePosition) {
        super(area, homePosition, homePosition, SPRITE_NAME, SPRITE_SIZE, Glow.GlowColors.PINK, FIELD_OF_VIEW);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        if (isPlayerInView()) {
            if (isStateUpdate()) {
                count = 0;
            }
            ++count;
            if (count < MAX_RANDOM_ATTEMPT) {
                // Get away from player
                return getRandomValidPosition(
                        getCellsFromRange(getCurrentMainCellCoordinates(), MIN_AFRAID_DISTANCE, true));
            } else {
                return null;
            }
        }
        return getTargetDefault();
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
        return getRandomValidPosition(getCellsFromRange(getCurrentMainCellCoordinates(), RANDOM_POS_RANGE, false));
    }
}
