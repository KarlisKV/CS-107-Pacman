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
import ch.epfl.cs107.play.game.superpacman.SoundUtility;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDepth;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDifficulty;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanSound;
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
    private static final String OPTIONS_TMP_FILENAME = "options.ser";
    private static boolean isMenuIntractable = true;
    private static boolean startGame = false;
    private static boolean endGame = false;
    private static boolean gameOver = false;
    private static boolean debugMode = false;
    private static boolean godMode = false;
    private static boolean speedMode = false;
    private static boolean quit = false;
    private static boolean paused = false;
    private static boolean soundDeactivated = false;
    private static boolean glowDeactivated = false;
    private static SubOption cameraSmoothingOption = SubOption.CAMERA_SMOOTH;
    private static boolean cameraChangeRequest = false;
    private static boolean cameraShakeDeactivated = false;
    private static boolean showFps = false;
    private static Window window;
    private final SoundUtility menuStateSoundUtility;
    private final ScreenFade screenFade = new ScreenFade(SuperPacmanDepth.MENU_SCREEN_FADE.value, 0.005f);
    private final Deque<Menu> menuStack = new ArrayDeque<>();
    private final Keyboard keyboard;
    private final Map<MenuState, Menu> menuStates = new EnumMap<>(MenuState.class);

    /**
     * Constructor for MenuItems class
     * @param window (Window): the current window
     */
    public MenuStateManager(Window window) {
        keyboard = window.getKeyboard();
        MenuStateManager.window = window;

        Menu mainMenu = new MainMenu(window);
        menuStates.put(MenuState.MAIN_MENU, mainMenu);
        menuStack.push(mainMenu);

        Object optionsMenu = Serialization.deserialize(OPTIONS_TMP_FILENAME);
        if (optionsMenu == null) {
            menuStates.put(MenuState.OPTIONS, new Options(window));
        } else {
            menuStates.put(MenuState.OPTIONS, (Menu) optionsMenu);
        }

        menuStates.put(MenuState.HELP, new Help(window));
        menuStates.put(MenuState.HELP_GHOSTS, new HelpGhosts(window));
        menuStates.put(MenuState.HELP_SCORE, new HelpScore(window));
        menuStates.put(MenuState.LEADERBOARD, new Leaderboard(window));
        menuStates.put(MenuState.CREDITS, new Credits(window));
        menuStates.put(MenuState.GAME_OVER, new GameOver(window));
        menuStates.put(MenuState.PLAY, new Play(window));
        menuStates.put(MenuState.QUIT, new Quit(window));
        menuStates.put(MenuState.PAUSE, new Pause(window));

        screenFade.setFadeIn();
        menuStateSoundUtility = new SoundUtility(new SoundAcoustics[]{ENTER_SOUND, EXIT_SOUND}, false);
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public static void setIsMenuIntractable(boolean isMenuIntractable) {
        MenuStateManager.isMenuIntractable = isMenuIntractable;
    }

    public static Window getWindow() {
        return window;
    }

    public static boolean isCameraChangeRequest() {
        return cameraChangeRequest;
    }

    public static void setCameraChangeRequest(boolean cameraChangeRequest) {
        MenuStateManager.cameraChangeRequest = cameraChangeRequest;
    }

    public static SubOption getCameraSmoothingOption() {
        return cameraSmoothingOption;
    }

    public static void setCameraSmoothingOption(SubOption cameraSmoothingOption) {
        MenuStateManager.cameraSmoothingOption = cameraSmoothingOption;
    }

    public static boolean isGlowDeactivated() {
        return glowDeactivated;
    }

    public static void setGlowDeactivated(boolean glowDeactivated) {
        MenuStateManager.glowDeactivated = glowDeactivated;
    }

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

    public static void setCameraShakeDeactivated(boolean cameraShakeDeactivated) {
        MenuStateManager.cameraShakeDeactivated = cameraShakeDeactivated;
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

    public static void setShowFps(boolean showFps) {
        MenuStateManager.showFps = showFps;
    }

    public static boolean isSoundDeactivated() {
        return soundDeactivated;
    }

    public static void setSoundDeactivated(boolean soundDeactivated) {
        MenuStateManager.soundDeactivated = soundDeactivated;
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
        menuStateSoundUtility.bip(audio);
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
        if (enterKeyIsPressed() && menu.getCurrentSelection() != null && isMenuIntractable) {
            menuStateSoundUtility.play(ENTER_SOUND);
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
                case MORE_GHOSTS:
                    menuStack.push(menuStates.get(MenuState.HELP_GHOSTS));
                    break;
                case MORE_POINTS:
                    menuStack.push(menuStates.get(MenuState.HELP_SCORE));
                    break;
                case BACK:
                    assert menuStack.peek() != null;
                    if (menuStack.peek().equals(menuStates.get(MenuState.OPTIONS))) {
                        Serialization.serialize(menuStack.peek(), OPTIONS_TMP_FILENAME);
                    }
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
                    menuStateSoundUtility.play(EXIT_SOUND);
                    quit = true;
                    break;
                // Options within a page
                case SOUND:
                    menu.updateSubSelection();
                    soundDeactivated = !menu.isToggleLogic();
                    break;
                case GLOW:
                    menu.updateSubSelection();
                    glowDeactivated = !menu.isToggleLogic();
                    break;
                case CAMERA_SMOOTHING:
                    menu.updateSubSelection();
                    cameraSmoothingOption = menu.getCurrentSubSelection();
                    cameraChangeRequest = true;
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
                case RESTORE_DEFAULT:
                    Serialization.delete(OPTIONS_TMP_FILENAME);
                    menuStack.removeFirst();
                    Menu newOption = new Options(window);
                    // Set same selection
                    newOption.setSelectionCount(menu.getSelectionCount());
                    newOption.setSubSelectionCount(menu.getSubSelectionCount());
                    newOption.setCurrentSelection(menu.getCurrentSelection());
                    newOption.setCurrentSubSelection(menu.getCurrentSubSelection());
                    menuStack.push(newOption);
                    menuStates.replace(MenuState.OPTIONS, newOption);

                    break;
                default:
                    // empty on purpose, do noting
            }
        }
        // pause menu
        if (escKeyIsPressed() && SuperPacmanPlayer.canUserMove() && isMenuIntractable) {
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
                menuStack.peek() != menuStates.get(MenuState.GAME_OVER) && isMenuIntractable) {
            System.out.println("Debug mode enabled");
            debugMode = true;
        }

        if (debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.G).isPressed() && isMenuIntractable) {
            godMode = !godMode;
            System.out.print("God mode ");
            if (godMode) {
                System.out.println("enabled");
            } else {
                System.out.println("disabled");
            }
        }

        if (debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.S).isPressed() && isMenuIntractable) {
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
        HELP_GHOSTS(),
        HELP_SCORE(),
        LEADERBOARD(),
        CREDITS(),
        GAME_OVER(),
        QUIT(),
        PAUSE()
    }
}
