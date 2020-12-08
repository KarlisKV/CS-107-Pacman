/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Help extends Menu {
    private static final String HELP_PATH = "superpacman/help";

    /**
     * Constructor for Help class
     * @param window (Window): the current window
     */
    public Help(Window window) {
        super(window);
    }

    @Override
    public Option getDefaultSelection() {
        return Option.BACK;
    }

    @Override
    public void setupOptionList() {
        getOptionList().add(Option.BACK);
    }

    @Override
    public void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float yTextOffSet = (super.getScaledHeight() / 2) - 15;

        // Title
        updateText(Option.HELP.text, HEADER_FONT_SIZE, 0, yTextOffSet).draw(canvas);

        // Help image
        updateImage(HELP_PATH).draw(canvas);

        // Back option
        updateText(getOptionText(Option.BACK), BODY_FONT_SIZE, 0, -50).draw(canvas);

    }
}
