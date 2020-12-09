/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        16:44
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Help extends Menu {
    private static final String HELP_PATH = "superpacman/help";
    private final TextGraphics title;
    private final ImageGraphics helpImage;
    private final TextGraphics back;

    /**
     * Constructor for Help class
     * @param window (Window): the current window
     */
    public Help(Window window) {
        super(window);
        title = createText(HEADER_FONT_SIZE);
        helpImage = createImage(HELP_PATH);
        back = createText(BODY_FONT_SIZE);
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
        updateText(title, Option.HELP.text, 0, yTextOffSet);
        title.draw(canvas);

        // Help image
        updateImage(helpImage);
        helpImage.draw(canvas);

        // Back option
        updateText(back, getOptionText(Option.BACK), 0, -50);
        back.draw(canvas);
    }
}
