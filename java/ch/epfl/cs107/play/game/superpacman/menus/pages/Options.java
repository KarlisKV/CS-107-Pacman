/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        06:12
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDifficulty;
import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.game.superpacman.menus.SubOption;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Options extends Menu implements Serializable {
    private static final float TEXT_PADDING = -5;
    private static final float HEADER_PADDING = 0;
    private final TextGraphics title;
    private final TextGraphics gameplaySubTitle;
    private final TextGraphics difficulty;
    private final TextGraphics fps;
    private final TextGraphics performanceSubTitle;
    private final TextGraphics sound;
    private final TextGraphics glow;
    private final TextGraphics cameraSmoothing;
    private final TextGraphics cameraShake;
    private final TextGraphics resetSubTitle;
    private final TextGraphics clearLeaderboard;
    private final TextGraphics restoreDefault;
    private final TextGraphics back;

    /**
     * Constructor for Options class
     * @param window (Window): the current window
     */
    public Options(Window window) {
        super(window);
        title = createText(HEADER_FONT_SIZE);
        gameplaySubTitle = createText(SUB_HEADER_FONT_SIZE);
        difficulty = createText(BODY_FONT_SIZE);
        fps = createText(BODY_FONT_SIZE);
        performanceSubTitle = createText(SUB_HEADER_FONT_SIZE);
        sound = createText(BODY_FONT_SIZE);
        glow = createText(BODY_FONT_SIZE);
        cameraSmoothing = createText(BODY_FONT_SIZE);
        cameraShake = createText(BODY_FONT_SIZE);
        resetSubTitle = createText(SUB_HEADER_FONT_SIZE);
        clearLeaderboard = createText(BODY_FONT_SIZE);
        restoreDefault = createText(BODY_FONT_SIZE);
        back = createText(BODY_FONT_SIZE);

        // Reset setting
        SuperPacmanDifficulty defaultDifficulty =
                SuperPacmanDifficulty.getDifficulty(getOptionSubSelection(Option.DIFFICULTY));
        SuperPacmanAreaBehavior.setInitDifficulty(defaultDifficulty);
        MenuStateManager.setSoundDeactivated(false);
        MenuStateManager.setGlowDeactivated(false);
        MenuStateManager.setCameraSmoothingOption(getOptionSubSelection(Option.CAMERA_SMOOTHING));
        MenuStateManager.setCameraChangeRequest(true);
        MenuStateManager.setCameraShakeDeactivated(false);
        MenuStateManager.setShowFps(false);
    }

    @Override
    public Option getDefaultSelection() {
        return Option.DIFFICULTY;
    }

    @Override
    public void setupOptionList() {
        getOptionList().add(Option.DIFFICULTY);
        getOptionList().add(Option.FPS);
        getOptionList().add(Option.SOUND);
        getOptionList().add(Option.GLOW);
        getOptionList().add(Option.CAMERA_SMOOTHING);
        getOptionList().add(Option.CAMERA_SHAKE);
        getOptionList().add(Option.CLEAR_LEADERBOARD);
        getOptionList().add(Option.RESTORE_DEFAULT);
        getOptionList().add(Option.BACK);
    }

    @Override
    public void setupSubOptionList() {
        getSubOptionList().put(Option.DIFFICULTY, new ArrayList<>(Arrays.asList(SubOption.DIFFICULTY_NORMAL, SubOption.DIFFICULTY_HARD, SubOption.DIFFICULTY_IMPOSSIBLE,SubOption.DIFFICULTY_EASY)));
        getSubOptionList().put(Option.FPS, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_OFF, SubOption.TOGGLE_ON)));
        getSubOptionList().put(Option.SOUND, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_ON, SubOption.TOGGLE_OFF)));
        getSubOptionList().put(Option.GLOW, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_ON, SubOption.TOGGLE_OFF)));
        getSubOptionList().put(Option.CAMERA_SMOOTHING, new ArrayList<>(Arrays.asList(SubOption.CAMERA_SMOOTH, SubOption.CAMERA_VERY_SMOOTH, SubOption.CAMERA_NO_SMOOTH)));
        getSubOptionList().put(Option.CAMERA_SHAKE, new ArrayList<>(Arrays.asList(SubOption.TOGGLE_ON, SubOption.TOGGLE_OFF)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float centerYOffset = getScaledHeight() / 3.3f;

        // Title text
        updateText(title, Option.OPTIONS.text, 0, getTopPadding());
        title.draw(canvas);

        float paddingCount = 0;
        // Gameplay sub-title text
        updateText(gameplaySubTitle, "Gameplay", 0, getTopPadding() - 11);
        gameplaySubTitle.draw(canvas);
        paddingCount += 1.9f;

        // Difficulty options text
        updateText(difficulty, Option.DIFFICULTY.text + ": " + getSubOptionText(Option.DIFFICULTY), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        difficulty.draw(canvas);
        ++paddingCount;

        // Fps options text
        updateText(fps, Option.FPS.text + ": " + getSubOptionText(Option.FPS), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        fps.draw(canvas);
        paddingCount += 2;

        // Performance sub-title text
        updateText(performanceSubTitle, "Performance", 0, centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        performanceSubTitle.draw(canvas);
        paddingCount += 1.25f;


        // Sound options text
        updateText(sound, Option.SOUND.text + ": " + getSubOptionText(Option.SOUND), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        sound.draw(canvas);
        ++paddingCount;

        // Glow options text
        updateText(glow, Option.GLOW.text + ": " + getSubOptionText(Option.GLOW), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        glow.draw(canvas);
        ++paddingCount;

        // Camera shake options text
        updateText(cameraSmoothing, Option.CAMERA_SMOOTHING.text + ": " + getSubOptionText(Option.CAMERA_SMOOTHING), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        cameraSmoothing.draw(canvas);
        ++paddingCount;

        // Camera shake options text
        updateText(cameraShake, Option.CAMERA_SHAKE.text + ": " + getSubOptionText(Option.CAMERA_SHAKE), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        cameraShake.draw(canvas);
        paddingCount += 2;

        // Reset sub-title text
        updateText(resetSubTitle, "Reset", 0, centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        resetSubTitle.draw(canvas);
        paddingCount += 1.25f;

        // Clear leaderboard option text
        updateText(clearLeaderboard, getOptionText(Option.CLEAR_LEADERBOARD), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        clearLeaderboard.draw(canvas);
        ++paddingCount;

        // Restore default option text
        updateText(restoreDefault, getOptionText(Option.RESTORE_DEFAULT), 0,
                   centerYOffset + TEXT_PADDING * paddingCount + HEADER_PADDING);
        restoreDefault.draw(canvas);


        // Back option text
        updateText(back, getOptionText(Option.BACK), 0, getBottomPadding());
        back.draw(canvas);
    }
}
