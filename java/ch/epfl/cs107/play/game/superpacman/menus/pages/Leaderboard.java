/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        11:20
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.leaderboard.GameScore;
import ch.epfl.cs107.play.game.superpacman.leaderboard.LeaderboardGameScores;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Leaderboard extends Menu {
    private static final float CENTER_X_OFFSET = -45;
    private static final float CENTER_Y_OFFSET = 28;
    private static final float TAB_COL_PADDING = 22.5f;
    private static final float TAB_LINE_PADDING = -4f;
    private final TextGraphics title;
    private final TextGraphics subTitle;
    private final TextGraphics back;

    /**
     * Constructor for Leaderboard class
     * @param window (Window): the current window
     */
    public Leaderboard(Window window) {
        super(window);
        title = createText(HEADER_FONT_SIZE);
        subTitle = createText(BODY_FONT_SIZE + 1);
        back = createText(BODY_FONT_SIZE);
    }

    @Override
    protected Option getDefaultSelection() {
        return Option.BACK;
    }

    @Override
    protected void setupOptionList() {
        getOptionList().add(Option.BACK);
    }

    @Override
    protected void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float yTextOffSet = (super.getScaledHeight() / 2) - 15;

        // Title
        updateText(title, Option.LEADERBOARD.text, 0, yTextOffSet);
        title.draw(canvas);

        // Subtitle
        updateText(subTitle, "Top 10 games", 0, yTextOffSet - 5.5f);
        subTitle.draw(canvas);

        // LeaderBoard table
        LeaderboardGameScores leaderboardGameScores = SuperPacman.getLeaderboardScores();
        String[][] leaderboardTable = new String[leaderboardGameScores.getSortedGameScores().size() + 1][5];
        leaderboardTable[0][0] = "Pos";
        leaderboardTable[0][1] = "Player";
        leaderboardTable[0][2] = "Score";
        leaderboardTable[0][3] = "Deaths";
        leaderboardTable[0][4] = "Time";
        for (int i = 1; i < leaderboardTable.length && i < 11; ++i) {
            GameScore gameScore = leaderboardGameScores.getSortedGameScores().get(i - 1);
            leaderboardTable[i][0] = String.valueOf(i);
            leaderboardTable[i][1] = gameScore.getPlayerName();
            leaderboardTable[i][2] = String.valueOf(gameScore.getScore());
            leaderboardTable[i][3] = gameScore.getDeaths() + "/" + gameScore.getMaxDeaths();
            leaderboardTable[i][4] = String.format("%.3f", gameScore.getTotalTime());
        }

        // draw all game scores
        float xOffset = 0;
        float yOffset = 0;
        boolean isTableTitle = true;
        for (String[] row : leaderboardTable) {
            for (String cell : row) {
                if (cell != null) {
                    getNewUpdatedText(cell, BODY_FONT_SIZE, CENTER_X_OFFSET + xOffset, CENTER_Y_OFFSET + yOffset)
                            .draw(canvas);
                    xOffset += TAB_COL_PADDING;
                }
            }
            if (isTableTitle) {
                yOffset += -1f;
                isTableTitle = false;
            }
            xOffset = 0;
            yOffset += TAB_LINE_PADDING;
        }

        // Back option
        updateText(back, getOptionText(Option.BACK), 0, -50);
        back.draw(canvas);
    }

}
