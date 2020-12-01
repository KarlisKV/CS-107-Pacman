/*
 *	Author:      Leonard Cseres
 *	Date:        26.11.20
 *	Time:        21:15
 */


package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.*;

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

    /**
     * Default interaction between something and a Bonus
     * @param bonus (Bonus)
     */
    default void interactWith(Bonus bonus) {
        // by default empty
    }

    /**
     * Default interaction between something and a Cherry
     * @param cherry (Cherry)
     */
    default void interactWith(Cherry cherry) {
        // by default empty
    }

    /**
     * Default interaction between something and a Diamond
     * @param diamond (Diamond)
     */
    default void interactWith(Diamond diamond) {
        // by default empty
    }

    /**
     * Default interaction between something and a Pellet
     * @param pellet (Pellet)
     */
    default void interactWith(Pellet pellet) {
        // by default empty
    }

    /**
     * Default interaction between something and a PowerPellet
     * @param powerPellet (PowerPellet)
     */
    default void interactWith(PowerPellet powerPellet) {
        // by default empty
    }
}
