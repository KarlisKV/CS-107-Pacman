package ch.epfl.cs107.play.game.superpacman.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.globalenums.SuperPacmanDepth;
import ch.epfl.cs107.play.game.superpacman.graphics.Glow;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends CollectableAreaEntity {
    private static final int POINTS = 200;
    private final Sprite sprite;
    private final Glow glow;
    private Logic signal = Logic.FALSE;

    /**
     * Default MovableAreaEntity constructor
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public Key(Area area, DiscreteCoordinates position) {
        super(area, position);
        sprite = new Sprite("superpacman/keySmall", 1, 1, this);
        sprite.setDepth(SuperPacmanDepth.COLLECTABLES.value);
        glow = new Glow(this, sprite, Glow.GlowColors.LIGHT_BLUE_KEY, 2.5f, 0.5f);
    }

    /**
     * +
     * set signal to Logic True when the key is collected
     */
    public void setSignalOn() {
        signal = Logic.TRUE;
    }

    /**
     * +
     * get the signal of the key
     */
    public Logic getSignal() {
        return signal;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


}