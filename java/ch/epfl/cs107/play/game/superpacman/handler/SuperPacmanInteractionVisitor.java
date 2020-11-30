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

    /**
     * Default interaction between something and the player
     * @param player (SuperPacmanPlayer)
     */
    default void interactWith(SuperPacmanPlayer player) {
        // by default empty
    }

    /**
     * Default interaction between something and a Ghost
     * @param ghost (Ghost)
     */
    default void interactWith(Ghost ghost) {
        // by default empty
    }
}
