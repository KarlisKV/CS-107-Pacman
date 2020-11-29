/*
 *	Author:      Leonard Cseres
 *	Date:        27.11.20
 *	Time:        15:15
 */


package ch.epfl.cs107.play.game.superpacman.area.camera;

import ch.epfl.cs107.play.game.areagame.Area;

public class SmoothLimited extends SmoothFollow {

    /**
     * Constructor for ParallaxCamera.
     * @param area            current area
     * @param tpCamera        teleport camera to player if he teleports (changes area...)
     * @param maxDisplacement the max displacement of the camera from the center
     * @param smoothStop      smooth acceleration/deceleration
     */
    public SmoothLimited(Area area, boolean tpCamera, boolean smoothStop, float maxDisplacement) {
        super(area, tpCamera, false, smoothStop);

        setCameraPosXY(CENTER_COORDINATES);
        setMinMaxPosX(
                new float[]{CENTER_COORDINATES.getX() - maxDisplacement, CENTER_COORDINATES.getX() + maxDisplacement});
        setMinMaxPosY(
                new float[]{CENTER_COORDINATES.getY() - maxDisplacement, CENTER_COORDINATES.getY() + maxDisplacement});
    }
}
