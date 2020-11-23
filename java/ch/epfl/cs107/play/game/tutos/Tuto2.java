/*
 *	Author:      Leonard Cseres
 *	Date:        14.11.20
 *	Time:        19:23
 */


package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Mine;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 18.f;
    public final static float STEP = 0.05f;
    private final String[] areas = {"mine/MineTest", "zelda/Village"};
    private final DiscreteCoordinates[] startingPositions = {new DiscreteCoordinates(3, 29),
                                                             new DiscreteCoordinates(5, 15)};
    private GhostPlayer player;
    private int areaIndex;

    /**
     * Add all the areas
     */
    private void createAreas() {
        addArea(new Mine());
        addArea(new Village());
    }


    public void switchArea() {
        player.leaveArea();

        areaIndex = (areaIndex == 0) ? 1 : 0;

        Area currentArea = setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, startingPositions[areaIndex]);

        player.strengthen();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 1;
            Area area = setCurrentArea(areas[areaIndex], true);
            player = new GhostPlayer(area, Orientation.DOWN, startingPositions[areaIndex], "ghost.1");
            area.registerActor(player);
            area.setViewCandidate(player);
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {

        if (player.isWeak()) {
            switchArea();
        }
        super.update(deltaTime);
    }

    @Override
    public void end() {
    }


    @Override
    public String getTitle() {
        return "Tuto2";
    }
}
