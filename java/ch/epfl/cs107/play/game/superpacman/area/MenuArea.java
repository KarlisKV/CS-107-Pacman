/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:25
 */


package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public abstract class MenuArea extends Area {

    @Override
    public float getCameraScaleFactor() {
        return SuperPacman.currentCameraScaleFactor;
    }

    @Override
    public int getWidth() {
        return 0;

    }

    @Override
    public int getHeight() {
        return 0;

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createArea();
            return true;
        }
        return false;
    }

    /**
     * Abstract method to create and add actors to area
     */
    protected abstract void createArea();
}
