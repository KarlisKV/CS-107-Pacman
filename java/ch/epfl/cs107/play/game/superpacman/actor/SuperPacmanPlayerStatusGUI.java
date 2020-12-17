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
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDepth;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.transitions.EaseInOutCubic;
import ch.epfl.cs107.play.math.transitions.EaseOutCirc;
import ch.epfl.cs107.play.math.transitions.Transition;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SuperPacmanPlayerStatusGUI implements Graphics {
    private static final float HP_SPACING = 1.25f;
    private static final float TOP_EDGE_PADDING = 9.0f;
    private static final float BOTTOM_EDGE_PADDING = TOP_EDGE_PADDING;
    private static final float LEFT_EDGE_PADDING = 6.2f;
    private static final float RIGHT_EDGE_PADDING = -9.5f;
    private static final float LIFE_SIZE = 1.0f;
    private static final float TEXT_PADDING = 1.5f;
    private static final int LIFE_SPRITE_SIZE = 14;
    private static final String FONT = "emulogic";
    private static final String LIVES_PATHNAME = "superpacman/lifeDisplaySmall";
    private static final float FONT_SIZE = 1.0f;
    private static final int LIFE = 0;
    private static final int NO_LIFE = LIFE_SPRITE_SIZE;
    private final int playerMaxHp;
    private final TextGraphics highScoreTitle;
    private final TextGraphics score;
    private final TextGraphics pelletTitle;
    private final TextGraphics eatenPelletsCount;
    private final TextGraphics timerTitle;
    private final TextGraphics timer;
    private final TextGraphics difficultyMultiplier;
    private int playerCurrentHp;
    private int playerScore = 0;
    private int tmpPlayerScore = 0;
    private int playerComboCount = 0;
    private float areaTimer = 0;
    private float buffer = 0;
    private Transition points = new EaseOutCirc(0.015f);
    private Transition noPoints = new EaseInOutCubic(0.01f);
    private List<Float> areaTimerHistory = new ArrayList<>();

    /**
     * Constructor for SuperPacmanPlayerStatusGUI
     * @param playerCurrentHp (int): the SuperPacmanPlayer's initial health
     * @param playerMaxHp     (int): the SuperPacmanPlayer's max health
     */
    protected SuperPacmanPlayerStatusGUI(int playerCurrentHp, int playerMaxHp) {
        this.playerCurrentHp = playerCurrentHp;
        this.playerMaxHp = playerMaxHp;

        highScoreTitle = new TextGraphics("High Score", FONT_SIZE, Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(highScoreTitle);

        score = new TextGraphics("", FONT_SIZE - 0.1f, Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(score);

        pelletTitle = new TextGraphics("Pellets", FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(pelletTitle);

        eatenPelletsCount =
                new TextGraphics(Pellet.getNbrOfPelletsEaten() + "/" + Pellet.getTotalPellets(), FONT_SIZE - 0.4f,
                                 Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(eatenPelletsCount);

        timerTitle = new TextGraphics("Time", FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(timerTitle);

        timer = new TextGraphics("", FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(timer);

        difficultyMultiplier =
                new TextGraphics("", FONT_SIZE - 0.4f, Color.WHITE, Color.BLACK, 0.0f, false, false, null);
        setFontAndDepth(difficultyMultiplier);
    }

    private void setFontAndDepth(TextGraphics textGraphics) {
        textGraphics.setFontName(FONT);
        textGraphics.setDepth(SuperPacmanDepth.PAYER_GUI.value);
    }

    /**
     * Method to update GUI with new values
     * @param currentHp the SuperPacmanPlayer's current health
     * @param score     the SuperPacmanPlayer's current health
     */
    protected void update(int currentHp, int score, int comboCount, float areaTimer, Collection<Float> historyTimer) {
        this.playerCurrentHp = currentHp;
        this.playerScore = score;
        this.playerComboCount = comboCount;
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
            if (!MenuStateManager.isGodMode()) {
                for (int i = 0; i < playerMaxHp; ++i) {
                    int x = i < playerCurrentHp ? LIFE : NO_LIFE;

                    float xPos =
                            LEFT_EDGE_PADDING + 4 + (HP_SPACING * i) -
                                    (((LIFE_SIZE * playerMaxHp) / 2.f) + (HP_SPACING * ((
                                            playerMaxHp / 2.f) - 2)));
                    float yPos = (BOTTOM_EDGE_PADDING) - 0.75f;

                    ImageGraphics life = new ImageGraphics(ResourcePath.getSprite(LIVES_PATHNAME),
                                                           LIFE_SIZE, LIFE_SIZE,
                                                           new RegionOfInterest(x, 0, LIFE_SPRITE_SIZE,
                                                                                LIFE_SPRITE_SIZE),
                                                           anchor.add(new Vector(xPos, yPos)), 1,
                                                           SuperPacmanDepth.PAYER_GUI.value);
                    life.draw(canvas);
                }
            } else {
                // Infinite lives (god mode)
                float xPos = LEFT_EDGE_PADDING + 4 -
                        (((LIFE_SIZE * playerMaxHp) / 2.f) + (HP_SPACING * ((playerMaxHp / 2.f) - 2)));
                float yPos = (BOTTOM_EDGE_PADDING) - 0.75f;

                ImageGraphics life = new ImageGraphics(ResourcePath.getSprite(LIVES_PATHNAME),
                                                       LIFE_SIZE, LIFE_SIZE,
                                                       new RegionOfInterest(LIFE, 0, LIFE_SPRITE_SIZE,
                                                                            LIFE_SPRITE_SIZE),
                                                       anchor.add(new Vector(xPos, yPos)), 1,
                                                       SuperPacmanDepth.PAYER_GUI.value);
                life.draw(canvas);

                ImageGraphics infinity = new ImageGraphics(ResourcePath.getSprite("superpacman/infinitySymbol"),
                                                           LIFE_SIZE, LIFE_SIZE);
                infinity.setDepth(SuperPacmanDepth.PAYER_GUI.value);
                infinity.setAlpha(1);
                infinity.setAnchor(anchor.add(new Vector(xPos + (HP_SPACING), yPos)));

                infinity.draw(canvas);
            }

            // High Score text
            highScoreTitle.setAnchor(anchor.add(
                    new Vector(width / 2 - (highScoreTitle.getText().length() * FONT_SIZE / 2),
                               height - TOP_EDGE_PADDING)));
            highScoreTitle.draw(canvas);

            // Score
            String scoreText = String.valueOf(playerScore);
            if (playerComboCount != 0) {
                scoreText += " *" + (playerComboCount + 1);
            }
            if (tmpPlayerScore != playerScore) {
                buffer = points.getProgress();
                noPoints.setCurrentProgress(points.getProgress());
            } else {
                points.reset();
                buffer = noPoints.getInverseProgress();
            }
            score.setText(scoreText);
            score.setFontSize(FONT_SIZE - 0.1f + buffer);
            score.setAnchor(anchor.add(new Vector(width / 2 - (scoreText.length() * score.getFontSize() / 2),
                                                  height - TOP_EDGE_PADDING - TEXT_PADDING + 0.25f)));
            score.draw(canvas);
            tmpPlayerScore = playerScore;

            // Pellets text
            pelletTitle.setAnchor(anchor.add(new Vector(LEFT_EDGE_PADDING, height - TOP_EDGE_PADDING + 0.2f)));
            pelletTitle.draw(canvas);

            // Eaten pellets
            eatenPelletsCount.setText(Pellet.getNbrOfPelletsEaten() + "/" + Pellet.getTotalPellets());
            eatenPelletsCount.setAnchor(
                    anchor.add(new Vector(LEFT_EDGE_PADDING, height - TOP_EDGE_PADDING + 0.2f - TEXT_PADDING + 0.25f)));
            eatenPelletsCount.draw(canvas);

            // Timer text
            timerTitle.setAnchor(anchor.add(
                    new Vector(width + RIGHT_EDGE_PADDING - (4 * 0.6f) + 3, height - TOP_EDGE_PADDING + 0.2f)));
            timerTitle.draw(canvas);

            // Timer
            String timerText = String.format("%.3f", areaTimer);
            timer.setText(timerText);
            timer.setAnchor(anchor.add(new Vector(width + RIGHT_EDGE_PADDING - (timerText.length() * 0.6f) + 3,
                                                  height - TOP_EDGE_PADDING + 0.2f - TEXT_PADDING + 0.25f)));
            timer.draw(canvas);

            // History Timer
            for (int i = 0; i < areaTimerHistory.size(); ++i) {
                String historyTimerText = String.format("%.3f", areaTimerHistory.get(i));
                TextGraphics historyTimer =
                        new TextGraphics(historyTimerText,
                                         FONT_SIZE - 0.5f, Color.WHITE, Color.BLACK, 0.0f, false, false, anchor.add(
                                new Vector(width + RIGHT_EDGE_PADDING - (historyTimerText.length() * 0.5f) + 3,
                                           height - TOP_EDGE_PADDING - 0.6f - (i * 0.6f) - TEXT_PADDING + 0.25f)));
                setFontAndDepth(historyTimer);
                historyTimer.draw(canvas);
            }

            // Difficulty Multiplier
            String difficulty = SuperPacmanAreaBehavior.getInitDifficulty().name();
            double multiplier = SuperPacmanAreaBehavior.getInitDifficulty().multiplicationFactor;
            String difficultyText = String.format("%s *%s", difficulty, multiplier);
            difficultyMultiplier.setText(difficultyText);
            difficultyMultiplier.setAnchor(
                    anchor.add(width + RIGHT_EDGE_PADDING - (difficultyText.length() * (FONT_SIZE - 0.4f)) + 2.75f,
                               BOTTOM_EDGE_PADDING - 0.75f));
            difficultyMultiplier.draw(canvas);
        }
    }
}
