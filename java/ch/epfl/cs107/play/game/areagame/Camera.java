/*
 *	Author:      Leonard Cseres
 *	Date:        21.11.20
 *	Time:        20:22
 */


package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.math.Vector;

import java.util.Random;

public class Camera {
    // Speed of smooth camera
    private static final float CAMERA_CATCHUP_SPEED = 0.07f;
    // How precise will the camera position itself if the player is no moving
    private static final float SPOT_PRECISION = 0.001f;
    // Trigger for player difference position
    private static final float PLAYER_TP_TRIGGER = 0.5f;
    // Trigger for edge detection
    private static final float EDGE_CONTROL_TRIGGER = 0.1f;
    private static final int MIN = 0;
    private static final int MAX = 1;
    private final Random rand = new Random();
    private final float cameraScaleFactor;
    private final boolean smoothFollow;
    private final boolean doEdgeControl;
    private int shakeDuration = 20;    // nbr of frames
    private float shakeIntensity = 0.2f;
    private int shakenFrames;
    private boolean shakeCamera = false;
    private Vector playerPosXY;
    private Vector tempPlayerPosXY;
    private Vector cameraPosXY;
    private float[] minMaxPosX = {Float.MIN_VALUE, Float.MAX_VALUE};
    private float[] minMaxPosY = {Float.MIN_VALUE, Float.MAX_VALUE};

    /**
     * Constructor for class.
     *
     * @param cameraScaleFactor get the camera scale factor
     * @param smoothFollow      if true, the camera with follow the player smoothly
     * @param doEdgeControl     if true, the camera will stop moving in the axis where there is the area edge
     */
    public Camera(float cameraScaleFactor, boolean smoothFollow, boolean doEdgeControl) {
        this.cameraScaleFactor = cameraScaleFactor;
        this.smoothFollow = smoothFollow;
        this.doEdgeControl = doEdgeControl;
        playerPosXY = new Vector(0.0f, 0.0f);
    }

    /**
     * Method to update the instantiated class with the current attributes.
     *
     * @param playerPos  the current player position
     * @param cameraPos  the current camera position
     * @param areaWidth  the area width
     * @param areaHeight the area height
     */
    public void updatePos(Vector playerPos, Vector cameraPos, float areaWidth, float areaHeight) {
        tempPlayerPosXY = playerPosXY;
        playerPosXY = playerPos;
        cameraPosXY = cameraPos;

        if (doEdgeControl) {
            minMaxPosX = setMinMaxPos(areaWidth);
            minMaxPosY = setMinMaxPos(areaHeight);
        }
    }

    /**
     * Method tho compute the new camera position.
     *
     * @return the new camera position
     */
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

    /**
     * Method to start camera shake with default values.
     */
    public void shake() {
        shakeCamera = true;
        shakenFrames = 0;
    }

    /**
     * Method to start camera shake.
     *
     * @param intensity the intensity of the shake
     * @param duration  the duration of the shake in frames
     */
    public void shake(float intensity, int duration) {
        shake();
        shakeIntensity = intensity;
        shakeDuration = duration;
    }

    /**
     * Method to detect if player has changed area.
     *
     * @return true if the difference of the player's last update coordinates and current coordinates is larger than
     * the tp trigger
     */
    private boolean isPlayerTeleported() {
        return (Math.abs(tempPlayerPosXY.getX() - playerPosXY.getX()) > PLAYER_TP_TRIGGER && Math.abs(
                tempPlayerPosXY.getY() - playerPosXY.getY()) > PLAYER_TP_TRIGGER);
    }

    /**
     * Method to check if camera is in initial position.
     *
     * @return true if camera is at (0, 0)
     */
    private boolean isCameraInInitPos() {
        return cameraPosXY.equals(new Vector(0.0f, 0.0f));
    }

    /**
     * Method to set minX, maxX, minY, maxY for the camera if doEdgeControl.
     *
     * @param areaSize the area size (width or height)
     * @return array with axis min and max positions
     */
    private float[] setMinMaxPos(float areaSize) {

        float[] minMaxCameraPos = new float[2];
        minMaxCameraPos[MIN] = (cameraScaleFactor / 2);
        minMaxCameraPos[MAX] = (areaSize - (cameraScaleFactor / 2));

        return minMaxCameraPos;
    }

    /**
     * Method to find initial position for the camera if the camera's initial position is invalid (with doEdgeControl).
     *
     * @param playerPos the player position
     * @param minMaxPos the min and max camera positions
     * @return the new valid position for the camera
     */
    private float setInitialPos(float playerPos, float[] minMaxPos) {

        float validPos = playerPos;
        while (isOutOfInterval(validPos, minMaxPos)) {
            if (validPos < minMaxPos[MIN]) {
                validPos += 0.5f;
            } else {
                validPos -= 0.5f;
            }
        }
        return validPos;
    }

    /**
     * Method to update camera current coordinate to new coordinate.
     *
     * @param playerPos the current player position
     * @param cameraPos the current camera position
     * @param minMaxPos the min and max position for the camera
     * @return new coordinate
     */
    private float updatePos(float playerPos, float cameraPos, float[] minMaxPos) {

        if (smoothFollow) {
            float modifier = computeModifier(playerPos, cameraPos);

            if (shakeCamera && shakenFrames < shakeDuration) {
                modifier += getRandomShakeModifier();
                ++shakenFrames;
            } else if (shakenFrames == shakeDuration) {
                shakeCamera = false;
            }

            if (modifier != 0.0f && isOutOfInterval(modifier + cameraPos, minMaxPos)) {
                // Set camera to min max positions if very close to edge
                for (float pos : minMaxPos) {
                    if (Math.abs(pos - (modifier + cameraPos)) < EDGE_CONTROL_TRIGGER) {
                        return pos;
                    }
                }
                // Make the camera not move
                modifier = 0.0f;
            }
            return cameraPos + modifier;

        } else {
            float pos = playerPos;
            if (isOutOfInterval(pos, minMaxPos)) {
                pos = cameraPos;
            }
            return pos;
        }
    }

    /**
     * Method to compute how much the camera moves, the bigger the difference, the faster it moves and same vice-versa.
     *
     * @param playerPos the current player position
     * @param cameraPos the current camera position
     * @return modifier depending on the difference and CAMERA_CATCHUP_SPEED
     */
    private float computeModifier(float playerPos, float cameraPos) {

        float modifiedPos = 0.0f;
        // get the difference
        float difference = Math.abs(playerPos - cameraPos);
        // adjust the coordinate
        if (cameraPos != playerPos && difference > SPOT_PRECISION) {
            if (playerPos < cameraPos) {
                modifiedPos = -(CAMERA_CATCHUP_SPEED * difference);
            } else {
                modifiedPos = CAMERA_CATCHUP_SPEED * difference;
            }
        }
        return modifiedPos;
    }


    /**
     * @param toCompare    the value on the interval
     * @param minMaxValues the min and max values
     * @return if toCompare is out of bounds
     */
    private boolean isOutOfInterval(float toCompare, float[] minMaxValues) {
        return (toCompare < minMaxValues[MIN]) || (toCompare > minMaxValues[MAX]);
    }

    private float getRandomShakeModifier() {

        final float min = -shakeIntensity;
        final float max = shakeIntensity;

        return min + rand.nextFloat() * (max - min);
    }
}
