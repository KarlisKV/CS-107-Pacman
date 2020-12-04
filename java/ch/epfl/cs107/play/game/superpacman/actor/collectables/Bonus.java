/*
 *	Author:      Leonard Cseres
 *	Date:        01.12.20
 *	Time:        04:29
 */


package ch.epfl.cs107.play.game.superpacman.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bonus extends CollectableAreaEntity {
    private static final int SPRITE_SIZE = 16;
    private static final int SPRITE_FRAMES = 4;
    private static final int ANIMATION_DURATION = 6;
    private final Animation animation;

    /**
     * Default MovableAreaEntity constructor
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public Bonus(Area area, DiscreteCoordinates position) {
        super(area, position);
        Sprite[] sprites =
                RPGSprite.extractSprites("superpacman/coin", SPRITE_FRAMES, 1, 1, this, SPRITE_SIZE, SPRITE_SIZE);
        for (Sprite sprite : sprites) {
            sprite.setDepth(-1000);
        }
        animation = new Animation(ANIMATION_DURATION, sprites);
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        //noinspection RedundantCast
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
