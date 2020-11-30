/*
 *	Author:      Leonard Cseres
 *	Date:        28.11.20
 *	Time:        16:04
 */


package ch.epfl.cs107.play.game.superpacman.area.camera;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.Vector;

public class Follow extends Camera {

    /**
     * Constructor for Follow Camera.
     * @param area          current area
     * @param tpCamera      teleport camera to player if he teleports (changes area...)
     * @param doEdgeControl if true, the camera will stop moving in the axis where there is the area edge
     */
    public Follow(Area area, boolean tpCamera, boolean doEdgeControl) {
        super(area, tpCamera, doEdgeControl);
    }

    @Override
    protected Vector getShakeModifier() {
        return getPlayerPosXY();
    }

    @Override
    protected float updateCamera(float playerPos, float cameraPos, float[] minMaxPos) {
        float pos = playerPos;
        if (isOutOfInterval(pos, minMaxPos)) {
            pos = cameraPos;
        }
        return pos;
    }
}
