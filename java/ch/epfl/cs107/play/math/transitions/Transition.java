/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:40
 */


package ch.epfl.cs107.play.math.transitions;

public abstract class Transition {
    private final float velocity;
    private float currentProgress;

    /**
     * Constructor for Transition class
     * @param velocity (float): the speed of the transition
     */
    public Transition(float velocity) {
        this.velocity = velocity;
    }

    /**
     * Getter for currentProgress
     * @return currentProgress
     */
    protected float getCurrentProgress() {
        return currentProgress;
    }

    /**
     * Setter for currentProgress
     */
    public void setCurrentProgress(float progress) {
        currentProgress = progress;
    }

    /**
     * Abstract method to update the progress (0 -> 1)
     * @return new progress
     */
    public abstract float getProgress();

    /**
     * Method to update (0 -> 1)
     */
    protected void update() {
        if (currentProgress < 1.0f) {
            this.currentProgress += velocity;
        }
    }

    /**
     * Abstract method to update the progress (1 -> 0)
     * @return new progress
     */
    public abstract float getInverseProgress();

    /**
     * Method to update (1 -> 0)
     */
    protected void inverseUpdate() {
        if (currentProgress > 0) {
            this.currentProgress -= velocity;
        }
    }

    /**
     * Method to reset currentProgress to 0
     */
    public void reset() {
        currentProgress = 0;
    }

    /**
     * Getter for transition completion state
     * @return (true) if the transition is competed
     */
    public boolean isFinished() {
        return currentProgress >= 0.999f;
    }
}
