/*
 *	Author:      Leonard Cseres
 *	Date:        27.11.20
 *	Time:        15:15
 */


package ch.epfl.cs107.play.game.superpacman.area.camera;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.Vector;

public class StaticCamera extends Camera {

    /**
     * Constructor for StaticCamera.
     * @param area          current area
     * @param doEdgeControl if true, the camera will stop moving in the axis where there is the area edge
     */
    public StaticCamera(Area area, boolean doEdgeControl) {
        super(area, doEdgeControl);
    }

    @Override
    protected float updateCamera(float playerPos, float cameraPos, float[] minMaxPos) {
        float pos = playerPos;
        if (isOutOfInterval(pos, minMaxPos)) {
            pos = cameraPos;
        }
        return pos;
    }

    @Override
    protected Vector getShakeModifier() {
        return getPlayerPosXY();
    }

}
