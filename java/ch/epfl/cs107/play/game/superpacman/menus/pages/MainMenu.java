/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:23
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.superpacman.graphics.ScreenFade;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class MainMenu extends Menu {
    private static final String TITLE_PATH = "superpacman/mainMenuTitle";
    private static final float Y_TEXT_OFFSET = 6.25f;
    private static final float TEXT_PADDING = -4.5f;
    private final ScreenFade screenFade = new ScreenFade(7500, 1);


    /**
     * Constructor for MainMenu class
     * @param window (Window): the current window
     */
    public MainMenu(Window window) {
        super(window);
    }

    @Override
    public Option getDefaultSelection() {
        return Option.PLAY;
    }

    @Override
    public void setupOptionList() {
        getOptionList().add(Option.PLAY);
        getOptionList().add(Option.OPTIONS);
        getOptionList().add(Option.HELP);
        getOptionList().add(Option.QUIT);
        getOptionList().add(Option.LEADERBOARD);
        getOptionList().add(Option.CREDITS);
    }

    @Override
    public void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        screenFade.setFadeOut();
        screenFade.draw(canvas);

        // Title image
        updateImage(TITLE_PATH).draw(canvas);

        // Play option
        int paddingCount = 0;
        updateText(getOptionText(Option.PLAY), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET).draw(canvas);
        ++paddingCount;

        // Options option text
        updateText(getOptionText(Option.OPTIONS), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount)
                .draw(canvas);
        ++paddingCount;

        // Help option text
        updateText(getOptionText(Option.HELP), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount)
                .draw(canvas);
        ++paddingCount;

        // Quit option text
        updateText(getOptionText(Option.QUIT), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount)
                .draw(canvas);

        // Leaderboard option text
        updateText(getOptionText(Option.LEADERBOARD), BODY_FONT_SIZE, 0, -50 - TEXT_PADDING).draw(canvas);

        // Credits option text
        updateText(getOptionText(Option.CREDITS), BODY_FONT_SIZE, 0, -50).draw(canvas);

    }

}
