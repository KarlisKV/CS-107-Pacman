/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        10:51
 */


package ch.epfl.cs107.play.game.superpacman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardScores implements Serializable {
    private final List<GameScore> gameScores = new ArrayList<>();

    public List<GameScore> getSortedGameScores() {
        gameScores.sort(Comparator.comparingInt(GameScore::getScore));
        Collections.reverse(gameScores);
        return gameScores;
    }

    public void add(GameScore gameScore) {
        gameScores.add(gameScore);
    }

    public GameScore getLastGame() {
        return gameScores.get(gameScores.size() - 1);
    }

    public void clear() {
        gameScores.clear();
    }
}
