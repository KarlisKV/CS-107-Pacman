/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:46
 */


package ch.epfl.cs107.play.math.transitions;

public class EaseInOutCubic extends Transition {

    /**
     * Constructor for EaseInOutCubic class
     * @param velocity (float): the speed of the transition
     */
    public EaseInOutCubic(float velocity) {
        super(velocity);
    }

    @Override
    public float getProgress() {
        update();
        return getCurrentProgress() < 0.5 ? (float) (4 * Math.pow(getCurrentProgress(), 3)) :
               (float) (1 - Math.pow(-2 * getCurrentProgress() + 2, 3) / 2);
    }

    @Override
    public float getInverseProgress() {
        update();
        return 1 - getProgress();
    }
}
