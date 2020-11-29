/*
 *	Author:      Leonard Cseres
 *	Date:        24.11.20
 *	Time:        11:09
 */


package ch.epfl.cs107.play.game.superpacman.area.camera;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;

import java.util.Random;

public abstract class Camera implements Updatable {

    protected static final int MIN = 0;
    protected static final int MAX = 1;
    // Trigger for player difference position
    private static final float PLAYER_TP_TRIGGER = 5.0f;
    protected final Vector CENTER_COORDINATES;
    private final Random rand = new Random();
    private final Area area;
    private boolean tpCamera = true;
    private Vector playerPosXY;
    private int shakeDuration = 8;    // nbr of frames
    private float shakeIntensity = 1.5f;
    private int shakenFrames;
    private boolean shakeCamera = false;
    private Vector tempPlayerPosXY;
    private Vector cameraPosXY;  // viewCenter
    private float[] minMaxPosX = {Float.MIN_VALUE, Float.MAX_VALUE};
    private float[] minMaxPosY = {Float.MIN_VALUE, Float.MAX_VALUE};

    /**
     * Constructor for class.
     * @param area          current area
     * @param tpCamera      teleport camera to player if he teleports (changes area...)
     * @param doEdgeControl if true, the camera will stop moving in the axis where there is the area edge
     */
    public Camera(Area area, boolean tpCamera, boolean doEdgeControl) {
        this.area = area;
        this.tpCamera = tpCamera;
        playerPosXY = Vector.ZERO;
        tempPlayerPosXY = Vector.ZERO;
        CENTER_COORDINATES = new Vector(area.getWidth() / (float) 2, area.getHeight() / (float) 2);
        cameraPosXY = CENTER_COORDINATES;

        if (doEdgeControl) {
            minMaxPosX = setMinMaxPos(area.getWidth());
            minMaxPosY = setMinMaxPos(area.getHeight());
        }
    }

    public void setMinMaxPosX(float[] minMaxPosX) {
        this.minMaxPosX = minMaxPosX;
    }

    public void setMinMaxPosY(float[] minMaxPosY) {
        this.minMaxPosY = minMaxPosY;
    }

    protected Vector getPlayerPosXY() {
        return playerPosXY;
    }

    protected Vector getCameraPosXY() {
        return cameraPosXY;
    }

    public void setCameraPosXY(Vector cameraPosXY) {
        this.cameraPosXY = cameraPosXY;
    }

    /**
     * Method to update the instantiated class with the current player position.
     * @param playerPos the current player position
     */
    public void updatePos(Vector playerPos) {
        tempPlayerPosXY = playerPosXY;
        playerPosXY = playerPos;
    }

    @Override
    public void update(float deltaTime) {
        if (shakeCamera) {
            shakeCamera();
        }
        if (playerPosXY != null) {
            cameraPosXY = getPos();
        } else {
            // Set default view to center
            cameraPosXY = CENTER_COORDINATES;
        }
        // Compute new viewport
        Transform viewTransform = Transform.I.scaled(area.getCameraScaleFactor()).translated(cameraPosXY);
        area.getWindow().setRelativeTransform(viewTransform);
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
     * @param intensity the intensity of the shake
     * @param duration  the duration of the shake in frames
     */
    public void shake(float intensity, int duration) {
        shakeIntensity = intensity;
        shakeDuration = duration;
        shake();
    }

    /**
     * Method to perform camera shake.
     */
    private void shakeCamera() {
        if (shakeCamera && shakenFrames < shakeDuration) {
            float modifierX = getShakeModifier().getX() + getRandomShakeModifier();
            float modifierY = getShakeModifier().getY() + getRandomShakeModifier();
            playerPosXY = new Vector(modifierX, modifierY);
            ++shakenFrames;
        } else if (shakenFrames == shakeDuration) {
            shakeCamera = false;
        }
    }

    /**
     * Abstract method to get what Vector will be modified
     * @return the Vector to modify
     */
    protected abstract Vector getShakeModifier();

    private float getRandomShakeModifier() {
        final float min = -shakeIntensity;
        final float max = shakeIntensity;

        return min + rand.nextFloat() * (max - min);
    }

    /**
     * Method tho compute the new camera position.
     * @return the new camera position
     */
    private Vector getPos() {
        if (isPlayerTeleported() && tpCamera) {
            float initX = setInitialPos(playerPosXY.getX(), minMaxPosX);
            float initY = setInitialPos(playerPosXY.getY(), minMaxPosY);

            return new Vector(initX, initY);
        } else {
            float newX = updateCamera(playerPosXY.getX(), cameraPosXY.getX(), minMaxPosX);
            float newY = updateCamera(playerPosXY.getY(), cameraPosXY.getY(), minMaxPosY);

            return new Vector(newX, newY);
        }
    }

    /**
     * Method to detect if player has changed area.
     * @return true if the difference of the player's last update coordinates and current coordinates is larger than
     * the tp trigger
     */
    private boolean isPlayerTeleported() {
        return (Math.abs(tempPlayerPosXY.getX() - playerPosXY.getX()) > PLAYER_TP_TRIGGER && Math.abs(
                tempPlayerPosXY.getY() - playerPosXY.getY()) > PLAYER_TP_TRIGGER);
    }

    /**
     * Method to set minX, maxX, minY, maxY for the camera if doEdgeControl.
     * @param areaSize the area size (width or height)
     * @return array with axis min and max positions
     */
    private float[] setMinMaxPos(float areaSize) {

        float[] minMaxCameraPos = new float[2];
        minMaxCameraPos[MIN] = (area.getCameraScaleFactor() / 2);
        minMaxCameraPos[MAX] = (areaSize - (area.getCameraScaleFactor() / 2));

        return minMaxCameraPos;
    }

    /**
     * Method to find initial position for the camera if the camera's initial position is invalid (with doEdgeControl).
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
     * @param playerPos the current player position
     * @param cameraPos the current camera position
     * @param minMaxPos the min and max position for the camera
     * @return new coordinate
     */
    protected float updateCamera(float playerPos, float cameraPos, float[] minMaxPos) {
        return cameraPos;
    }

    /**
     * @param toCompare    the value on the interval
     * @param minMaxValues the min and max values
     * @return if toCompare is out of bounds
     */
    protected boolean isOutOfInterval(float toCompare, float[] minMaxValues) {
        return (toCompare < minMaxValues[MIN]) || (toCompare > minMaxValues[MAX]);
    }
}
