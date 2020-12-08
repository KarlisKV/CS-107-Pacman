/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        02:15
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Pellet;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SuperPacmanPlayerStatusGUI implements Graphics {
    private static final float DEPTH = 5000.0f;
    private static final float HP_SPACING = 1.25f;
    private static final float TOP_EDGE_PADDING = 9.0f;
    private static final float BOTTOM_EDGE_PADDING = TOP_EDGE_PADDING;
    private static final float LEFT_EDGE_PADDING = 6.2f;
    private static final float RIGHT_EDGE_PADDING = -9.5f;
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
    private int comboCount = 0;
    private float areaTimer = 0;
    private List<Float> areaTimerHistory = new ArrayList<>();

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
    protected void update(int currentHp, int score, int comboCount, float areaTimer, Collection<Float> historyTimer) {
        this.currentHp = currentHp;
        this.score = score;
        this.comboCount = comboCount;
        this.areaTimer = areaTimer;
        this.areaTimerHistory = new ArrayList<>(historyTimer);
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        if (SuperPacman.currentCameraScaleFactor < 55) {
            // Lives
            for (int i = 0; i < maxHp; ++i) {
                int x = i < currentHp ? LIFE : NO_LIFE;

                float xPos = LEFT_EDGE_PADDING + 4 + (HP_SPACING * i) -
                        (((LIFE_SIZE * maxHp) / 2.f) + (HP_SPACING * ((maxHp / 2.f) - 2)));
                float yPos = (BOTTOM_EDGE_PADDING) - 0.75f;

                ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplaySmall"),
                                                       LIFE_SIZE, LIFE_SIZE,
                                                       new RegionOfInterest(x, 0, LIFE_SPRITE_SIZE, LIFE_SPRITE_SIZE),
                                                       anchor.add(new Vector(xPos, yPos)), 1, DEPTH);
                life.draw(canvas);
            }

            // High Score text
            String text = "High Score";
            TextGraphics highScore =
                    new TextGraphics(text, FONT_SIZE, Color.WHITE, Color.BLACK, 0.0f, false, false,
                                     anchor.add(new Vector(width / 2 - (text.length() * FONT_SIZE / 2),
                                                           height - TOP_EDGE_PADDING)));
            highScore.setFontName(FONT);
            highScore.setDepth(DEPTH);
            highScore.draw(canvas);

            // Score
            String textToDisplay = String.valueOf(score);
            if (comboCount != 0) {
                textToDisplay += " x" + (comboCount + 1);
            }
            TextGraphics scoreText =
                    new TextGraphics(textToDisplay, FONT_SIZE - 0.1f, Color.WHITE, Color.BLACK, 0.0f, false, false,
                                     anchor.add(new Vector(width / 2 - (textToDisplay.length() * FONT_SIZE / 2),
                                                           height - TOP_EDGE_PADDING - TEXT_PADDING + 0.25f)));
            scoreText.setFontName(FONT);
            scoreText.setDepth(DEPTH);
            scoreText.draw(canvas);

            // Pellets text
            TextGraphics pelletText =
                    new TextGraphics("Pellets",
                                     FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, anchor.add(
                            new Vector(LEFT_EDGE_PADDING, height - TOP_EDGE_PADDING + 0.2f)));
            pelletText.setFontName(FONT);
            pelletText.setDepth(DEPTH);
            pelletText.draw(canvas);

            // Eaten pellets
            TextGraphics eatenPellets =
                    new TextGraphics(Pellet.getNbrOfPelletsEaten() + "/" + Pellet.getTotalPellets(),
                                     FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, anchor.add(
                            new Vector(LEFT_EDGE_PADDING, height - TOP_EDGE_PADDING + 0.2f - TEXT_PADDING + 0.25f)));
            eatenPellets.setFontName(FONT);
            eatenPellets.setDepth(DEPTH);
            eatenPellets.draw(canvas);

            // Timer text
            TextGraphics timerIndicationText =
                    new TextGraphics("Time", FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, anchor.add(
                            new Vector(width + RIGHT_EDGE_PADDING - (4 * 0.6f) + 3,
                                       height - TOP_EDGE_PADDING + 0.2f)));
            timerIndicationText.setFontName(FONT);
            timerIndicationText.setDepth(DEPTH);
            timerIndicationText.draw(canvas);

            // Timer
            String timerText = String.format("%.3f", areaTimer);
            TextGraphics timer =
                    new TextGraphics(timerText,
                                     FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, anchor.add(
                            new Vector(width + RIGHT_EDGE_PADDING - (timerText.length() * 0.6f) + 3,
                                       height - TOP_EDGE_PADDING + 0.2f - TEXT_PADDING + 0.25f)));
            timer.setFontName(FONT);
            timer.setDepth(DEPTH);
            timer.draw(canvas);

            // History Timer
            for (int i = 0; i < areaTimerHistory.size(); ++i) {
                String historyTimerText = String.format("%.3f", areaTimerHistory.get(i));
                TextGraphics historyTimer =
                        new TextGraphics(historyTimerText,
                                         FONT_SIZE - 0.5f, Color.WHITE, Color.BLACK, 0.0f, false, false, anchor.add(
                                new Vector(width + RIGHT_EDGE_PADDING - (historyTimerText.length() * 0.5f) + 3,
                                           height - TOP_EDGE_PADDING - 0.6f - (i * 0.6f) - TEXT_PADDING + 0.25f)));
                historyTimer.setFontName(FONT);
                historyTimer.setDepth(DEPTH);
                historyTimer.draw(canvas);
            }


        }
    }
}
