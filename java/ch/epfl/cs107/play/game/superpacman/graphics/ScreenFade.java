/*
 *	Author:      Leonard Cseres
 *	Date:        07.12.20
 *	Time:        12:56
 */


package ch.epfl.cs107.play.game.superpacman.graphics;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.transitions.EaseInOutCubic;
import ch.epfl.cs107.play.math.transitions.Transition;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class ScreenFade implements Graphics {
    private final float overlayDepth;
    private final Transition transitionOverlay;
    private boolean fadeIn = false;
    private boolean fadeOut = false;

    /**
     * Constructor for ScreenFade class
     * @param depth    (float): the desired depth of the black overlay
     * @param velocity (float): the fade velocity of the black overlay
     */
    public ScreenFade(float depth, float velocity) {
        overlayDepth = depth;
        transitionOverlay = new EaseInOutCubic(velocity);
    }

    public void setFadeIn() {
        fadeIn = true;
    }

    public void setFadeOut() {
        fadeOut = true;
    }

    @Override
    public void draw(Canvas canvas) {
        float alpha = 0;
        if (fadeOut && !fadeIn) {
            // Show black overlay (alpha 0 -> 1)
            alpha = transitionOverlay.getProgress();
            fadeOut = false;
        } else {
            // Hide black overlay (alpha 1 -> 0)
            alpha = transitionOverlay.getInverseProgress();
        }

        if (alpha > 0) {
            // DRAW BLACK OVERLAY
            ShapeGraphics overlay =
                    new ShapeGraphics(
                            new Circle(300, new Vector(canvas.getScaledWidth(), canvas.getScaledHeight())),
                            Color.BLACK, Color.BLACK, 0.0f,
                            alpha, overlayDepth);
            overlay.draw(canvas);
        }
    }

}
