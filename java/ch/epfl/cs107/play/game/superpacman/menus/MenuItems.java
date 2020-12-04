/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        03:52
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.actor.Acoustics;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanDifficulty;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanSound;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanAreaBehavior;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.transitions.EaseInOutCubic;
import ch.epfl.cs107.play.math.transitions.Transition;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public final class MenuItems implements Graphics, Acoustics {
    private static final SoundAcoustics ENTER_SOUND = SuperPacmanSound.MENU_ENTER.sound;
    private static final SoundAcoustics EXIT_SOUND = SuperPacmanSound.MENU_EXIT.sound;
    private static final float OVERLAY_DEPTH = Menu.DEPTH + 500;
    private static boolean startGame = false;
    private static boolean debugMode = false;
    private static boolean exit = false;
    private static boolean soundDeactivated = false;
    private static boolean showFps = false;
    private final Transition transitionOverlay = new EaseInOutCubic(0.01f);
    private final EnumMap<MenuState, Menu> menus = new EnumMap<>(MenuState.class);
    private final Keyboard keyboard;
    private MenuState currentState = MenuState.MAIN_MENU_PAGE;
    private boolean updateState = true;
    public MenuItems(Window window) {
        keyboard = window.getKeyboard();

        Menu mainMenu = new MainMenu(window);
        menus.put(MenuState.MAIN_MENU_PAGE, mainMenu);
        Menu options = new Options(window);
        menus.put(MenuState.OPTIONS_PAGE, options);
        Menu help = new Help(window);
        menus.put(MenuState.HELP_PAGE, help);
        Menu credits = new Credits(window);
        menus.put(MenuState.CREDITS_PAGE, credits);
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

    public static boolean isExit() {
        return exit;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!debugMode && keyboard.get(Keyboard.SHIFT).isDown() && keyboard.get(Keyboard.CTRL).isDown() &&
                keyboard.get(Keyboard.ALT).isDown()) {
            System.out.println("Debug mode enabled");
            debugMode = true;
        }
        for (Map.Entry<MenuState, Menu> menuStateMenuEntry : menus.entrySet()) {
            if (menuStateMenuEntry.getKey().equals(currentState)) {
                menuStateMenuEntry.getValue().draw(canvas);
                selectOption(menuStateMenuEntry.getValue());
            }
        }
        updateState = true;

        float alphaOverlay = 0.0f;
        if (!transitionOverlay.isFinished()) {
            alphaOverlay = 1 - transitionOverlay.getProgress();
        }

        // DRAW BLACK OVERLAY
        ShapeGraphics overlay =
                new ShapeGraphics(new Circle(300, new Vector(canvas.getScaledWidth(), canvas.getScaledHeight())),
                                  Color.BLACK, Color.BLACK, 0.0f,
                                  alphaOverlay, OVERLAY_DEPTH + 500);
        overlay.draw(canvas);
    }

    private void selectOption(Menu menu) {
        if (enterKeyIsPressed() && updateState) {
            ENTER_SOUND.shouldBeStarted();
            switch (menu.getCurrentSelection()) {
                case PLAY:
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
                case CREDITS:
                    currentState = MenuState.CREDITS_PAGE;
                    break;
                case BACK:
                    menu.reset();
                    currentState = MenuState.MAIN_MENU_PAGE;
                    break;
                case SOUND:
                    menu.updateSubSelection();
                    soundDeactivated = !menu.isToggleLogic();
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

    private boolean enterKeyIsPressed() {
        return keyboard.get(Keyboard.ENTER).isPressed();
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

    private enum MenuState {
        MAIN_MENU_PAGE(),
        PLAY(),
        OPTIONS_PAGE(),
        HELP_PAGE(),
        CREDITS_PAGE(),
        EXIT()
    }
}
