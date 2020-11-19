/*
 *	Author:      Leonard Cseres
 *	Date:        18.11.20
 *	Time:        20:03
 */


package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {
    private final TextGraphics hpText;
    private final Sprite sprite;
    private float hp;

    public GhostPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        this.sprite = new Sprite(spriteName, 1, 1.f, this);
        hp = 10;

        hpText = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLUE);
        hpText.setParent(this);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        hpText.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        Button key = keyboard.get(Keyboard.UP);
        if (key.isDown()) {
            this.moveUp();
            this.orientate(Orientation.UP);
        }
        key = keyboard.get(Keyboard.DOWN);
        if (key.isDown()) {
            this.moveDown();
            this.orientate(Orientation.DOWN);
        }
        key = keyboard.get(Keyboard.LEFT);
        if (key.isDown()) {
            this.moveLeft();
            this.orientate(Orientation.LEFT);
        }
        key = keyboard.get(Keyboard.RIGHT);
        if (key.isDown()) {
            this.moveRight();
            this.orientate(Orientation.RIGHT);
        }

        if (!isWeak()) {
            hp -= deltaTime;
        }
        this.hpText.setText(Integer.toString((int) hp));
        super.update(deltaTime);
    }

    public boolean isWeak() {
        return hp <= 0;
    }

    public void strengthen() {
        hp = 10;
    }

    public void moveUp() {
        setCurrentPosition(getPosition().add(0.f, 0.1f));
    }

    public void moveDown() {
        setCurrentPosition(getPosition().add(0.f, -0.1f));
    }

    public void moveLeft() {
        setCurrentPosition(getPosition().add(-0.1f, 0.f));
    }

    public void moveRight() {
        setCurrentPosition(getPosition().add(0.1f, 0.f));
    }

    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setCurrentPosition(position.toVector());
        this.resetMotion();

    }

    public void leaveArea(Area area, DiscreteCoordinates position) {
        area.unregisterActor(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
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
}
