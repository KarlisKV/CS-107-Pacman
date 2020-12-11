/*
 *	Author:      Leonard Cseres
 *	Date:        05.12.20
 *	Time:        20:43
 */


package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.Play;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanStatusGUI implements Graphics {
    private static final float DEPTH = 15000;
    private static final float TEXT_PADDING = 1.5f;
    private static final String FONT = "emulogic";
    private static final float FONT_SIZE = 1.5f;
    private final TextGraphics fpsCounter;
    private final TextGraphics debugMode;

    public SuperPacmanStatusGUI() {
        fpsCounter = new TextGraphics("", 0, Color.WHITE, Color.WHITE, 0.0f, false, false, null);
        fpsCounter.setFontName(FONT);
        fpsCounter.setDepth(DEPTH);

        debugMode = new TextGraphics("", 0, Color.ORANGE, Color.ORANGE, 0.0f, false, false, null);
        debugMode.setFontName(FONT);
        debugMode.setDepth(DEPTH);
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        float fontSize = height / (FONT_SIZE * 35);

        // FPS counter
        if (MenuStateManager.isShowFps()) {
            fpsCounter.setText("Fps: " + Play.getCurrentFps());
            fpsCounter.setFontSize(fontSize);
            fpsCounter.setAnchor(anchor.add(new Vector(height / (TEXT_PADDING * 35), height / (TEXT_PADDING * 35))));
            fpsCounter.draw(canvas);
        }

        // DEBUG MODE TEXT
        if (MenuStateManager.isDebugMode()) {
            debugMode.setText("Debug Mode");
            debugMode.setFontSize(fontSize);
            debugMode.setAnchor(anchor.add(
                    new Vector(width - (2 * (height / (TEXT_PADDING * 7))) - (height / (TEXT_PADDING * 35)),
                               height / (TEXT_PADDING * 35))));
            debugMode.draw(canvas);
        }
    }
}
