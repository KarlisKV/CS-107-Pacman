/*
 *	Author:      Leonard Cseres
 *	Date:        15.11.20
 *	Time:        23:04
 */


package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class Tuto2Behavior extends AreaBehavior {

    public Tuto2Behavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Tuto2CellType color = Tuto2CellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new Tuto2Cell(x, y, color));
            }
        }
    }

    public enum Tuto2CellType {
        NULL(0, false),
        WALL(-16777216, false),         // #000000 RGB code of black
        IMPASSABLE(-8750470, false),    // #7A7A7A, RGB color of grey
        INTERACT(-256, true),           // #FFFF00, RGB color of yellow
        DOOR(-195580, true),            // #FD0404, RGB color of red
        WALKABLE(-1, true);             // #FFFFFF, RGB color of white
        final int type;
        final boolean isWalkable;

        Tuto2CellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        static Tuto2CellType toType(int type) {
            for (Tuto2CellType cellType : Tuto2CellType.values()) {
                if (type == cellType.type) {
                    return cellType;
                }
            }
            System.out.println(type);
            return NULL;
        }
    }

    public class Tuto2Cell extends AreaBehavior.Cell {
        private final Tuto2Behavior.Tuto2CellType type;

        public Tuto2Cell(int x, int y, Tuto2Behavior.Tuto2CellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return type.isWalkable;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
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
}