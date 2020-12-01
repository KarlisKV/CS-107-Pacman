/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:40
 */


package ch.epfl.cs107.play.math.transitions;

public abstract class Transition {
    private final float velocity;
    private float currentProgress;

    public Transition(float velocity) {
        this.velocity = velocity;
    }

    protected float getCurrentProgress() {
        return currentProgress;
    }

    public float getProgress() {
        update();
        return currentProgress;
    }

    private void update() {
        if (currentProgress < 1.0f) {
            this.currentProgress += velocity;
        }
    }

    public void reset() {
        currentProgress = 0;
    }

    public boolean isFinished() {
        return currentProgress >= 0.999f;
    }
}
