/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        04:05
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.*;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanSound;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Menu implements Graphics, Acoustics {
    public static final float DEPTH = 20000;
    protected static final float HEADER_FONT_SIZE = 5;
    protected static final float BODY_FONT_SIZE = 2;
    protected static final String FONT = "emulogic";
    private static final SoundAcoustics SELECT_SOUND = SuperPacmanSound.MENU_SELECT.sound;
    private static final SoundAcoustics KEY_CLICK_SOUND = SuperPacmanSound.MENU_KEY_CLICK.sound;
    private static final SoundAcoustics ERROR_SOUND = SuperPacmanSound.MENU_ERROR.sound;
    private final Keyboard keyboard;
    private final float alpha = 1.0f;
    private final EnumMap<Option, List<SubOption>> subOptionList = new EnumMap<>(Option.class);
    private final EnumMap<Option, SubOption> subOptionSectionList = new EnumMap<>(Option.class);
    private final List<Option> optionList = new ArrayList<>();
    private float scaledWidth;
    private float scaledHeight;
    private Vector anchor;
    private float width;
    private float height;
    private int selectionCount = 0;
    private int subSelectionCount = 0;
    private Option currentSelection;
    private SubOption currentSubSelection;
    private boolean toggleLogic = true;

    /**
     * Constructor for Menu class
     * @param window (Window): the current window
     */
    public Menu(Window window) {
        keyboard = window.getKeyboard();
        currentSelection = getDefaultSelection();
        setupOptionList();
        setupSubOptionList();
        setupSubOptionSelectionList();
    }

    /**
     * Abstract method to get which option is selected by default when entering the Menu
     * @return a selected Option
     */
    protected abstract Option getDefaultSelection();

    /**
     * Abstract method to set List of all Options on the Menu
     */
    protected abstract void setupOptionList();

    /**
     * Abstract method to set EnumMap of all SubOptions on the Menu
     */
    protected abstract void setupSubOptionList();

    /**
     * Method to setup SubOptionSelection EnumMap and currentSubSelection
     */
    private void setupSubOptionSelectionList() {
        for (Map.Entry<Option, List<SubOption>> optionListEntry : subOptionList.entrySet()) {
            subOptionSectionList.put(optionListEntry.getKey(), optionListEntry.getValue().get(0));
        }
        if (!subOptionList.isEmpty()) {
            currentSubSelection = subOptionList.get(currentSelection).get(0);
        }
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    protected SubOption getCurrentSubSelection() {
        return currentSubSelection;
    }

    protected boolean isToggleLogic() {
        return toggleLogic;
    }

    protected Map<Option, List<SubOption>> getSubOptionList() {
        return subOptionList;
    }

    protected List<Option> getOptionList() {
        return optionList;
    }

    protected float getScaledWidth() {
        return scaledWidth;
    }

    protected float getScaledHeight() {
        return scaledHeight;
    }

    protected float getAlpha() {
        return alpha;
    }

    protected float getWidth() {
        return width;
    }

    protected float getHeight() {
        return height;
    }

    /**
     * Method to rest selections for a Menu
     */
    protected void reset() {
        currentSelection = getDefaultSelection();
        selectionCount = 0;
        subSelectionCount = 0;
    }

    /**
     * Method to create and update a TextGraphics with specific parameters
     * @param text          the text to display
     * @param fontSize      the size of the text
     * @param centerXOffset the x offset from the center
     * @param centerYOffset the y offset from the center
     * @return a new TextGraphics
     */
    protected TextGraphics updateText(String text, float fontSize, float centerXOffset, float centerYOffset) {
        TextGraphics option = new TextGraphics(text, fontSize, Color.WHITE, Color.WHITE, 0.0f, false, false,
                                               anchor.add(((width / 2) - text.length() * fontSize / 2) +
                                                                  centerXOffset,
                                                          (height / 2) + centerYOffset));
        option.setFontName(FONT);
        option.setDepth(DEPTH);
        option.setAlpha(alpha);
        return option;
    }

    /**
     * Method to create and update an ImageGraphics with specific parameters
     * @param path the pathname to the image
     * @return a new ImageGraphics
     */
    protected ImageGraphics updateImage(String path) {
        return new ImageGraphics(ResourcePath.getBackgrounds(path), scaledWidth, scaledHeight,
                                 new RegionOfInterest(0, 0, 1100, 1100), anchor.add(
                new Vector((width / 2) - (scaledWidth / 2), (height / 2) - (scaledHeight / 2))), alpha, DEPTH + 250);
    }

    /**
     * Method to replace Option text with selecting brackets if it is selected
     * @param option the specified option that can be selected
     * @return String of the Option text
     */
    protected String getOptionText(Option option) {
        if (option.equals(currentSelection)) {
            return "[" + option.text + "]";
        } else {
            return option.text;
        }
    }

    /**
     * Method to replace add to Option text user input
     * @param option    the specified option that can be selected
     * @param userInput the user input text
     * @return String of the Option text
     */
    protected String getUserInputOptionText(Option option, String userInput) {
        if (option.equals(currentSelection)) {
            return "[" + option.text + ": " + userInput + "]";
        } else {
            return option.text + ": " + userInput;
        }
    }

    /**
     * Method to get user input from keyboard, including all letters and Backspace key
     * @param modifiableOption the Option where user input can be added
     * @param userInput        the user input text
     * @return userInput with new or less char depending on the user input
     */
    protected String getUserTextInput(Option modifiableOption, String userInput) {
        char keyInput = 0;
        // Check for the letters of the alphabet
        for (int key = 65; key <= 90; ++key) {
            if (keyboard.get(key).isPressed()) {
                if (userInput.length() < 8) {
                    keyInput = (char) key;
                    KEY_CLICK_SOUND.shouldBeStarted();
                } else {
                    ERROR_SOUND.shouldBeStarted();
                }
            }
            // Check for the backspace key
            if (keyboard.get(Keyboard.BACKSPACE).isPressed()) {
                if (!userInput.isEmpty()) {
                    KEY_CLICK_SOUND.shouldBeStarted();
                    return userInput.substring(0, userInput.length() - 1);
                } else {
                    ERROR_SOUND.shouldBeStarted();
                }
            }
        }
        if (keyInput != 0 && getCurrentSelection().equals(modifiableOption)) {
            return userInput + keyInput;
        } else {
            return userInput;
        }
    }

    protected Option getCurrentSelection() {
        return currentSelection;
    }

    protected String getSubOptionText(Option option) {
        if (option.equals(currentSelection)) {
            return "[" + subOptionSectionList.get(option).text + "]";
        } else {
            return subOptionSectionList.get(option).text;
        }
    }

    /**
     * Method to update subSelection by looping in SubOptionList for a specific Option
     */
    protected void updateSubSelection() {
        ++subSelectionCount;
        subSelectionCount = subSelectionCount >= subOptionList.get(currentSelection).size() ? 0 : subSelectionCount;
        currentSubSelection = subOptionList.get(currentSelection).get(subSelectionCount);
        subOptionSectionList.replace(currentSelection, currentSubSelection);
        setLogic();

    }

    /**
     * Method to set logic (acts like a switch)
     */
    private void setLogic() {
        if (currentSubSelection.equals(SubOption.TOGGLE_ON)) {
            toggleLogic = true;
        } else if (currentSubSelection.equals(SubOption.TOGGLE_OFF)) {
            toggleLogic = false;
        }
    }

    @Override
    public void bip(Audio audio) {
        SELECT_SOUND.bip(audio);
        KEY_CLICK_SOUND.bip(audio);
        ERROR_SOUND.bip(audio);
    }

    @Override
    public void draw(Canvas canvas) {
        scaledWidth = canvas.getScaledWidth();
        scaledHeight = canvas.getScaledHeight();
        width = canvas.getWidth();
        height = canvas.getHeight();

        anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        updateCurrentSelection();
    }

    /**
     * Method to update currentSelection with user UP and DOWN arrow keys
     */
    protected void updateCurrentSelection() {
        if (downKeyIsPressed() && selectionCount < optionList.size() - 1) {
            requestPlaySound(SELECT_SOUND);
            ++selectionCount;
            currentSelection = optionList.get(selectionCount);
            resetSubCount();

        } else if (upKeyIsPressed() && selectionCount > 0) {
            requestPlaySound(SELECT_SOUND);
            --selectionCount;
            currentSelection = optionList.get(selectionCount);
            resetSubCount();
        }
    }

    /**
     * Method to check if DOWN key is pressed
     * @return (true) if the key is pressed
     */
    protected boolean downKeyIsPressed() {
        return keyboard.get(Keyboard.DOWN).isPressed();
    }

    /**
     * Method to start sounds
     * @param sound the desired sound to be started
     */
    private void requestPlaySound(SoundAcoustics sound) {
        if (!MenuItems.isSoundDeactivated()) {
            sound.shouldBeStarted();
        }
    }

    /**
     * Method to reset subSelectionCount when exiting an Option
     */
    private void resetSubCount() {
        if (!subOptionSectionList.isEmpty() && !subOptionList.isEmpty() && currentSelection != null &&
                subOptionSectionList.containsKey(currentSelection) && subOptionList.containsKey(currentSelection)) {

            SubOption target = subOptionSectionList.get(currentSelection);
            SubOption[] subOptions = subOptionList.get(currentSelection).toArray(new SubOption[0]);

            boolean exit = false;
            for (int i = 0; i < subOptions.length && !exit; ++i) {
                if (target.equals(subOptions[i])) {
                    subSelectionCount = i;
                    exit = true;
                }
            }
        }
    }

    /**
     * Method to check if UP key is pressed
     * @return (true) if the key is pressed
     */
    protected boolean upKeyIsPressed() {
        return keyboard.get(Keyboard.UP).isPressed();
    }

}
