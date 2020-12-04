/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        06:12
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.Arrays;

public class Options extends Menu {
    private static final float TEXT_PADDING = -5;
    private static final float HEADER_PADDING = -7;


    public Options(Window window) {
        super(window);
    }

    @Override
    public void setupOptionList() {
        getOptionList().add(Option.SOUND);
        getOptionList().add(Option.FPS);
        getOptionList().add(Option.DIFFICULTY);
        getOptionList().add(Option.BACK);
    }

    @Override
    public void setupSubOptionList() {
        getSubOptionList().put(Option.SOUND, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_ON, SubOption.TOGGLE_OFF)));
        getSubOptionList().put(Option.FPS, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_OFF, SubOption.TOGGLE_ON)));
        getSubOptionList().put(Option.DIFFICULTY, new ArrayList<>(Arrays.asList(SubOption.DIFFICULTY_NORMAL, SubOption.DIFFICULTY_HARD, SubOption.DIFFICULTY_IMPOSSIBLE, SubOption.DIFFICULTY_EASY)));
    }

    @Override
    public Option getDefaultSelection() {
        return Option.SOUND;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float yTextOffSet = (super.getScaledHeight() / 2) - 15;

        // Title text
        updateText(Option.OPTIONS.text, HEADER_FONT_SIZE, 0, yTextOffSet).draw(canvas);

        // Sound options text
        TextGraphics sound = updateText(Option.SOUND.text + ": " + getSubOptionText(Option.SOUND), BODY_FONT_SIZE, 0,
                                       yTextOffSet + HEADER_PADDING);
        sound.draw(canvas);

        int paddingCount = 0;
        // Graphics options text
        ++paddingCount;
        TextGraphics graphics = updateText(Option.FPS.text + ": " + getSubOptionText(Option.FPS), BODY_FONT_SIZE, 0,
                                       yTextOffSet + TEXT_PADDING * paddingCount + HEADER_PADDING);
        graphics.draw(canvas);

        // Difficulty options text
        ++paddingCount;
        TextGraphics difficulty = updateText(Option.DIFFICULTY.text + ": " + getSubOptionText(Option.DIFFICULTY), BODY_FONT_SIZE, 0,
                                           yTextOffSet + TEXT_PADDING * paddingCount + HEADER_PADDING);
        difficulty.draw(canvas);

        // Back option text
        updateText(getOptionText(Option.BACK), BODY_FONT_SIZE, 0, -50).draw(canvas);

    }
}
