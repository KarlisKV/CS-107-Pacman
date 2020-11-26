/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:45
 */


package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacmanBehavior extends AreaBehavior {
    private int cellX = 0;
    private int cellY = 0;

    /**
     * Default SuperPacmanBehavior Constructor
     * @param window (Window), not null
     * @param name   (String): Name of the Behavior, not null
     */
    public SuperPacmanBehavior(Window window, String name) {
        super(window, name);
        do {
            SuperPacmanBehavior.SuperPacmanCellType color =
                    SuperPacmanBehavior.SuperPacmanCellType.toType(getRGB(getHeight() - 1 - cellY, cellX));
            setCell(cellX, cellY, new SuperPacmanBehavior.SuperPacmanCell(cellX, cellY, color));
        } while (canIterateCells());
    }

    /**
     * Method to register all of the cell related actors.
     * @param area the area to register the actors
     */
    protected void registerActors(Area area) {
        do {
            if (compareCellToType(cellX, cellY, SuperPacmanCellType.WALL)) {
                Wall wall = new Wall(area, new DiscreteCoordinates(cellX, cellY), neighborhood(cellX, cellY));
                area.registerActor(wall);
            }
        } while (canIterateCells());
    }

    /**
     * Method to iterate over all of the cells.
     * @return false if loop is finished
     */
    private boolean canIterateCells() {
        if (cellY < getHeight()) {
            if (cellX < getWidth()) {
                ++cellX;
            } else {
                cellX = 0;
                ++cellY;
            }
        } else {
            cellX = 0;
            cellY = 0;
            return false;
        }
        return true;
    }

    /**
     * Method to find if neighborhood cells are walls.
     * @param x coordinate of the cell
     * @param y coordinate of the cell
     * @return 3x3 array with the true for surrounding walls
     */
    private boolean[][] neighborhood(int x, int y) {
        boolean[][] neighbors = new boolean[3][3];

        for (int j = y - 1; j <= y + 1 && j <= 0 && j < getHeight(); ++j) {
            for (int i = x - 1; i <= x + 1 && i <= 0; ++i) {
                if (compareCellToType(i, j, SuperPacmanCellType.WALL)) {
                    neighbors[i][j] = true;
                }
            }
        }
        return neighbors;
    }

    /**
     * Method to check if cell type matches.
     * @param x        coordinate of the cell
     * @param y        coordinate of the cell
     * @param cellType the type of the cell wanted
     * @return true if the current cell matches cellType
     */
    private boolean compareCellToType(int x, int y, SuperPacmanCellType cellType) {
        return ((SuperPacmanCell) getCell(x, y)).type == cellType;
    }

    public enum SuperPacmanCellType {
        NONE(0), // never used as real content
        WALL(-16777216), //black
        FREE_WITH_DIAMOND(-1), //white
        FREE_WITH_BLINKY(-65536), //red
        FREE_WITH_PINKY(-157237), //pink
        FREE_WITH_INKY(-16724737), //cyan
        FREE_WITH_CHERRY(-36752), //light red
        FREE_WITH_BONUS(-16478723), //light blue
        FREE_EMPTY(-6118750); // sort of gray

        final int type;

        SuperPacmanCellType(int type) {
            this.type = type;
        }

        public static SuperPacmanBehavior.SuperPacmanCellType toType(int type) {
            for (SuperPacmanBehavior.SuperPacmanCellType ict : SuperPacmanBehavior.SuperPacmanCellType.values()) {
                if (ict.type == type) {
                    return ict;
                }
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;
        }
    }

    /**
     * Cell adapted to the SuperPacman game
     */
    public class SuperPacmanCell extends AreaBehavior.Cell {
        /// Type of the cell following the enum
        private final SuperPacmanBehavior.SuperPacmanCellType type;

        /**
         * Default SuperPacmanCell Constructor
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public SuperPacmanCell(int x, int y, SuperPacmanBehavior.SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return takeCellSpace();
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

    }
}
