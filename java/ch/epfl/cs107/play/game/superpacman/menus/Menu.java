/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        04:05
 */


package ch.epfl.cs107.play.game.superpacman.menus;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.*;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanSound;
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
    protected static final float HEADER_FONT_SIZE = 5;
    protected static final float BODY_FONT_SIZE = 2;
    protected static final String FONT = "emulogic";
    protected static final float DEPTH = 20000;
    private static final SoundAcoustics SELECT_SOUND = SuperPacmanSound.MENU_SELECT.sound;
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

    public Menu(Window window) {
        keyboard = window.getKeyboard();
        currentSelection = getDefaultSelection();
        setupOptionList();
        setupSubOptionList();
        setupSubOptionSelectionList();
    }

    protected abstract Option getDefaultSelection();

    protected abstract void setupOptionList();

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    protected abstract void setupSubOptionList();

    private void setupSubOptionSelectionList() {
        for (Map.Entry<Option, List<SubOption>> optionListEntry : subOptionList.entrySet()) {
            subOptionSectionList.put(optionListEntry.getKey(), optionListEntry.getValue().get(0));
        }
        if (!subOptionList.isEmpty()) {
            currentSubSelection = subOptionList.get(currentSelection).get(0);
        }
    }

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

    protected Option getCurrentSelection() {
        return currentSelection;
    }

    protected void setCurrentSelection(Option currentSelection) {
        this.currentSelection = currentSelection;
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

    protected void reset() {
        currentSelection = getDefaultSelection();
        selectionCount = 0;
        subSelectionCount = 0;
    }

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

    protected ImageGraphics updateImage(String path) {
        return new ImageGraphics(ResourcePath.getBackgrounds(path), scaledWidth, scaledHeight,
                                 new RegionOfInterest(0, 0, 1100, 1100), anchor.add(
                new Vector((width / 2) - (scaledWidth / 2), (height / 2) - (scaledHeight / 2))), alpha, DEPTH + 250);
    }

    protected String getOptionText(Option option) {
        if (option.equals(currentSelection)) {
            return "[" + option.text + "]";
        } else {
            return option.text;
        }
    }

    protected String getSubOptionText(Option option) {
        if (option.equals(currentSelection)) {
            return "[" + subOptionSectionList.get(option).text + "]";
        } else {
            return subOptionSectionList.get(option).text;
        }
    }

    protected void updateSubSelection() {
        ++subSelectionCount;
        subSelectionCount = subSelectionCount >= subOptionList.get(currentSelection).size() ? 0 : subSelectionCount;
        currentSubSelection = subOptionList.get(currentSelection).get(subSelectionCount);
        subOptionSectionList.replace(currentSelection, currentSubSelection);
        setLogic();

    }

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

    protected boolean downKeyIsPressed() {
        return keyboard.get(Keyboard.DOWN).isPressed();
    }

    private void requestPlaySound(SoundAcoustics sound) {
        if (!MenuItems.isSoundDeactivated()) {
            sound.shouldBeStarted();
        }
    }

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

    protected boolean upKeyIsPressed() {
        return keyboard.get(Keyboard.UP).isPressed();
    }

}
