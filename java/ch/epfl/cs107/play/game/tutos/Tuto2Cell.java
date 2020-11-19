/*
 *	Author:      Leonard Cseres
 *	Date:        15.11.20
 *	Time:        23:22
 */


package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;

class Tuto2Cell extends Cell {
    private final Tuto2Behavior.Tuto2CellType type;

    public Tuto2Cell(int x, int y, Tuto2Behavior.Tuto2CellType type) {
        super(x, y);
        this.type = type;
    }

    @Override
    protected boolean canLeave(Interactable entity) {
        return false;
    }

    @Override
    protected boolean canEnter(Interactable entity) {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }

    public Tuto2Behavior.Tuto2CellType getType() {
        return type;
    }

    // Autres méthodes
}