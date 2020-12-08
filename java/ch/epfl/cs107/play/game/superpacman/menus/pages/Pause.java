/*
 *	Author:      Leonard Cseres
 *	Date:        08.12.20
 *	Time:        14:36
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.superpacman.graphics.ScreenFade;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Pause extends Menu {
    private static final float TITLE_FONT_SIZE = 2.0f;
    private static final float BODY_FONT_SIZE = 0.6f;
    private static final float TEXT_PADDING = 1.5f;
    private static final float PAUSE_MENU_DEPTH = 8500;
    private final ScreenFade screenFade = new ScreenFade(7500, 1);

    /**
     * Constructor for Pause class
     * @param window (Window): the current window
     */
    public Pause(Window window) {
        super(window);
    }

    @Override
    protected Option getDefaultSelection() {
        return Option.RESUME;
    }

    @Override
    protected void setupOptionList() {
        getOptionList().add(Option.RESUME);
        getOptionList().add(Option.END_GAME);
    }

    @Override
    protected void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        screenFade.setFadeOut();
        screenFade.draw(canvas);

        // Title
        String text = Option.PAUSE.text;
        updateText(text, TITLE_FONT_SIZE, getScaledAnchor()
                .add(new Vector(getScaledWidth() / 2 - (text.length() * (TITLE_FONT_SIZE) / 2),
                                getScaledHeight() - 16)), PAUSE_MENU_DEPTH).draw(canvas);

        // Resume option
        text = getOptionText(Option.RESUME);
        updateText(text, BODY_FONT_SIZE,
                   getScaledAnchor()
                           .add(new Vector(getScaledWidth() / 2 - (text.length() * (BODY_FONT_SIZE) / 2), 10.5f)),
                   PAUSE_MENU_DEPTH)
                .draw(canvas);

        // End game option
        text = getOptionText(Option.END_GAME);
        updateText(text, BODY_FONT_SIZE, getScaledAnchor()
                           .add(new Vector(getScaledWidth() / 2 - (text.length() * (BODY_FONT_SIZE) / 2),
                                           10.5f - TEXT_PADDING)),
                   PAUSE_MENU_DEPTH)
                .draw(canvas);

    }

}

