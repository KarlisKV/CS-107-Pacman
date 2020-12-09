/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:41
 */


package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level0;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level1;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level2;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level3;
import ch.epfl.cs107.play.game.superpacman.graphics.Arcade;
import ch.epfl.cs107.play.game.superpacman.graphics.ScreenFade;
import ch.epfl.cs107.play.game.superpacman.leaderboard.GameScore;
import ch.epfl.cs107.play.game.superpacman.leaderboard.LeaderboardGameScores;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.Serialization;
import ch.epfl.cs107.play.math.transitions.EaseInOutCubic;
import ch.epfl.cs107.play.math.transitions.Transition;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {
    public static final float INIT_CAMERA_SCALE_FACTOR = 120.0f;
    public static final float FIN_CAMERA_SCALE_FACTOR = 37.0f;
    private static LeaderboardGameScores leaderboardGameScores;
    public static float currentCameraScaleFactor = INIT_CAMERA_SCALE_FACTOR;
    private final String[] areas =
            {"superpacman/level0", "superpacman/level1", "superpacman/level2"};
    private final Transition transition = new EaseInOutCubic(0.01f);
    private final SuperPacmanStatusGUI superPacmanStatusGUI = new SuperPacmanStatusGUI();
    private Arcade arcade;
    private int areaIndex;
    private float timer = 0;
    private boolean pauseTimer = false;
    private SuperPacmanPlayer player;
    private final ScreenFade screenFade = new ScreenFade(4000, 0.02f);

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public static LeaderboardGameScores getLeaderboardScores() {
        return leaderboardGameScores;
    }

    @Override
    public String getTitle() {
        return "Super Pac-mac";
    }

    @Override
    protected void initPlayer(Player player) {
        super.initPlayer(player);
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            Area area = setCurrentArea(areas[areaIndex], true);
            player = new SuperPacmanPlayer(area, Level0.PLAYER_SPAWN_POSITION);
            initPlayer(player);

            arcade = new Arcade(window);

            Object leaderboardScoresTemp = Serialization.deserialize("leaderboard.ser");
            if (leaderboardScoresTemp != null) {
                leaderboardGameScores = (LeaderboardGameScores) leaderboardScoresTemp;
            } else {
                leaderboardGameScores = new LeaderboardGameScores();
            }
            return true;
        }
        return false;
    }

    /**
     * Method to add Areas to the AreaGame
     */
    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
        addArea(new Level3());
    }

    @Override
    public void update(float deltaTime) {

        if (MenuStateManager.isStartGame()) {
            updateGame(deltaTime);
        }

        super.update(deltaTime);
        // update arcade
        arcade.draw(getWindow());
        arcade.bip(getWindow());
        // update game status GUI
        superPacmanStatusGUI.draw(getWindow());
        screenFade.draw(getWindow());
    }

    /**
     * Method to start, update and end the game
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    private void updateGame(float deltaTime) {
        if (!pauseTimer) {
            timer += deltaTime;
        }
        // START GAME
        if (!player.isGameOver() && !MenuStateManager.isEndGame()) {
            // Turn on arcade
            if (timer > 2) {
                if (!arcade.isArcadeTurnedOn()) {
                    arcade.setArcadeTurnedOn(true);
                }
                // Zoom in onto arcade
                if (timer > 3 && currentCameraScaleFactor > FIN_CAMERA_SCALE_FACTOR && !transition.isFinished()) {
                    float newProgress = transition.getProgress();
                    currentCameraScaleFactor = FIN_CAMERA_SCALE_FACTOR +
                            ((INIT_CAMERA_SCALE_FACTOR - FIN_CAMERA_SCALE_FACTOR) * (1 - newProgress));
                } else if (transition.isFinished() && !player.canUserMove()) {
                    // Start player
                    SuperPacmanPlayer.getPlayerSoundUtility().setAudioPaused(false);
                    player.setCanUserMove(true);
                    timer = 0;
                    transition.reset();
                    pauseTimer = true;
                }
            }
        } else {
            // GAME OVER
            // Fade out
            screenFade.setFadeOut();
            // Zoom out
            if (currentCameraScaleFactor < INIT_CAMERA_SCALE_FACTOR && !transition.isFinished()) {
                float newProgress = transition.getProgress();
                currentCameraScaleFactor = INIT_CAMERA_SCALE_FACTOR +
                        ((FIN_CAMERA_SCALE_FACTOR - INIT_CAMERA_SCALE_FACTOR) * (1 - newProgress));
            } else if (transition.isFinished()) {

                // Reset all
                arcade.setAlpha(1);
                arcade.setArcadeTurnedOn(false);
                MenuStateManager.setStartGame(false);
                timer = 0;
                transition.reset();
                if (MenuStateManager.isEndGame()) {
                    player.reset();
                    MenuStateManager.setEndGame(false);
                } else {
                    // Save leaderboard to file
                    leaderboardGameScores
                            .add(new GameScore(SuperPacmanPlayer.getMaxHp(), player.getAreaTimerHistory(),
                                               player.getScore(),
                                               player.getCurrentHp()));
                    MenuStateManager.setGameOver(true);
                }
                player.restart();
                player.leaveArea();
                Area area = setCurrentArea(areas[0], true);
                player.enterArea(area, Level0.PLAYER_SPAWN_POSITION);

                pauseTimer = false;
            }

        }
    }

    @Override
    public void end() {
    }

}