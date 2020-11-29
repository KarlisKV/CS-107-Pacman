/*
 *	Author:      Leonard Cseres
 *	Date:        27.11.20
 *	Time:        15:15
 */


package ch.epfl.cs107.play.game.superpacman.area.camera;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.Vector;

public class Fixed extends Camera {

    /**
     * Constructor for Fixed camera.
     * @param area current area
     */
    public Fixed(Area area) {
        super(area, false, false);
    }

    @Override
    protected Vector getShakeModifier() {
        return getCameraPosXY();
    }

}
