/*
 *	Author:      Leonard Cseres
 *	Date:        12.12.20
 *	Time:        23:28
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class HelpGhosts extends Menu {
    private static final String HELP_GHOSTS_PATH = "superpacman/helpGhosts";
    private final TextGraphics title;
    private final ImageGraphics helpGhostsImage;
    private final TextGraphics back;

    /**
     * Constructor for HelpGhosts class
     * @param window (Window): the current window
     */
    public HelpGhosts(Window window) {
        super(window);
        title = createText(HEADER_FONT_SIZE);
        helpGhostsImage = createImage(HELP_GHOSTS_PATH);
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

        // Title
        updateText(title, Option.HELP.text, 0, getTopPadding());
        title.draw(canvas);

        // Help image
        updateImage(helpGhostsImage);
        helpGhostsImage.draw(canvas);

        // Back option
        updateText(back, getOptionText(Option.BACK), 0, getBottomPadding());
        back.draw(canvas);
    }
}
