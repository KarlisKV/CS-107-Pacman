/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:43
 */


package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class SuperPacmanArea extends Area {
    private static List<Ghost> ghosts = new ArrayList<>();

    private SuperPacmanAreaBehavior behavior;

    // TODO: find better way...
    public static List<Ghost> getGhosts() {
        return ghosts;
    }

    public static void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    public static void clearGhosts() {
        ghosts.clear();
    }

    public void scareGhost() {
        behavior.scareGhosts();
    }

    public void resetGhosts() {
        behavior.resetGhosts();
    }

    @Override
    public float getCameraScaleFactor() {
        return SuperPacman.currentCameraScaleFactor;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new SuperPacmanAreaBehavior(window, getTitle());
            setBehavior(behavior);
            behavior.registerActors(this);
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
