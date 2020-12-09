/*
 *	Author:      Leonard Cseres
 *	Date:        08.12.20
 *	Time:        14:36
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.TextGraphics;
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
    private final TextGraphics title;
    private final TextGraphics resume;
    private final TextGraphics endGame;

    /**
     * Constructor for Pause class
     * @param window (Window): the current window
     */
    public Pause(Window window) {
        super(window);
        title = createText(TITLE_FONT_SIZE);
        title.setDepth(PAUSE_MENU_DEPTH);

        resume = createText(BODY_FONT_SIZE);
        resume.setDepth(PAUSE_MENU_DEPTH);

        endGame = createText(BODY_FONT_SIZE);
        endGame.setDepth(PAUSE_MENU_DEPTH);
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
        String titleText = Option.PAUSE.text;
        updateText(title, titleText, 0, 0);
        title.setAnchor(getScaledAnchor()
                                .add(new Vector(getScaledWidth() / 2 - (titleText.length() * (TITLE_FONT_SIZE) / 2),
                                                getScaledHeight() - 16)));
        title.draw(canvas);

        // Resume option
        String resumeText = getOptionText(Option.RESUME);
        updateText(resume, resumeText, 0, 0);
        resume.setAnchor(getScaledAnchor()
                                 .add(new Vector(getScaledWidth() / 2 - (resumeText.length() * (BODY_FONT_SIZE) / 2),
                                                 10.5f)));
        resume.draw(canvas);

        // End game option
        String endGameText = getOptionText(Option.END_GAME);
        updateText(endGame, endGameText, 0, 0);
        endGame.setAnchor(getScaledAnchor()
                                  .add(new Vector(getScaledWidth() / 2 - (endGameText.length() * (BODY_FONT_SIZE) / 2),
                                                  10.5f - TEXT_PADDING)));
        endGame.draw(canvas);

    }

}

