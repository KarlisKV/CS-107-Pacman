/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        05:48
 */


package ch.epfl.cs107.play.game.superpacman.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Glow;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class PowerPellet extends CollectableAreaEntity {
    private Sprite sprite;
    private final Glow glow;

    /**
     * Default MovableAreaEntity constructor
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public PowerPellet(Area area, DiscreteCoordinates position) {
        super(area, position);
        sprite = new Sprite("superpacman/powerPellet", 1, 1, this);
        sprite.setDepth(-1000);
        glow = new Glow(this, sprite, Glow.GlowColors.LIGHT_PINK, 4.0f, 0.5f);

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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
