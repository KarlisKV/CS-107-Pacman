/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        08:23
 */


package ch.epfl.cs107.play.game.superpacman.area.menus;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.superpacman.area.MenuArea;

public class MainMenu extends MenuArea {

    @Override
    protected void createArea() {
        registerActor(new Background(this));
    }

    @Override
    public String getTitle() {
        return "superpacman/pacmanTitleMenu";
    }
}
