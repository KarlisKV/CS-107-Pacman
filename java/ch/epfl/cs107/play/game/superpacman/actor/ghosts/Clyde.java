/*
 *	Author:      Leonard Cseres
 *	Date:        02.12.20
 *	Time:        22:25
 */


package ch.epfl.cs107.play.game.superpacman.actor.ghosts;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Clyde extends Ghost {
    private static final String SPRITE_NAME = "superpacman/ghost.clyde";
    private static final int FIELD_OF_VIEW = 35;
    private static final int FORWARD_VISION = 6;
    private static final int MIN_AFRAID_DISTANCE = 10;
    private static final int FORWARD_RANGE = 3;

    public Clyde(Area area, DiscreteCoordinates homePosition) {
        super(area, homePosition, homePosition, SPRITE_NAME, Glow.GlowColors.ORANGE, FIELD_OF_VIEW);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        // Returning null on purpose, Orientation will be chose randomly
        return getRandomValidPosition(
                getCellsFromRange(getCurrentMainCellCoordinates(), MIN_AFRAID_DISTANCE, true));
    }

    @Override
    protected DiscreteCoordinates getTargetWhilePlayerInVew() {
        if (getLastPlayerPosition() != null && getLastPlayerOrientation() != null) {

            float xA = getCurrentMainCellCoordinates().toVector().getX();
            float yA = getCurrentMainCellCoordinates().toVector().getY();
            float xB = getLastPlayerPosition().toVector().getX();
            float yB = getLastPlayerPosition().toVector().getY();
            float distance = (float) Math.sqrt(Math.pow(xB - xA, 2) + Math.pow(yB - yA, 2));
            if (distance >= 4) {
                DiscreteCoordinates target =
                        getRandomValidPosition(getCellsFromRange(
                                getLastPlayerPosition()
                                        .jump(getLastPlayerOrientation().toVector().resized(FORWARD_VISION)),
                                FORWARD_RANGE, false));
                if (!invalidPath(target)) {
                    return target;
                }
            }
            return getLastPlayerPosition();
        } else {
            return null;
        }

    }

    @Override
    protected DiscreteCoordinates getTargetWhileChaseMode() {
        return getTargetWhilePlayerInVew();
    }

    @Override
    protected DiscreteCoordinates getTargetDefault() {
        return getTargetWhilePlayerInVew();
    }
}
