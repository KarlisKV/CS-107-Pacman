/*
 *	Author:      Leonard Cseres
 *	Date:        26.11.20
 *	Time:        21:15
 */


package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;

public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor {

    default void interactWith(SuperPacmanPlayer player) {
        // TODO: later modify with actions with player interacts
    }

    default void interactWith(Ghost ghost) {
    }
}
