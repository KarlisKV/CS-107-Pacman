/*
 *	Author:      Leonard Cseres
 *	Date:        27.11.20
 *	Time:        15:15
 */


package ch.epfl.cs107.play.game.superpacman.area.camera;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.Vector;

public class SmoothFollow extends Camera {

    // How precise will the camera position itself if the player is no moving
    private static final float SPOT_PRECISION = 0.001f;
    // Trigger for edge detection
    private static final float EDGE_CONTROL_TRIGGER = 0.01f;
    // Speed of smooth camera
    private float cameraCatchupSpeed = 0.08f;
    private final boolean smoothStop;
    private boolean checkBounds = false;

    /**
     * Constructor for SmoothCamera.
     * @param area          current area
     * @param tpCamera      teleport camera to player if he teleports (changes area...)
     * @param doEdgeControl if true, the camera will stop moving in the axis where there is the area edge
     * @param smoothStop    smooth acceleration/deceleration
     */
    public SmoothFollow(Area area, boolean tpCamera, boolean doEdgeControl, float cameraSpeed, boolean smoothStop) {
        super(area, tpCamera, doEdgeControl);
        cameraCatchupSpeed = cameraSpeed;
        this.smoothStop = smoothStop;
    }

    @Override
    protected Vector getShakeModifier() {
        return getCameraPosXY();
    }

    @Override
    protected float updateCamera(float playerPos, float cameraPos, float[] minMaxPos) {
        if (smoothStop) {
            if (playerPos < minMaxPos[MIN]) {
                playerPos = minMaxPos[MIN];
            }
            if (playerPos > minMaxPos[MAX]) {
                playerPos = minMaxPos[MAX];
            }
        }
        float modifier = computeModifier(playerPos, cameraPos, minMaxPos);

        if (checkBounds) {
            checkBounds = false;
            return getEdgePos(cameraPos, minMaxPos, modifier);
        }
        return cameraPos + modifier;
    }

    /**
     * Method to compute how much the camera moves, the bigger the difference, the faster it moves and same vice-versa.
     * @param playerPos the current player position
     * @param cameraPos the current camera position
     * @param minMaxPos the min and max position for the camera
     * @return modifier depending on the difference and CAMERA_CATCHUP_SPEED
     */
    protected float computeModifier(float playerPos, float cameraPos, float[] minMaxPos) {
        float modifier = 0.0f;
        // get the difference
        float difference = Math.abs(playerPos - cameraPos);
        // adjust the coordinate
        if (cameraPos != playerPos && difference > SPOT_PRECISION) {
            if (playerPos < cameraPos) {
                modifier = -(cameraCatchupSpeed * difference);
            } else {
                modifier = cameraCatchupSpeed * difference;
            }
        }

        if (modifier != 0.0f && isOutOfInterval(modifier + cameraPos, minMaxPos)) {
            checkBounds = true;
            modifier = 0.0f;
        }
        return modifier;
    }

    /**
     * Method to get min or max position if camera is very close to them.
     * @param cameraPos the current camera position
     * @param minMaxPos the min and max position for the camera
     * @param modifier  the camera movement modifier
     * @return position of min or max, or if not triggered, normal camera with modifier
     */
    protected float getEdgePos(float cameraPos, float[] minMaxPos, float modifier) {
        // Set camera to min max positions if very close to edge
        for (float pos : minMaxPos) {
            if (Math.abs(pos - (modifier + cameraPos)) < EDGE_CONTROL_TRIGGER) {
                return pos;
            }
        }
        return cameraPos + modifier;
    }

}