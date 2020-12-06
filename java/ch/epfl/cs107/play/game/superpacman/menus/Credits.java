/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        17:00
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Credits extends Menu {
    private static final String CREDITS_PATH = "superpacman/help";

    public Credits(Window window) {
        super(window);
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
        updateText(Option.CREDITS.text, HEADER_FONT_SIZE, 0, yTextOffSet).draw(canvas);

        // Credits image
//        updateImage(CREDITS_PATH).draw(canvas);

        // Back option
        updateText(getOptionText(Option.BACK), BODY_FONT_SIZE, 0, -50).draw(canvas);


    }
}
