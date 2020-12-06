/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        11:04
 */


package ch.epfl.cs107.play.game.superpacman.graphics;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Glow implements Graphics {
    private final Positionable parent;
    private final ImageGraphics sprite;
    private final GlowColors color;
    private final float size;
    private final float alpha;
    private float currentAlpha;

    public Glow(Positionable parent, ImageGraphics sprite, GlowColors color, float size, float alpha) {
        this.parent = parent;
        this.sprite = sprite;
        this.color = color;
        this.size = size;
        this.alpha = alpha;
        currentAlpha = alpha;
    }

    public void fadeOut(float speed) {
        if (currentAlpha > 0) {
            currentAlpha -= currentAlpha * speed;
        }
        currentAlpha = currentAlpha < 0 ? 0 : currentAlpha;
    }

    public void reset() {
        currentAlpha = alpha;
    }

    @Override
    public void draw(Canvas canvas) {

        ImageGraphics glow = new ImageGraphics(ResourcePath.getSprite(color.pathToColor),
                                               size,
                                               size,
                                               new RegionOfInterest(0, 0, 195, 195),
                                               new Vector(-size / 2 + sprite.getWidth() / 2,
                                                          -size / 2 + sprite.getHeight() / 2),
                                               currentAlpha, -5000);
        glow.setParent(parent);
        glow.draw(canvas);
    }

    public enum GlowColors {
        TEST("superpacman/glowTest"),
        YELLOW("superpacman/glowYellow"),
        RED("superpacman/glowRed"),
        BLUE("superpacman/glowBlue"),
        LIGHT_BLUE("superpacman/glowLightBlue"),
        PINK("superpacman/glowPink"),
        ORANGE("superpacman/glowOrange"),
        LIGHT_PINK("superpacman/glowLightPink"),
        WHITE("superpacman/glowWhite");

        final String pathToColor;

        GlowColors(String pathToColor) {
            this.pathToColor = pathToColor;
        }
    }
}