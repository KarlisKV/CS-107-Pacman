/*
 *	Author:      Leonard Cseres
 *	Date:        05.12.20
 *	Time:        17:07
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class GameOver extends Menu {
    private static final float Y_TEXT_OFFSET = 0;
    private static final String GAME_OVER_TITLE_PATH = "superpacman/gameOverTitle";

    public GameOver(Window window) {
        super(window);
    }

    @Override
    protected Option getDefaultSelection() {
        return Option.RESTART;
    }

    @Override
    protected void setupOptionList() {
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

        // Restart option
        TextGraphics play = updateText(getOptionText(Option.RESTART), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET);
        play.draw(canvas);

        // Back option
        updateText(getOptionText(Option.BACK_TO_MAIN_MENU), BODY_FONT_SIZE, 0, -50).draw(canvas);
    }
}
