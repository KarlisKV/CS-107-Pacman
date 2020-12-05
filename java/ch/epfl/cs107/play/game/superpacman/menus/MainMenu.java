/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:23
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class MainMenu extends Menu {
    private static final String TITLE_PATH = "superpacman/mainMenuTitle";
    private static final float Y_TEXT_OFFSET = 6.25f;
    private static final float TEXT_PADDING = -4.5f;

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
        getOptionList().add(Option.CREDITS);
    }

    @Override
    public void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // Title image
        updateImage(TITLE_PATH).draw(canvas);

        // Play option
        int paddingCount = 0;
        TextGraphics play = updateText(getOptionText(Option.PLAY), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET);
        play.draw(canvas);
        ++paddingCount;

        // Options option text
        TextGraphics options =
                updateText(getOptionText(Option.OPTIONS), BODY_FONT_SIZE, 0,
                           Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        options.draw(canvas);
        ++paddingCount;

        // Help option text
        TextGraphics help =
                updateText(getOptionText(Option.HELP), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        help.draw(canvas);
        ++paddingCount;

        // Quit option text
        TextGraphics quit =
                updateText(getOptionText(Option.QUIT), BODY_FONT_SIZE, 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        quit.draw(canvas);

        // Credits option text
        TextGraphics credits = updateText(getOptionText(Option.CREDITS), BODY_FONT_SIZE, 0, -50);
        credits.draw(canvas);

    }

}
