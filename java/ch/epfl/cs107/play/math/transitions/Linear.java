/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        10:55
 */


package ch.epfl.cs107.play.math.transitions;

public class Linear extends Transition {

    /**
     * Constructor for Linear class
     * @param velocity (float): the speed of the transition
     */
    public Linear(float velocity) {
        super(velocity);
    }

    @Override
    public float getProgress() {
        update();
        return getCurrentProgress();
    }

    @Override
    public float getInverseProgress() {
        inverseUpdate();
        return getProgress();
    }
}
