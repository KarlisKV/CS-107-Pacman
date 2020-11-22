/*
 *	Author:      Leonard Cseres
 *	Date:        21.11.20
 *	Time:        20:22
 */


package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.math.Vector;

public class ComputeCamera {
    private static final float CAMERA_CATCHUP_SPEED = 0.07f;
    private static final float SPOT_PRECISION = 0.001f;
    private static final float PLAYER_TP_TRIGGER = 1.0f;
    private static final float EDGE_CONTROL_TRIGGER = 0.1f;

    private static final int MIN = 0;
    private static final int MAX = 1;

    private final float cameraScaleFactor;
    private final boolean smoothFollow;
    private final boolean doEdgeControl;
    private Vector playerPosXY;
    private Vector tempPlayerPosXY;
    private Vector cameraPosXY;
    private float[] minMaxPosX = {Float.MIN_VALUE, Float.MAX_VALUE};
    private float[] minMaxPosY = {Float.MIN_VALUE, Float.MAX_VALUE};

    public ComputeCamera(float cameraScaleFactor, boolean smoothFollow, boolean doEdgeControl) {
        this.cameraScaleFactor = cameraScaleFactor;
        this.smoothFollow = smoothFollow;
        this.doEdgeControl = doEdgeControl;
        playerPosXY = new Vector(0.0f, 0.0f);
    }

    public void setPos(Vector playerPos, Vector cameraPos, float areaWidth, float areaHeight) {
        tempPlayerPosXY = playerPosXY;
        playerPosXY = playerPos;
        cameraPosXY = cameraPos;

        if (doEdgeControl) {
            minMaxPosX = setMinMaxPos(areaWidth);
            minMaxPosY = setMinMaxPos(areaHeight);
        }
    }

    public Vector getPos() {
        if (isPlayerTeleported() || isCameraInInitPos()) {
            float initX = setInitialPos(playerPosXY.getX(), minMaxPosX);
            float initY = setInitialPos(playerPosXY.getY(), minMaxPosY);

            return new Vector(initX, initY);

        } else {
            float newX = updatePos(playerPosXY.getX(), cameraPosXY.getX(), minMaxPosX);
            float newY = updatePos(playerPosXY.getY(), cameraPosXY.getY(), minMaxPosY);

            return new Vector(newX, newY);
        }
    }

    private boolean isPlayerTeleported() {
        return (Math.abs(tempPlayerPosXY.getX() - playerPosXY.getX()) > PLAYER_TP_TRIGGER && Math.abs(
                tempPlayerPosXY.getY() - playerPosXY.getY()) > PLAYER_TP_TRIGGER);
    }

    private boolean isCameraInInitPos() {
        return cameraPosXY.equals(new Vector(0.0f, 0.0f));
    }

    private float[] setMinMaxPos(float areaSize) {

        float[] minMaxCameraPos = new float[2];
        minMaxCameraPos[MIN] = (cameraScaleFactor / 2);
        minMaxCameraPos[MAX] = (areaSize - (cameraScaleFactor / 2));

        return minMaxCameraPos;
    }

    private float setInitialPos(float playerPos, float[] minMaxPos) {

        float validPos = playerPos;
        while (!isInInterval(validPos, minMaxPos)) {
            if (validPos < minMaxPos[MIN]) {
                validPos += 0.5f;
            } else {
                validPos -= 0.5f;
            }
        }
        return validPos;
    }

    private float updatePos(float playerPos, float cameraPos, float[] minMaxPos) {

        if (smoothFollow) {
            float modifier = smoothModifyPos(playerPos, cameraPos);
            if (modifier != 0.0f) {
                if (!isInInterval(modifier + cameraPos, minMaxPos)) {

                    // return
                    for (float pos : minMaxPos) {
                        if (Math.abs(pos - (modifier + cameraPos)) < EDGE_CONTROL_TRIGGER) {
                            return pos;
                        }
                    }
                    modifier = 0.0f;
                }
            }
            return cameraPos + modifier;

        } else {
            float pos = playerPos;
            if (!isInInterval(pos, minMaxPos)) {
                pos = cameraPos;
            }
            return pos;
        }
    }

    private float smoothModifyPos(float playerPos, float cameraPos) {

        float modifiedPos = 0.0f;
        // get the difference
        float difference = Math.abs(playerPos - cameraPos);
        // adjust the coordinates
        if (cameraPos != playerPos && difference > SPOT_PRECISION) {
            if (playerPos < cameraPos) {
                modifiedPos = -CAMERA_CATCHUP_SPEED * difference;
            } else {
                modifiedPos = CAMERA_CATCHUP_SPEED * difference;
            }
        }
        return modifiedPos;
    }


    private boolean isInInterval(float toCompare, float[] minMaxValues) {
        return toCompare >= minMaxValues[MIN] && toCompare <= minMaxValues[MAX];
    }
}
