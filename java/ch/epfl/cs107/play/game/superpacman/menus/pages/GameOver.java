/*
 *	Author:      Leonard Cseres
 *	Date:        05.12.20
 *	Time:        17:07
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

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

    /**
     * Constructor for GameOver class
     * @param window (Window): the current window
     */
    public GameOver(Window window) {
        super(window);
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
        updateImage(GAME_OVER_TITLE_PATH).draw(canvas);

        // Game score
        GameScore gameScore = SuperPacman.getLeaderboardScores().getLastGame();

        int paddingCount = 0;
        updateText("High Score: " + gameScore.getScore(), BODY_FONT_SIZE + 1.2f, 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);
        ++paddingCount;
        updateText("Deaths: " + gameScore.getDeaths() + "/" + gameScore.getMaxDeaths(), BODY_FONT_SIZE, 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);
        ++paddingCount;
        updateText("Total Time: " + String.format("%.3f", gameScore.getTotalTime()), BODY_FONT_SIZE, 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);
        ++paddingCount;
        updateText("Time by level: ", BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);
        ++paddingCount;

        for (Map.Entry<String, Float> areaTimerEntry : gameScore.getOrderedAreaHistoryTimes().entrySet()) {
            updateText(" - " + areaTimerEntry.getKey().substring(12) + ": " +
                               String.format("%.3f", areaTimerEntry.getValue()), BODY_FONT_SIZE, 0,
                       Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);
            ++paddingCount;
        }
        ++paddingCount;
        gameScore.setPlayerName(getUserTextInput(Option.NAME, gameScore.getPlayerName()));
        updateText(getUserInputOptionText(Option.NAME, gameScore.getPlayerName()), BODY_FONT_SIZE, 0,
                   Y_TEXT_OFFSET + TEXT_PADDING * paddingCount).draw(canvas);

        // Restart option
        paddingCount += 2;
        updateText(getOptionText(Option.RESTART), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount)
                .draw(canvas);


        // Back to main menu option
        updateText(getOptionText(Option.BACK_TO_MAIN_MENU), BODY_FONT_SIZE, 0, -50).draw(canvas);
    }
}
