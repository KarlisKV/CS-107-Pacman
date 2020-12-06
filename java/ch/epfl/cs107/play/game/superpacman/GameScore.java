/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        09:55
 */


package ch.epfl.cs107.play.game.superpacman;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GameScore implements Serializable {
    private final int maxDeaths;
    private final HashMap<String, Float> areaHistoryTimes;
    private final int score;
    private final int deaths;
    private String playerName = "";
    private float totalTime;

    public GameScore(int maxDeaths, Map<String, Float> areaHistoryTimes, int score, int currentHp) {
        this.maxDeaths = maxDeaths;
        for (Float areaHistoryTime : areaHistoryTimes.values()) {
            totalTime += areaHistoryTime;
        }
        this.areaHistoryTimes = new HashMap<>(areaHistoryTimes);
        this.score = score;
        this.deaths = maxDeaths - currentHp;
    }

    public Map<String, Float> getOrderedAreaHistoryTimes() {
        return new TreeMap<>(areaHistoryTimes);
    }

    public int getMaxDeaths() {
        return maxDeaths;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public float getTotalTime() {
        return totalTime;
    }


    public int getScore() {
        return score;
    }

    public int getDeaths() {
        return deaths;
    }
}
