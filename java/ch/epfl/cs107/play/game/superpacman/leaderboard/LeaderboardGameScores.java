/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        10:51
 */


package ch.epfl.cs107.play.game.superpacman.leaderboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardGameScores implements Serializable {
    private final List<GameScore> gameScores = new ArrayList<>();

    /**
     * Method to sort List<GameScore> by score
     * @return a sorted List
     */
    public List<GameScore> getSortedGameScores() {
        gameScores.sort(Comparator.comparingInt(GameScore::getScore));
        Collections.reverse(gameScores);
        return gameScores;
    }

    /**
     * Method to add a new GameScore to the leaderboard
     * @param gameScore the GameScore to add
     */
    public void add(GameScore gameScore) {
        gameScores.add(gameScore);
    }

    /**
     * Method to get the last game saved into the List<GameScore>
     * @return the last game
     */
    public GameScore getLastGame() {
        return gameScores.get(gameScores.size() - 1);
    }

    /**
     * Method to clear the List of GameScore
     */
    public void clear() {
        gameScores.clear();
    }
}
