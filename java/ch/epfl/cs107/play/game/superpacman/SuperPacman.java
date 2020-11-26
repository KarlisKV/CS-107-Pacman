/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:41
 */


package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {
    public static final float CAMERA_SCALE_FACTOR = 15.f;
    private final String[] areas = {"zelda/Ferme", "zelda/Village"};
    private final DiscreteCoordinates[] startingPositions = {new DiscreteCoordinates(2, 10),
                                                             new DiscreteCoordinates(5, 15)};
    private GhostPlayer player;
    private int areaIndex;


    private void createAreas() {

        addArea(new Ferme());
        addArea(new Village());

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
            player = new GhostPlayer(area, Orientation.DOWN, startingPositions[areaIndex], "ghost.1");
            area.registerActor(player);
            area.setViewCandidate(player);
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {

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
