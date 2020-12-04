/*
 *	Author:      Leonard Cseres
 *	Date:        03.12.20
 *	Time:        20:23
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.Updatable;

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

    public GhostsBehavior(SuperPacmanDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    @Override
    public void update(float deltaTime) {
        if (!isDifficultSet) {
            setGhostDifficulty();
            isDifficultSet = true;
        }
        if (difficulty.increaseDifficultyOverTime && updateTimer) {
            updateTimer(deltaTime);
            // Set more difficult
        }
        for (Ghost ghost : ghosts) {
            if (ghost.isEaten() && ghost.reachedHome()) {
//                ghost.setEaten(false);
                SuperPacmanPlayer.setStopAllAudio();
                if (!areGhostsFrightened()) {
                    SuperPacmanPlayer.SIREN_SOUND.shouldBeStarted();
                }
            }
        }

    }

    public void setGhostDifficulty() {
        updateTimer = false;
        for (Ghost ghost : ghosts) {
            int animationDuration = difficulty.ghostAnimationDuration + (ANIMATION_DURATION_DECREASE * increaseCount);
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

    private void updateTimer(float deltaTime) {
        timer += deltaTime;
        if (timer >= timeToIncreaseDifficulty) {
            ++increaseCount;
            setGhostDifficulty();
            timer = 0;
        }
    }

    private boolean areGhostsFrightened() {
        int count = 0;
        for (Ghost ghost : ghosts) {
            if (ghost.isFrightened()) {
                ++count;
            }
        }
        return count > 2;
    }

    public void frightenGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.setFrightened(true);
        }
    }

    public void resetGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }
}
