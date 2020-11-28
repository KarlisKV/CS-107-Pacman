/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:43
 */


package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public abstract class SuperPacmanArea extends Area {
    private SuperPacmanBehavior behavior;

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);
            behavior.registerActors(this);
            createArea();
            return true;
        }
        return false;
    }

    protected abstract void createArea();

    @Override
    public float getCameraScaleFactor() {
        return SuperPacman.CAMERA_SCALE_FACTOR;
    }

}
