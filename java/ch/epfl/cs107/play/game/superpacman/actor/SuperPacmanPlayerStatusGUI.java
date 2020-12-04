/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        02:15
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.Play;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.menus.MenuItems;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerStatusGUI implements Graphics {
    private static final float DEPTH = 5000.0f;
    private static final float HP_SPACING = 1.25f;
    private static final float EDGE_PADDING = 9.0f;
    private static final float LIFE_SIZE = 1.0f;
    private static final float TEXT_PADDING = 1.5f;
    private static final int LIFE_SPRITE_SIZE = 14;
    private static final String FONT = "emulogic";
    private static final float FONT_SIZE = 1.0f;
    private static final int LIFE = 0;
    private static final int NO_LIFE = LIFE_SPRITE_SIZE;
    private final int maxHp;
    private int currentHp;
    private int score = 0;

    /**
     * Consructor for SuperPacmanPlayerStatusGUI
     * @param currentHp (int): the SuperPacmanPlayer's initial health
     * @param maxHp     (int): the SuperPacmanPlayer's max health
     */
    protected SuperPacmanPlayerStatusGUI(int currentHp, int maxHp) {
        this.currentHp = currentHp;
        this.maxHp = maxHp;
    }

    /**
     * Method to update GUI with new values
     * @param currentHp the SuperPacmanPlayer's current health
     * @param score     the SuperPacmanPlayer's current health
     */
    protected void update(int currentHp, int score) {
        this.currentHp = currentHp;
        this.score = score;
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        // TODO: Temporary fix, find better solution
        if (SuperPacman.currentCameraScaleFactor < 55) {
            for (int i = 0; i < maxHp; ++i) {
                int x = i < currentHp ? LIFE : NO_LIFE;

                float xPos = width / 2 + (HP_SPACING * i) -
                        (((LIFE_SIZE * maxHp) / 2.f) + (HP_SPACING * ((maxHp / 2.f) - 2)));
                float yPos = height - (EDGE_PADDING) - TEXT_PADDING;

                ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplaySmall"),
                                                       LIFE_SIZE, LIFE_SIZE,
                                                       new RegionOfInterest(x, 0, LIFE_SPRITE_SIZE, LIFE_SPRITE_SIZE),
                                                       anchor.add(new Vector(xPos, yPos)), 1, DEPTH);
                life.draw(canvas);
            }
            String textToDisplay = "Score: " + score;
            TextGraphics scoreText =
                    new TextGraphics(textToDisplay, FONT_SIZE, Color.YELLOW, Color.BLACK, 0.0f, false, false,
                                     anchor.add(new Vector(width / 2 - (textToDisplay.length() * FONT_SIZE / 2),
                                                           height - (EDGE_PADDING))));
            scoreText.setFontName(FONT);
            scoreText.setDepth(DEPTH);
            scoreText.draw(canvas);
        }

        if (MenuItems.isShowFps()) {
            String fps = "Fps: " + Play.getCurrentFps();
            TextGraphics fpsText =
                    new TextGraphics(fps, 1.5f, Color.WHITE, Color.WHITE, 0.0f, false, false,
                                     anchor.add(new Vector(TEXT_PADDING,
                                                           TEXT_PADDING)));
            fpsText.setFontName(FONT);
            fpsText.setDepth(DEPTH + 10000);
            fpsText.draw(canvas);
        }
    }
}
