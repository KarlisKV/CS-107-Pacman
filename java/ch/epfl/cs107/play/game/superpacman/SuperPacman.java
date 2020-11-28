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
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {
    public static final float CAMERA_SCALE_FACTOR = 30.f;
    private final String[] areas = {"superpacman/level0", "superpacman/level1", "superpacman/level2"};
    private SuperPacmanPlayer player;
    private int areaIndex;
    private int count = 0;


    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
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
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        ++count;
        if (count % 200 == 0) {
            getCurrentArea().getCamera().shake();
        }
        super.update(deltaTime);

    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
        return "Super Pac-mac";
    }
}
