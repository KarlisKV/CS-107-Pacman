/*
 *	Author:      Leonard Cseres
 *	Date:        18.11.20
 *	Time:        20:03
 */


package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {
    private final TextGraphics message;
    private final Sprite sprite;
    private final SoundAcoustics soundAcoustics;
    private final int ANIMATION_DURATION = 12;
    public static ShapeGraphics shapeGraphics;
    private final ShapeGraphics shadow;
    private final ImageGraphics glow;
    private float hp = 10;
    private int count = 0;

    public GhostPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        message = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLACK);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        soundAcoustics = new SoundAcoustics(ResourcePath.getSounds("sadPiano1"), 1.f, true, false, true, false);
        shapeGraphics = new ShapeGraphics(new Circle(250.f, new Vector(10.f, 10.f)), Color.BLACK, Color.BLACK, 0.f, 0.f,
                                          10000.f);
        shadow = new ShapeGraphics(new Circle(250.f, new Vector(10.f, 10.f)), Color.BLACK, Color.BLACK, 0.f, 0.4f,
                                   10000.f);
        glow = new ImageGraphics(ResourcePath.getSprite("Test"), 30.f, 30.f, new RegionOfInterest(0, 0, 2000, 2000), new Vector(-14.5f, -14.5f), 0.8f, -1.f);
        glow.setParent(this);

        resetMotion();
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        message.draw(canvas);
        shapeGraphics.draw(canvas);
        glow.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        if (hp > 0) {
            hp -= deltaTime;
            message.setText(Integer.toString((int) hp));
        }
        if (hp < 0) {
            hp = 0.f;
        }
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
        moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        super.update(deltaTime);

    }

    @Override
    public void bip(Audio audio) {
        if (count == 0) {
            soundAcoustics.shouldBeStarted();
            soundAcoustics.bip(audio);
            ++count;
        }
    }

    /**
     * Orientate or Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveOrientate(Orientation orientation, Button b) {

        if (b.isDown()) {
            if (getOrientation() == orientation) {
                move(ANIMATION_DURATION);
            } else {
                orientate(orientation);
            }
        }
    }

    public boolean isWeak() {
        return hp <= 0.f;
    }

    public void strengthen() {
        hp = 10;
    }

    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());

        resetMotion();

    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
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
