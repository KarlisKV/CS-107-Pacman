/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        19:49
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Blinky extends Ghost {
    private static final String SPRITE_NAME = "superpacman/ghost.blinky";
    private static final int SPRITE_SIZE = 16;

    public Blinky(Area area, DiscreteCoordinates position) {
        super(area, position, SPRITE_NAME, SPRITE_SIZE, Glow.GlowColors.RED);
    }

    @Override
    protected DiscreteCoordinates getTargetWhileFrightened() {
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetWhilePlayerInVew() {
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetWhileChaseMode() {
        return null;
    }

    @Override
    protected DiscreteCoordinates getTargetDefault() {
        return null;
    }
}
