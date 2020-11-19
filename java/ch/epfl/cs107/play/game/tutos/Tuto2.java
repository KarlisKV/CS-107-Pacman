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
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
    private GhostPlayer player;
    public static final float SCALE_FACTOR = 13.f;

    private void createArea(Area area) {
        addArea(area);
    }

    public void switchArea() {
        player.strengthen();
        player.leaveArea(getCurrentArea(), new DiscreteCoordinates(2, 10));

        if (this.getCurrentArea().getTitle().equals("zelda/Ferme")) {
            setCurrentArea("zelda/Village", false);
            player = new GhostPlayer(this.getCurrentArea(), Orientation.DOWN, new DiscreteCoordinates(5, 15), "ghost.1");
        } else {
            setCurrentArea("zelda/Ferme", false);
        }
        player.enterArea(getCurrentArea(), new DiscreteCoordinates(2, 10));

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        // traitement spécifiques à Tuto1

        if (super.begin(window, fileSystem)) {
            createArea(new Village());
            createArea(new Ferme());
            setCurrentArea("zelda/Ferme", false);

            // Actors
            player = new GhostPlayer(getCurrentArea(), Orientation.DOWN, new DiscreteCoordinates(2, 10), "ghost.1");

            player.enterArea(getCurrentArea(), new DiscreteCoordinates(2, 10));
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (player.isWeak()) {
            switchArea();
        }
    }

    @Override
    public void end() {
        super.end();
    }


    @Override
    public String getTitle() {
        return "Tuto2";
    }
}
