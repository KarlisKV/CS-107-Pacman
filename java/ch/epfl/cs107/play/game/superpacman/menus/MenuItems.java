/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        03:52
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.actor.Acoustics;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanDifficulty;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanSound;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.graphics.ScreenFade;
import ch.epfl.cs107.play.game.superpacman.menus.pages.*;
import ch.epfl.cs107.play.io.Serialization;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.EnumMap;
import java.util.Map;

public final class MenuItems implements Updatable, Graphics, Acoustics {
    private static final SoundAcoustics ENTER_SOUND = SuperPacmanSound.MENU_ENTER.sound;
    private static final SoundAcoustics EXIT_SOUND = SuperPacmanSound.MENU_EXIT.sound;
    private static final String LEADERBOARD_TMP_FILENAME = "leaderboard.ser";
    private static boolean startGame = false;
    private static boolean gameOver = false;
    private static boolean debugMode = false;
    private static boolean exit = false;
    private static boolean soundDeactivated = false;
    private static boolean cameraShakeDeactivated = false;
    private static boolean showFps = false;
    private final ScreenFade screenFade = new ScreenFade(15000, 0.02f);
    private final EnumMap<MenuState, Menu> menus = new EnumMap<>(MenuState.class);
    private final Keyboard keyboard;
    private MenuState currentState = MenuState.MAIN_MENU_PAGE;
    private boolean updateState = true;

    /**
     * Constructor for MenuItems class
     * @param window (Window): the current window
     */
    public MenuItems(Window window) {
        keyboard = window.getKeyboard();

        Menu mainMenu = new MainMenu(window);
        menus.put(MenuState.MAIN_MENU_PAGE, mainMenu);
        Menu options = new Options(window);
        menus.put(MenuState.OPTIONS_PAGE, options);
        Menu help = new Help(window);
        menus.put(MenuState.HELP_PAGE, help);
        Menu leaderboard = new Leaderboard(window);
        menus.put(MenuState.LEADERBOARD_PAGE, leaderboard);
        Menu credits = new Credits(window);
        menus.put(MenuState.CREDITS_PAGE, credits);
        Menu death = new GameOver(window);
        menus.put(MenuState.GAME_OVER, death);
        screenFade.setFadeIn();
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    protected static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        MenuItems.gameOver = gameOver;
    }

    public static boolean isCameraShakeDeactivated() {
        return cameraShakeDeactivated;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static boolean isShowFps() {
        return showFps;
    }

    public static boolean isSoundDeactivated() {
        return soundDeactivated;
    }

    public static boolean isStartGame() {
        return startGame;
    }

    public static void setStartGame(boolean startGame) {
        MenuItems.startGame = startGame;
    }

    public static boolean isExit() {
        return exit;
    }

    protected static void setExit(boolean exit) {
        MenuItems.exit = exit;
    }

    @Override
    public void bip(Audio audio) {
        ENTER_SOUND.bip(audio);
        EXIT_SOUND.bip(audio);
        for (Map.Entry<MenuState, Menu> menuStateMenuEntry : menus.entrySet()) {
            if (menuStateMenuEntry.getKey().equals(currentState)) {
                menuStateMenuEntry.getValue().bip(audio);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        screenFade.draw(canvas);

        for (Map.Entry<MenuState, Menu> menuStateMenuEntry : menus.entrySet()) {
            if (menuStateMenuEntry.getKey().equals(currentState)) {
                menuStateMenuEntry.getValue().draw(canvas);
                selectOption(menuStateMenuEntry.getValue());
            }
        }
        updateState = true;
    }

    /**
     * Method to update menu state if Enter key is pressed
     * @param menu the selected menu option
     */
    private void selectOption(Menu menu) {
        if (enterKeyIsPressed() && updateState) {
            ENTER_SOUND.shouldBeStarted();
            switch (menu.getCurrentSelection()) {
                case PLAY:
                    currentState = MenuState.PLAY;
                    startGame = true;
                    break;
                case RESTART:
                    Serialization.serialize(SuperPacman.getLeaderboardScores(), LEADERBOARD_TMP_FILENAME);
                    currentState = MenuState.PLAY;
                    startGame = true;
                    break;
                case OPTIONS:
                    currentState = MenuState.OPTIONS_PAGE;
                    break;
                case HELP:
                    currentState = MenuState.HELP_PAGE;
                    break;
                case QUIT:
                    currentState = MenuState.EXIT;
                    EXIT_SOUND.shouldBeStarted();
                    soundDeactivated = false;
                    exit = true;
                    break;
                case LEADERBOARD:
                    currentState = MenuState.LEADERBOARD_PAGE;
                    break;
                case CLEAR_LEADERBOARD:
                    SuperPacman.getLeaderboardScores().clear();
                    Serialization.delete(LEADERBOARD_TMP_FILENAME);
                    break;
                case CREDITS:
                    currentState = MenuState.CREDITS_PAGE;
                    break;
                case BACK_TO_MAIN_MENU:
                    Serialization.serialize(SuperPacman.getLeaderboardScores(), LEADERBOARD_TMP_FILENAME);
                    menu.reset();
                    currentState = MenuState.MAIN_MENU_PAGE;
                    break;
                case BACK:
                    menu.reset();
                    currentState = MenuState.MAIN_MENU_PAGE;
                    break;
                case SOUND:
                    menu.updateSubSelection();
                    soundDeactivated = !menu.isToggleLogic();
                    break;
                case CAMERA_SHAKE:
                    menu.updateSubSelection();
                    cameraShakeDeactivated = !menu.isToggleLogic();
                    break;
                case FPS:
                    menu.updateSubSelection();
                    showFps = menu.isToggleLogic();
                    break;
                case DIFFICULTY:
                    menu.updateSubSelection();
                    SuperPacmanDifficulty difficulty =
                            SuperPacmanDifficulty.getDifficulty(menu.getCurrentSubSelection());
                    SuperPacmanAreaBehavior.setInitDifficulty(difficulty);
                    break;
                default:
                    // empty on purpose, do noting
            }
            updateState = false;
        }
    }

    /**
     * @return (true) if Enter key is pressed
     */
    private boolean enterKeyIsPressed() {
        return keyboard.get(Keyboard.ENTER).isPressed();
    }

    @Override
    public void update(float deltaTime) {
        // Press shift, ctrl and alt to enter debug mode
        if (!debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.CTRL).isDown() &&
                keyboard.get(Keyboard.ALT).isDown() && currentState != MenuState.PLAY &&
                currentState != MenuState.GAME_OVER) {
            System.out.println("Debug mode enabled");
            debugMode = true;
        }
        if (gameOver) {
            currentState = MenuState.GAME_OVER;
            gameOver = false;
        }
    }


    /**
     * Private enum of all of the menu states
     */
    private enum MenuState {
        MAIN_MENU_PAGE(),
        PLAY(),
        OPTIONS_PAGE(),
        HELP_PAGE(),
        LEADERBOARD_PAGE(),
        CREDITS_PAGE(),
        GAME_OVER(),
        EXIT()
    }
}
