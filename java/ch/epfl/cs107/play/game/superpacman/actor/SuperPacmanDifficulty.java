package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.superpacman.menus.SubOption;

public enum SuperPacmanDifficulty {
    EASY(SubOption.DIFFICULTY_EASY, false, 20, 20, 15, 15, 4,
         5),
    NORMAL(SubOption.DIFFICULTY_NORMAL, true, 16, 12, 10, 10, 0,
           3),
    HARD(SubOption.DIFFICULTY_HARD, true, 14, 10, 8, 6, 2.5f,
         1.5f),
    IMPOSSIBLE(SubOption.DIFFICULTY_IMPOSSIBLE, true, 12, 8, 6, 4, 1.5f,
               1);

    final SubOption subOption;
    final boolean increaseDifficultyOverTime;
    final int ghostAnimationDuration;
    final int minGhostAnimationDuration;
    final float ghostFrightenTime;
    final float minGhostFrightenTime;
    final float ghostStateUpdateTime;
    final float minGhostStateUpdateTime;

    SuperPacmanDifficulty(SubOption subOption, boolean increaseDifficultyOverTime, int ghostAnimationDuration,
                          int minGhostAnimationDuration, float ghostFrightenTime, float minGhostFrightenTime,
                          float ghostStateUpdateTime, float minGhostStateUpdateTime) {
        this.subOption = subOption;
        this.increaseDifficultyOverTime = increaseDifficultyOverTime;
        this.ghostAnimationDuration = ghostAnimationDuration;
        this.minGhostAnimationDuration = minGhostAnimationDuration;
        this.ghostFrightenTime = ghostFrightenTime;
        this.minGhostFrightenTime = minGhostFrightenTime;
        this.ghostStateUpdateTime = ghostStateUpdateTime;
        this.minGhostStateUpdateTime = minGhostStateUpdateTime;
    }

    public static SuperPacmanDifficulty getDifficulty(SubOption selectedSubOption) {
        for (SuperPacmanDifficulty difficulty : SuperPacmanDifficulty.values()) {
            if (difficulty.subOption.equals(selectedSubOption)) {
                return difficulty;
            }
        }
        // Not found, return default difficulty
        return NORMAL;
    }
}
