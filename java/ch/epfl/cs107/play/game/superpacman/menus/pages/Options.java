/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        06:12
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.game.superpacman.menus.SubOption;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.Arrays;

public class Options extends Menu {
    private float centerYOffset;
    private static final float TEXT_PADDING = -5;
    private static final float HEADER_PADDING = -7;
    private final TextGraphics title;
    private final TextGraphics sound;
    private final TextGraphics cameraShake;
    private final TextGraphics fps;
    private final TextGraphics difficulty;
    private final TextGraphics clearLeaderboard;
    private final TextGraphics back;

    /**
     * Constructor for Options class
     * @param window (Window): the current window
     */
    public Options(Window window) {
        super(window);
        title = createText(HEADER_FONT_SIZE);
        sound = createText(BODY_FONT_SIZE);
        cameraShake = createText(BODY_FONT_SIZE);
        fps = createText(BODY_FONT_SIZE);
        difficulty = createText(BODY_FONT_SIZE);
        clearLeaderboard = createText(BODY_FONT_SIZE);
        back = createText(BODY_FONT_SIZE);
    }

    @Override
    public Option getDefaultSelection() {
        return Option.SOUND;
    }

    @Override
    public void setupOptionList() {
        getOptionList().add(Option.SOUND);
        getOptionList().add(Option.CAMERA_SHAKE);
        getOptionList().add(Option.FPS);
        getOptionList().add(Option.DIFFICULTY);
        getOptionList().add(Option.CLEAR_LEADERBOARD);
        getOptionList().add(Option.BACK);
    }

    @Override
    public void setupSubOptionList() {
        getSubOptionList().put(Option.SOUND, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_ON, SubOption.TOGGLE_OFF)));
        getSubOptionList()
                .put(Option.CAMERA_SHAKE, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_ON, SubOption.TOGGLE_OFF)));
        getSubOptionList().put(Option.FPS, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_OFF, SubOption.TOGGLE_ON)));
        getSubOptionList().put(Option.DIFFICULTY, new ArrayList<>(
                Arrays.asList(SubOption.DIFFICULTY_NORMAL, SubOption.DIFFICULTY_HARD, SubOption.DIFFICULTY_IMPOSSIBLE,
                              SubOption.DIFFICULTY_EASY)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        centerYOffset = getScaledHeight() / 3.3f;

        // Title text
        updateText(title, Option.OPTIONS.text, 0, getTopPadding());
        title.draw(canvas);

        // Sound options text
        updateText(sound, Option.SOUND.text + ": " + getSubOptionText(Option.SOUND), 0,
                   centerYOffset + HEADER_PADDING);
        sound.draw(canvas);

        int paddingCount = 1;
        // Camera shake options text
        updateText(cameraShake, Option.CAMERA_SHAKE.text + ": " + getSubOptionText(Option.CAMERA_SHAKE), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        cameraShake.draw(canvas);
        ++paddingCount;

        // Fps options text
        updateText(fps, Option.FPS.text + ": " + getSubOptionText(Option.FPS), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        fps.draw(canvas);
        ++paddingCount;

        // Difficulty options text
        updateText(difficulty, Option.DIFFICULTY.text + ": " + getSubOptionText(Option.DIFFICULTY), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        difficulty.draw(canvas);
        ++paddingCount;

        // Clear leaderboard option text
        updateText(clearLeaderboard, getOptionText(Option.CLEAR_LEADERBOARD), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        clearLeaderboard.draw(canvas);

        // Back option text
        updateText(back, getOptionText(Option.BACK), 0, getBottomPadding());
        back.draw(canvas);
    }
}
