/*
 *	Author:      Leonard Cseres
 *	Date:        02.12.20
 *	Time:        22:25
 */


package ch.epfl.cs107.play.game.superpacman.actor.ghosts;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class Clyde extends Ghost {
    private static final String SPRITE_NAME = "superpacman/ghost.clyde";
    private static final int FIELD_OF_VIEW = 35;
    private static final int FORWARD_VISION = 6;
    private static final int MIN_AFRAID_DISTANCE = 8;
    private static final int FORWARD_RANGE = 3;
    private static final float TRIGGER_TO_PLAYER = 4;

    public Clyde(Area area, DiscreteCoordinates homePosition) {
        super(area, homePosition, homePosition, SPRITE_NAME, Glow.GlowColors.ORANGE, FIELD_OF_VIEW);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        List<DiscreteCoordinates> cellsFromRange =
                getCellsFromRange(getCurrentMainCellCoordinates(), MIN_AFRAID_DISTANCE, true);
        float maxDistance = Float.MIN_VALUE;
        DiscreteCoordinates maxValidCell = null;
        if (getLastPlayerPosition() != null) {
            // Find the furthest away position from the player
            for (DiscreteCoordinates cell : cellsFromRange) {
                if (!invalidPath(cell)) {
                    float distance = cell.toVector().dist(getLastPlayerPosition().toVector());
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        maxValidCell = cell;
                    }
                }
            }
        }
        return maxValidCell;
    }

    @Override
    protected DiscreteCoordinates getTargetWhilePlayerInVew() {
        if (getLastPlayerPosition() != null && getLastPlayerOrientation() != null) {

            float distance = getCurrentMainCellCoordinates().toVector().dist(getLastPlayerPosition().toVector());
            if (distance >= TRIGGER_TO_PLAYER) {
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
