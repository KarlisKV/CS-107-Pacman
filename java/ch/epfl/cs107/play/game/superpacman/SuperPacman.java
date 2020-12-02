/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:41
 */


package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.actor.Arcade;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level0;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level1;
import ch.epfl.cs107.play.game.superpacman.area.levels.Level2;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.transitions.EaseInOutCubic;
import ch.epfl.cs107.play.math.transitions.Transition;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {
    public static final float INIT_CAMERA_SCALE_FACTOR = 120.0f;
    public static final float FIN_CAMERA_SCALE_FACTOR = 37.0f;
    public static float currentCameraScaleFactor = INIT_CAMERA_SCALE_FACTOR;
    private final String[] areas =
            {"superpacman/level0", "superpacman/level1", "superpacman/level2"};
    private final Transition transition = new EaseInOutCubic(0.01f);
    private Arcade arcade;

    private int areaIndex;
    private boolean startGame = false;
    private float timer = 0;
    private SuperPacmanPlayer player;

    /* ----------------------------------- ACCESSORS ----------------------------------- */

    @Override
    public String getTitle() {
        return "Super Pac-mac";
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
        arcade.fadeTitle();
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
            arcade = new Arcade(window, false, true, true);
            getCurrentArea().registerActor(arcade);

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
    }

    @Override
    public void update(float deltaTime) {
        if (!startGame) {
            Keyboard keyboard = getCurrentArea().getKeyboard();
            if (keyboard.get(Keyboard.SPACE).isDown()) {
                setStartGame(true);
            }
        } else {
            updateGame(deltaTime);
        }

        super.update(deltaTime);
    }

    private void updateGame(float deltaTime) {
        // TODO: find better alfternative
        timer += deltaTime;
        if (timer > 2) {
            if (!arcade.isArcadeTurnedOn()) {
                arcade.setArcadeTurnedOn(true);
            }
            if (timer > 3 && currentCameraScaleFactor > FIN_CAMERA_SCALE_FACTOR && !transition.isFinished()) {
                float newProgress = transition.getProgress();
                currentCameraScaleFactor = FIN_CAMERA_SCALE_FACTOR +
                        ((INIT_CAMERA_SCALE_FACTOR - FIN_CAMERA_SCALE_FACTOR) * (1 - newProgress));
            } else if (transition.isFinished() && !player.canUserMove()) {
                player.setCanUserMove(true);
            }
        }
    }

    @Override
    public void end() {
    }


}
