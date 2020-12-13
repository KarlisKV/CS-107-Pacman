/*
 *	Author:      Leonard Cseres
 *	Date:        05.12.20
 *	Time:        17:07
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.leaderboard.GameScore;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import java.util.Map;

public class GameOver extends Menu {
    private static final float Y_TEXT_OFFSET = 14;
    private static final String GAME_OVER_TITLE_PATH = "superpacman/gameOverTitle";
    private static final float TEXT_PADDING = -4.5f;
    private final ImageGraphics titleImage;
    private final TextGraphics highScore;
    private final TextGraphics deaths;
    private final TextGraphics totTime;
    private final TextGraphics timerPerLevel;
    private final TextGraphics enterName;
    private final TextGraphics restart;
    private final TextGraphics backToMainMenu;

    /**
     * Constructor for GameOver class
     * @param window (Window): the current window
     */
    public GameOver(Window window) {
        super(window);
        titleImage = createImage(GAME_OVER_TITLE_PATH);
        highScore = createText(BODY_FONT_SIZE + 1.2f);
        deaths = createText(BODY_FONT_SIZE);
        totTime = createText(BODY_FONT_SIZE);
        timerPerLevel = createText(BODY_FONT_SIZE);
        enterName = createText(BODY_FONT_SIZE);
        restart = createText(BODY_FONT_SIZE);
        backToMainMenu = createText(BODY_FONT_SIZE);
    }

    @Override
    protected Option getDefaultSelection() {
        return Option.NAME;
    }

    @Override
    protected void setupOptionList() {
        getOptionList().add(Option.NAME);
        getOptionList().add(Option.RESTART);
        getOptionList().add(Option.BACK_TO_MAIN_MENU);
    }

    @Override
    protected void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // Game over image
        updateImage(titleImage);
        titleImage.draw(canvas);

        // Game score
        GameScore gameScore = SuperPacman.getLeaderboardScores().getLastGame();

        int paddingCount = 0;
        updateText(highScore, "High Score: " + gameScore.getScore(), 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        highScore.draw(canvas);
        ++paddingCount;

        updateText(deaths, "Deaths: " + gameScore.getDeaths() + "/" + gameScore.getMaxDeaths(), 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        deaths.draw(canvas);
        ++paddingCount;

        updateText(totTime, "Total Time: " + String.format("%.3f", gameScore.getTotalTime()), 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        totTime.draw(canvas);
        ++paddingCount;

        updateText(timerPerLevel, "Time by level: ", 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        timerPerLevel.draw(canvas);
        ++paddingCount;

        for (Map.Entry<String, Float> areaTimerEntry : gameScore.getOrderedAreaHistoryTimes().entrySet()) {
            getNewUpdatedText(" - " + areaTimerEntry.getKey().substring(12) + ": " +
                                      String.format("%.3f", areaTimerEntry.getValue()), BODY_FONT_SIZE, 0,
                              Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);
            ++paddingCount;
        }
        ++paddingCount;

        // Player input name
        gameScore.setPlayerName(getUserTextInput(Option.NAME, gameScore.getPlayerName()));
        updateText(enterName, getUserInputOptionText(Option.NAME, gameScore.getPlayerName()), 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        enterName.draw(canvas);


        // Restart option
        updateText(restart, getOptionText(Option.RESTART), 0, getBottomPadding() - TEXT_PADDING);
        restart.draw(canvas);

        // Back to main menu option
        updateText(backToMainMenu, getOptionText(Option.BACK_TO_MAIN_MENU), 0, getBottomPadding());
        backToMainMenu.draw(canvas);
    }
}
