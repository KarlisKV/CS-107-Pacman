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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

public final class MenuStateManager implements Updatable, Graphics, Acoustics {
    private static final SoundAcoustics ENTER_SOUND = SuperPacmanSound.MENU_ENTER.sound;
    private static final SoundAcoustics EXIT_SOUND = SuperPacmanSound.MENU_EXIT.sound;
    private static final String LEADERBOARD_TMP_FILENAME = "leaderboard.ser";
    private static boolean startGame = false;
    private static boolean endGame = false;
    private static boolean gameOver = false;
    private static boolean debugMode = false;
    private static boolean godMode = false;
    private static boolean speedMode = false;
    private static boolean quit = false;
    private static boolean paused = false;
    private static boolean soundDeactivated = false;
    private static boolean cameraShakeDeactivated = false;
    private static boolean showFps = false;
    private final ScreenFade screenFade = new ScreenFade(25000, 0.005f);
    private final Deque<Menu> menuStack = new ArrayDeque<>();
    private final Keyboard keyboard;
    private final Map<MenuState, Menu> menuStates = new EnumMap<>(MenuState.class);
    private final Window window;

    /**
     * Constructor for MenuItems class
     * @param window (Window): the current window
     */
    public MenuStateManager(Window window) {
        keyboard = window.getKeyboard();
        this.window = window;

        Menu mainMenu = new MainMenu(window);
        menuStates.put(MenuState.MAIN_MENU, mainMenu);
        menuStack.push(mainMenu);

        Menu options = new Options(window);
        menuStates.put(MenuState.OPTIONS, options);

        Menu help = new Help(window);
        menuStates.put(MenuState.HELP, help);

        Menu leaderboard = new Leaderboard(window);
        menuStates.put(MenuState.LEADERBOARD, leaderboard);

        Menu credits = new Credits(window);
        menuStates.put(MenuState.CREDITS, credits);

        Menu death = new GameOver(window);
        menuStates.put(MenuState.GAME_OVER, death);

        Menu play = new Play(window);
        menuStates.put(MenuState.PLAY, play);

        Menu quit = new Quit(window);
        menuStates.put(MenuState.QUIT, quit);

        Menu pause = new Pause(window);
        menuStates.put(MenuState.PAUSE, pause);

        screenFade.setFadeIn();
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public static boolean isPaused() {
        return paused;
    }

    public static boolean isEndGame() {
        return endGame;
    }

    public static void setEndGame(boolean endGame) {
        MenuStateManager.endGame = endGame;
    }

    protected static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        MenuStateManager.gameOver = gameOver;
    }

