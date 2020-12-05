/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:43
 */


package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.GhostsBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class SuperPacmanArea extends Area {
    private SuperPacmanAreaBehavior behavior;

    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    public GhostsBehavior getGhostsManagement() {
        return behavior.getGhostsManagement();
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
    public int getTotalPellets() {
        return behavior.getTotalPellets();
    }
    public int getEatenPellets() {
        return behavior.getEatenPellets();
    }
    public void setEatenPellets() {
        behavior.setEatenPellets();
    }
    /**
     * Abstract method to create and add actors to area
     */
    protected abstract void createArea();
}
