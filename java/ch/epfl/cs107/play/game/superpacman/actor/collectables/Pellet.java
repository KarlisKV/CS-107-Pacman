/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        05:46
 */


package ch.epfl.cs107.play.game.superpacman.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pellet extends CollectableAreaEntity implements Interactor {
    private static final int POINTS = 10;
    private static int nbrOfPelletsEaten = 0;
    private static int totalPellets = 0;

    private static boolean allPelletsCleared = false;

    public static boolean areAllPelletsCleared() {
        return getNbrOfPelletsEaten() >= getTotalPellets();
    }

    private final Glow glow;
    private final Sprite sprite;
    private final SuperPacmanPelletHandler pelletHandler = new SuperPacmanPelletHandler();


    /**
     * Default MovableAreaEntity constructor
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public Pellet(Area area, DiscreteCoordinates position) {
        super(area, position);
        sprite = new Sprite("superpacman/pellet", 1, 1, this);
        sprite.setDepth(DEPTH_COLLECTABLES);
        glow = new Glow(this, sprite, Glow.GlowColors.LIGHT_PINK, 1.5f, 0.2f);
        ++totalPellets;
    }

    public static int getTotalPellets() {
        return totalPellets;
    }

    public static int getNbrOfPelletsEaten() {
        return nbrOfPelletsEaten;
    }

    public static void resetPelletCount() {
        nbrOfPelletsEaten = 0;
        totalPellets = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        glow.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public void collect() {
        ++nbrOfPelletsEaten;
        super.collect();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> cellsInView = new ArrayList<>();
        final int RANGE = 2;
        for (DiscreteCoordinates currentCell : getCurrentCells()) {
            for (int x = -RANGE; x <= RANGE; ++x) {
                for (int y = -RANGE; y <= RANGE; ++y) {
                    cellsInView.add(currentCell.jump(x, y));
                }
            }
        }
        return cellsInView;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(pelletHandler);
    }

    /**
     * Interaction handler class for Pellet
     */
    private class SuperPacmanPelletHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer player) {
            if (!((SuperPacmanArea) getOwnerArea()).getGhostsManagement().areGhostsNotFrightened()) {
                // Pellet movement towards player
                float xPlayer = player.getPosition().x;
                float yPlayer = player.getPosition().y;
                float xPellet = getCurrentMainCellCoordinates().x;
                float yPellet = getCurrentMainCellCoordinates().y;

                final float SPEED_MODIFIER = 0.25f;
                float xDiff = 0;
                if (xPellet > xPlayer) {
                    xDiff -= SPEED_MODIFIER;
                } else if (xPellet < xPlayer) {
                    xDiff += SPEED_MODIFIER;
                }
                float yDiff = 0;
                if (yPellet > yPlayer) {
                    yDiff -= SPEED_MODIFIER;
                } else if (yPellet < yPlayer) {
                    yDiff += SPEED_MODIFIER;
                }
                setCurrentPosition(getPosition().add(xDiff, yDiff));
                collect();
                SuperPacmanPlayer.getPlayerSoundUtility().play(SuperPacmanPlayer.MUNCH_SOUND);
                player.updateScore(getPoints());
            }
        }
    }
}
