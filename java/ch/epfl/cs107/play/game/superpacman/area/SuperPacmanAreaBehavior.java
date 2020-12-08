/*
 *	Author:      Leonard Cseres
 *	Date:        25.11.20
 *	Time:        16:45
 */


package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanDifficulty;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Cake;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.Pellet;
import ch.epfl.cs107.play.game.superpacman.actor.collectables.PowerPellet;
import ch.epfl.cs107.play.game.superpacman.actor.ghosts.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacmanAreaBehavior extends AreaBehavior {
    public static AreaGraph areaGraph;
    public static SuperPacmanDifficulty initDifficulty = SuperPacmanDifficulty.NORMAL;
    private final GhostsBehavior ghostsBehavior = new GhostsBehavior(initDifficulty);

    /**
     * Default SuperPacmanBehavior Constructor
     * @param window (Window), not null
     * @param name   (String): Name of the Behavior, not null
     */
    public SuperPacmanAreaBehavior(Window window, String name) {
        super(window, name);
        areaGraph = new AreaGraph();
        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                SuperPacmanAreaBehavior.SuperPacmanCellType color =
                        SuperPacmanAreaBehavior.SuperPacmanCellType.toType(getRGB(getHeight() - 1 - y, x));
                setCell(x, y, new SuperPacmanAreaBehavior.SuperPacmanCell(x, y, color));
            }
        }
        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                if (!cellEqualsToType(x, y, SuperPacmanCellType.WALL) &&
                        !cellEqualsToType(x, y, SuperPacmanCellType.NONE)) {
                    areaGraph.addNode(new DiscreteCoordinates(x, y), hasLeftEdge(x, y), hasUpEdge(x, y),
                                      hasRightEdge(x, y), hasDownEdge(x, y));
                }
            }
        }
    }

    /**
     * Method to check if cell type matches.
     * @param x        coordinate of the cell
     * @param y        coordinate of the cell
     * @param cellType the type of the cell wanted
     * @return true if the current cell matches cellType
     */
    private boolean cellEqualsToType(int x, int y, SuperPacmanCellType cellType) {
        return ((SuperPacmanCell) getCell(x, y)).type == cellType;
    }

    /* ------------------------- Specific methods for constructing areaGraph -------------------------
     * Take parameters x and y of current iteration and compares the WALL edges
     * Return (boolean)
     */
    private boolean hasLeftEdge(int x, int y) {
        return x > 0 && !cellEqualsToType(x - 1, y, SuperPacmanCellType.WALL) &&
                !cellEqualsToType(x - 1, y, SuperPacmanCellType.NONE);
    }

    private boolean hasUpEdge(int x, int y) {
        return y < getHeight() - 1 && !cellEqualsToType(x, y + 1, SuperPacmanCellType.WALL) &&
                !cellEqualsToType(x, y + 1, SuperPacmanCellType.NONE);
    }

    private boolean hasRightEdge(int x, int y) {
        return x < getWidth() - 1 && !cellEqualsToType(x + 1, y, SuperPacmanCellType.WALL) &&
                !cellEqualsToType(x + 1, y, SuperPacmanCellType.NONE);
    }

    private boolean hasDownEdge(int x, int y) {
        return y > 0 && !cellEqualsToType(x, y - 1, SuperPacmanCellType.WALL) &&
                !cellEqualsToType(x, y - 1, SuperPacmanCellType.NONE);
    }

    public static void setInitDifficulty(SuperPacmanDifficulty initDifficulty) {
        SuperPacmanAreaBehavior.initDifficulty = initDifficulty;
    }

    protected GhostsBehavior getGhostsManagement() {
        return ghostsBehavior;
    }

    /**
     * Method to register all of the cell related actors.
     * @param area the area to register the actors
     */
    protected void registerActors(Area area) {
        // Reset the amount of pellets eaten
        Pellet.resetPelletCount();

        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                switch (((SuperPacmanCell) getCell(x, y)).type) {
                    case WALL:
                        Wall wall = new Wall(area, new DiscreteCoordinates(x, y), neighborhood(x, y));
                        area.registerActor(wall);
                        break;
                    case FREE_WITH_BONUS:
                        PowerPellet powerPellet = new PowerPellet(area, new DiscreteCoordinates(x, y));
                        area.registerActor(powerPellet);
                        break;
                    case FREE_WITH_CHERRY:
                        Cake cake = new Cake(area, new DiscreteCoordinates(x, y));
                        area.registerActor(cake);
                        break;
                    case FREE_WITH_DIAMOND:
                        Pellet pellet = new Pellet(area, new DiscreteCoordinates(x, y));
                        area.registerActor(pellet);
                        break;
                    case FREE_WITH_BLINKY:
                        Ghost blinky = new Blinky(area, new DiscreteCoordinates(x, y));
                        area.registerActor(blinky);
                        ghostsBehavior.addGhost(blinky);
                        break;
                    case FREE_WITH_INKY:
                        Ghost inky = new Inky(area, new DiscreteCoordinates(x, y));
                        area.registerActor(inky);
                        ghostsBehavior.addGhost(inky);
                        break;
                    case FREE_WITH_PINKY:
                        Ghost pinky = new Pinky(area, new DiscreteCoordinates(x, y));
                        area.registerActor(pinky);
                        ghostsBehavior.addGhost(pinky);
                        break;
                    case FREE_WITH_CLYDE:
                        Ghost clyde = new Clyde(area, new DiscreteCoordinates(x, y));
                        area.registerActor(clyde);
                        ghostsBehavior.addGhost(clyde);
                        break;
                    default:
                        // do nothing
                }
            }
        }
    }

    /**
     * Method to find if neighborhood cells are walls.
     * @param x coordinate of the cell
     * @param y coordinate of the cell
     * @return 3x3 array with the true for surrounding walls
     */
    private boolean[][] neighborhood(int x, int y) {
        boolean[][] neighbors = new boolean[3][3];

        for (int tabY = -1; tabY < 2; ++tabY) {
            for (int tabX = -1; tabX < 2; ++tabX) {
                if (((x + tabX) >= 0) && ((x + tabX) < getWidth()) && ((y + tabY) >= 0) && ((y + tabY) < getHeight()) &&
                        cellEqualsToType(x + tabX, y + tabY, SuperPacmanCellType.WALL)) {
                    neighbors[tabX + 1][-tabY + 1] = true;
                }
            }
        }
        return neighbors;
    }

    /**
     * Private enum for all of the different cell types
     */
    private enum SuperPacmanCellType {
        NONE(0), // never used as real content
        WALL(-16777216), //black
        FREE_WITH_DIAMOND(-1), //white
        FREE_WITH_BLINKY(-65536), //red
        FREE_WITH_PINKY(-157237), //pink
        FREE_WITH_INKY(-16724737), //cyan
        FREE_WITH_CLYDE(-24526), //orange
        // TODO: change from cherry to cake
        FREE_WITH_CHERRY(-36752), //light red
        FREE_WITH_BONUS(-16478723), //light blue
        FREE_EMPTY(-6118750); // sort of gray

        final int type;

        SuperPacmanCellType(int type) {
            this.type = type;
        }

        public static SuperPacmanAreaBehavior.SuperPacmanCellType toType(int type) {
            for (SuperPacmanAreaBehavior.SuperPacmanCellType ict : SuperPacmanAreaBehavior.SuperPacmanCellType
                    .values()) {
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
        private final SuperPacmanAreaBehavior.SuperPacmanCellType type;

        /**
         * Default SuperPacmanCell Constructor
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public SuperPacmanCell(int x, int y, SuperPacmanAreaBehavior.SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return !hasNonTraversableContent();
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
