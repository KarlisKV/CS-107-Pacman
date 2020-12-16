/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        17:00
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Credits extends Menu {
    private static final String CREDITS_PATH = "superpacman/credits";
    private final TextGraphics title;
    private final ImageGraphics creditsImage;
    private final TextGraphics back;

    /**
     * Constructor for Credits class
     * @param window (Window): the current window
     */
    public Credits(Window window) {
        super(window);
        title = createText(HEADER_FONT_SIZE);
        creditsImage = createImage(CREDITS_PATH);
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
        updateText(title, Option.CREDITS.text, 0, getTopPadding());
        title.draw(canvas);

        // Credits image
        updateImage(creditsImage);
        creditsImage.draw(canvas);

        // Back option
        updateText(back, getOptionText(Option.BACK), 0, getBottomPadding());
        back.draw(canvas);
    }
}
