/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:23
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
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
    private final ImageGraphics title;
    private final TextGraphics play;
    private final TextGraphics options;
    private final TextGraphics help;
    private final TextGraphics quit;
    private final TextGraphics leaderboard;
    private final TextGraphics credits;

    /**
     * Constructor for MainMenu class
     * @param window (Window): the current window
     */
    public MainMenu(Window window) {
        super(window);
        title = createImage(TITLE_PATH);
        play = createText(BODY_FONT_SIZE);
        options = createText(BODY_FONT_SIZE);
        help = createText(BODY_FONT_SIZE);
        quit = createText(BODY_FONT_SIZE);
        leaderboard = createText(BODY_FONT_SIZE);
        credits = createText(BODY_FONT_SIZE);
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
        updateImage(title);
        title.draw(canvas);

        // Play option
        int paddingCount = 0;
        updateText(play, getOptionText(Option.PLAY), 0, Y_TEXT_OFFSET);
        play.draw(canvas);
        ++paddingCount;

        // Options option text
        updateText(options, getOptionText(Option.OPTIONS), 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        options.draw(canvas);
        ++paddingCount;

        // Help option text
        updateText(help, getOptionText(Option.HELP), 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        help.draw(canvas);
        ++paddingCount;

        // Quit option text
        updateText(quit, getOptionText(Option.QUIT), 0, Y_TEXT_OFFSET + TEXT_PADDING * paddingCount);
        quit.draw(canvas);

        // Leaderboard option text
        updateText(leaderboard, getOptionText(Option.LEADERBOARD), 0, -50 - TEXT_PADDING);
        leaderboard.draw(canvas);

        // Credits option text
        updateText(credits, getOptionText(Option.CREDITS), 0, -50);
        credits.draw(canvas);

    }

}
