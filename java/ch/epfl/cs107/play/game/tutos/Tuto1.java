/*
 *	Author:      Leonard Cseres
 *	Date:        14.11.20
 *	Time:        19:23
 */


package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto1 extends AreaGame {
    private SimpleGhost player;
    private SimpleGhost simpleGhost;

    private void createArea(Area area) {
        addArea(area);
    }

    public void switchArea() {
        player.strengthen();
        if (this.getCurrentArea().getTitle().equals("zelda/Ferme")) {
            setCurrentArea("zelda/Village", true);
            this.getCurrentArea().registerActor(simpleGhost);
        } else {
            this.getCurrentArea().unregisterActor(simpleGhost);
            setCurrentArea("zelda/Ferme", true);
        }
        this.getCurrentArea().registerActor(player);
        this.getCurrentArea().setViewCandidate(player);
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        // traitement spécifiques à Tuto1

        // Actors
        player = new SimpleGhost(new Vector(18, 7), "ghost.1");
        simpleGhost = new SimpleGhost(new Vector(18, 7), "ghost.2");

        if (super.begin(window, fileSystem)) {
            createArea(new Village());
            createArea(new Ferme());
            setCurrentArea("zelda/Ferme", true);
            this.getCurrentArea().registerActor(player);
            this.getCurrentArea().setViewCandidate(player);
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getWindow().getKeyboard();
        Button key = keyboard.get(Keyboard.UP);
        if (key.isDown()) {
            player.moveUp();
        }
        key = keyboard.get(Keyboard.DOWN);
        if (key.isDown()) {
            player.moveDown();
        }
        key = keyboard.get(Keyboard.LEFT);
        if (key.isDown()) {
            player.moveLeft();
        }
        key = keyboard.get(Keyboard.RIGHT);
        if (key.isDown()) {
            player.moveRight();
        }

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
        return "Tuto1";
    }

}
