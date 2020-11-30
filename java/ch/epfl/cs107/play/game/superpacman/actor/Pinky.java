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
    private static final int MAX_RANDOM_ATTEMPT = 200;
    private int count;

    public Pinky(Area area, DiscreteCoordinates position) {
        super(area, position, SPRITE_NAME, SPRITE_SIZE, Glow.GlowColors.PINK);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetWhilePlayerInVew() {
        if (isFrightened()) {
            if (isStateUpdate()) {
                count = 0;
            }
            ++count;
            if (count < MAX_RANDOM_ATTEMPT) {
                return getRandomValidElement(
                        getCellsFromRange(getCurrentMainCellCoordinates(), MIN_AFRAID_DISTANCE, true));
            } else {
                return null;
            }
        }
        return getLastPlayerPosition();
    }

    @Override
    protected DiscreteCoordinates getTargetWhileChaseMode() {
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetDefault() {
        return getRandomValidElement(getCellsFromRange(getCurrentMainCellCoordinates(), 20, false));
    }
}
