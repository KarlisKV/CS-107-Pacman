/*
 *	Author:      Leonard Cseres
 *	Date:        17.12.20
 *	Time:        01:01
 */


package ch.epfl.cs107.play.math.transitions;

public class EaseOutCirc extends Transition {
    /**
     * Constructor for EaseOutCirc class
     * @param velocity (float): the speed of the transition
     */
    public EaseOutCirc(float velocity) {
        super(velocity);
    }

    @Override
    public float getProgress() {
        update();
        return (float) Math.sqrt(1 - Math.pow(getCurrentProgress() - 1, 2));
    }

    @Override
    public float getInverseProgress() {
        inverseUpdate();
        return (float) Math.sqrt(1 - Math.pow(getCurrentProgress() - 1, 2));
    }

}
