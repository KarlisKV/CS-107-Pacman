/*
 *	Author:      Leonard Cseres
 *	Date:        08.12.20
 *	Time:        14:11
 */


package ch.epfl.cs107.play.game.superpacman.menus.pages;

import ch.epfl.cs107.play.game.superpacman.menus.Menu;
import ch.epfl.cs107.play.game.superpacman.menus.Option;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class Quit extends Menu {

    /**
     * Constructor for Quit class
     * @param window (Window): the current window
     */
    public Quit(Window window) {
        super(window);
    }

    @Override
    protected Option getDefaultSelection() {
        // Empty on purpose, current menu tab does not contain any OptionsList
        return null;
    }

    @Override
    protected void setupOptionList() {
        // Empty on purpose, current menu tab does not contain any Options, so List is set to null
    }

    @Override
    protected void setupSubOptionList() {
        // Empty on purpose, current menu tab does not contain any subOptions, so List is set to null
    }

    @Override
    public void draw(Canvas canvas) {
        // Empty on purpose, draw nothing
    }
}