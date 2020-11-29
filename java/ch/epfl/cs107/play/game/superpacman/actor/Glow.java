/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        11:04
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Glow implements Graphics {
    private final Positionable parent;
    private final GlowColors color;

    protected Glow(Positionable parent, GlowColors color) {
        this.parent = parent;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        ImageGraphics glow = new ImageGraphics(ResourcePath.getSprite(color.pathToColor),
                                               4.f,
                                               4.f,
                                               new RegionOfInterest(0, 0, 195, 195),
                                               new Vector(-1.5f, -1.5f),
                                               0.5f,
                                               100.f);
        glow.setParent(parent);
        glow.draw(canvas);
    }

    protected enum GlowColors {
        YELLOW("superpacman/glowYellow");

        final String pathToColor;

        GlowColors(String pathToColor) {
            this.pathToColor = pathToColor;
        }
    }
}
