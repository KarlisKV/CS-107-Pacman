/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        20:23
 */


package ch.epfl.cs107.play.game.superpacman.actor.ghosts;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanDifficulty;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;

import java.util.ArrayList;
import java.util.List;

public class GhostsBehavior implements Updatable {
    private static final int ANIMATION_DURATION_DECREASE = -1;
    private static final float FRIGHTEN_TIME_DECREASE = -0.5f;
    private static final float STATE_UPDATE_TIME_DECREASE = -0.25f;
    private final List<Ghost> ghosts = new ArrayList<>();
    private final float timeToIncreaseDifficulty = 30;
    private final SuperPacmanDifficulty difficulty;
    private boolean isDifficultSet = false;
    private int increaseCount = 0;
    private float timer = 0;
    private boolean updateTimer = true;
    private boolean requestToFrighten = false;

    public GhostsBehavior(SuperPacmanDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Method to add new ghosts to the current List
     * @param ghost a new ghost
     */
    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    @Override
    public void update(float deltaTime) {
        // Set initial difficulty settings
        if (!isDifficultSet) {
            setGhostDifficulty();
            isDifficultSet = true;
        }
        // Increase difficulty over time
        if (difficulty.increaseDifficultyOverTime && updateTimer) {
            updateTimer(deltaTime);
        }
        if (requestToFrighten && !ghosts.isEmpty()) {
            for (Ghost ghost : ghosts) {
                if (ghost.isEaten() && ghost.reachedHome()) {
                    boolean playPowerPelletSound = true;
                    for (Ghost otherGhost : ghosts) {
                        if (!otherGhost.equals(ghost) && otherGhost.isEaten()) {
                            playPowerPelletSound = false;
                            break;
                        }
                    }
                    // Play power pellet if ghosts are still frightened and all got home
                    if (playPowerPelletSound) {
                        Ghost.getGhostSoundManager().stopAll();
                        SuperPacmanPlayer.getPlayerSoundUtility().play(SuperPacmanPlayer.POWER_PELLET_SOUND, true);
                    }
                }
            }
            if (areGhostsNotFrightened()) {
                SuperPacmanPlayer.resetComboCount();
                // Play siren sound if ghosts not anymore frightened
                if (!SuperPacmanPlayer.isDead()) {
                    Ghost.getGhostSoundManager().stopAll();
                    SuperPacmanPlayer.getPlayerSoundUtility().play(SuperPacmanPlayer.SIREN_SOUND, true);
                }
                requestToFrighten = false;
            }
        }

    }

    /**
     * Method to set ghosts difficulty, and increase ghosts difficulty
     */
    public void setGhostDifficulty() {
        updateTimer = false;
        for (Ghost ghost : ghosts) {
            int animationDuration =
                    difficulty.ghostAnimationDuration + (ANIMATION_DURATION_DECREASE * increaseCount);
            if (animationDuration >= difficulty.minGhostAnimationDuration) {
                ghost.setAnimationDuration(animationDuration);
                updateTimer = true;
            }
            float frightenTime = difficulty.ghostFrightenTime + (FRIGHTEN_TIME_DECREASE * increaseCount);
            if (frightenTime >= difficulty.minGhostFrightenTime) {
                ghost.setFrightenTime(frightenTime);
                updateTimer = true;
            }
            float stateUpdateTime = difficulty.ghostStateUpdateTime + (STATE_UPDATE_TIME_DECREASE * increaseCount);
            if (stateUpdateTime >= difficulty.minGhostStateUpdateTime) {
                ghost.setStateUpdateTime(stateUpdateTime);
                updateTimer = true;
            }
        }
    }

    /**
     * Method to time and update the ghosts difficulty
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    private void updateTimer(float deltaTime) {
        timer += deltaTime;
        if (timer >= timeToIncreaseDifficulty) {
            ++increaseCount;
            setGhostDifficulty();
            timer = 0;
        }
    }

    /**
     * Method the check whether the ghosts are not anymore frightened
     * @return (true) if all the ghosts are not frightened
     */
    public boolean areGhostsNotFrightened() {
        int count = 0;
        for (Ghost ghost : ghosts) {
            if (!ghost.isFrightened()) {
                ++count;
            }
        }
        return count == ghosts.size();
    }

    /**
     * Method to frighten all the ghosts
     */
    public void frightenGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.setFrightened(true);
        }
        requestToFrighten = true;

    }

    /**
     * Method to reset all the ghosts
     */
    public void resetGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    /**
     * Method to pause all the ghosts
     */
    public void pauseGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.setGameOver();
        }
    }
}