    public static boolean isCameraShakeDeactivated() {
        return cameraShakeDeactivated;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static boolean isGodMode() {
        return godMode;
    }

    public static boolean isSpeedMode() {
        return speedMode;
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
        MenuStateManager.startGame = startGame;
    }

    public static boolean isQuit() {
        return quit;
    }

    protected static void setQuit(boolean quit) {
        MenuStateManager.quit = quit;
    }

    @Override
    public void bip(Audio audio) {
        ENTER_SOUND.bip(audio);
        EXIT_SOUND.bip(audio);
        menuStack.peek().bip(audio);
    }

    @Override
    public void draw(Canvas canvas) {
        screenFade.draw(canvas);

        assert menuStack.peek() != null;
        menuStack.peek().draw(canvas);
        selectOption(menuStack.peek());
    }

    /**
     * Method to update menu state if Enter key is pressed
     * @param menu the selected menu option
     */
    private void selectOption(Menu menu) {
        if (enterKeyIsPressed() && menu.getCurrentSelection() != null) {
            ENTER_SOUND.shouldBeStarted();
            switch (menu.getCurrentSelection()) {
                case PLAY:
                    menuStack.push(menuStates.get(MenuState.PLAY));
                    startGame = true;
                    break;
                case RESTART:
                    Serialization.serialize(SuperPacman.getLeaderboardScores(), LEADERBOARD_TMP_FILENAME);
                    menuStack.push(menuStates.get(MenuState.PLAY));
                    startGame = true;
                    break;
                case OPTIONS:
                    menuStack.push(menuStates.get(MenuState.OPTIONS));
                    break;
                case HELP:
                    menuStack.push(menuStates.get(MenuState.HELP));
                    break;
                case LEADERBOARD:
                    menuStack.push(menuStates.get(MenuState.LEADERBOARD));
                    break;
                case CREDITS:
                    menuStack.push(menuStates.get(MenuState.CREDITS));
                    break;
                case BACK_TO_MAIN_MENU:
                    Serialization.serialize(SuperPacman.getLeaderboardScores(), LEADERBOARD_TMP_FILENAME);
                    menu.reset();
                    menuStack.clear();
                    menuStack.push(menuStates.get(MenuState.MAIN_MENU));
                    break;
                case BACK:
                    menuStack.removeFirst();
                    menu.reset();
                    break;
                case RESUME:
                    paused = false;
                    menuStack.removeFirst();
                    menu.reset();
                    break;
                case END_GAME:
                    endGame = true;
                    paused = false;
                    menuStack.removeFirst();
                    menu.reset();
                    menuStack.push(menuStates.get(MenuState.MAIN_MENU));
                    break;
                case QUIT:
                    menuStack.push(menuStates.get(MenuState.QUIT));
                    EXIT_SOUND.shouldBeStarted();
                    soundDeactivated = false;
                    quit = true;
                    break;
                // Options within a page
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
                case CLEAR_LEADERBOARD:
                    SuperPacman.getLeaderboardScores().clear();
                    Serialization.delete(LEADERBOARD_TMP_FILENAME);
                    break;
                default:
                    // empty on purpose, do noting
            }
        }
        // pause menu
        if (escKeyIsPressed()) {
            assert menuStack.peek() != null;
            if (menuStack.peek().equals(menuStates.get(MenuState.PLAY))) {
                SoundAcoustics.stopAllSounds(window);
                menuStack.push(menuStates.get(MenuState.PAUSE));
                paused = true;
            } else if (menuStack.peek().equals(menuStates.get(MenuState.PAUSE))) {
                menuStack.removeFirst();
                menu.reset();
                paused = false;
            }
        }
    }

    /**
     * @return (true) if Enter key is pressed
     */
    private boolean enterKeyIsPressed() {
        return keyboard.get(Keyboard.ENTER).isPressed();
    }

    /**
     * @return (true) if Esc key is pressed
     */
    private boolean escKeyIsPressed() {
        return keyboard.get(Keyboard.ESC).isPressed();
    }

    @Override
    public void update(float deltaTime) {
        // Press shift, ctrl and alt to enter debug mode
        if (!debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.CTRL).isDown() &&
                keyboard.get(Keyboard.ALT).isDown() && menuStack.peek() != menuStates.get(MenuState.PLAY) &&
                menuStack.peek() != menuStates.get(MenuState.GAME_OVER)) {
            System.out.println("Debug mode enabled");
            debugMode = true;
        }

        if (debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.G).isPressed()) {
            godMode = !godMode;
            System.out.print("God mode ");
            if (godMode) {
                System.out.println("enabled");
            } else {
                System.out.println("disabled");
            }
        }

        if (debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.S).isPressed()) {
            speedMode = !speedMode;
            System.out.print("Speed mode ");
            if (speedMode) {
                System.out.println("enabled");
            } else {
                System.out.println("disabled");
            }
        }
        if (gameOver) {
            menuStack.push(menuStates.get(MenuState.GAME_OVER));
            gameOver = false;
        }
    }


    /**
     * Private enum of all of the menu states
     */
    private enum MenuState {
        MAIN_MENU(),
        PLAY(),
        OPTIONS(),
        HELP(),
        LEADERBOARD(),
        CREDITS(),
        GAME_OVER(),
        QUIT(),
        PAUSE()
    }
}
