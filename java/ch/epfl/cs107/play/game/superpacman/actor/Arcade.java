/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        16:01
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

// TODO: check if implementation is good
public class Arcade implements Actor {
    private final SoundAcoustics gameStartSound;
    private boolean soundHasStarted = false;
    private float alpha = 0;

    /**
     * Default Arcade Constructor, initialising the sound
     */
    public Arcade() {
        gameStartSound =
                new SoundAcoustics(ResourcePath.getSounds("superpacman/game_start"), 1.f, false, false, false, false);
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void bip(Audio audio) {
        if (!soundHasStarted) {
            gameStartSound.shouldBeStarted();
            gameStartSound.bip(audio);
            soundHasStarted = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        ImageGraphics bgArcade = new ImageGraphics(ResourcePath.getBackgrounds("superpacman/pacmanArcade"),
                                                   46,
                                                   92,
                                                   new RegionOfInterest(0, 0, 258, 484),
                                                   anchor.add(new Vector(252, 210)),
                                                   alpha,
                                                   15000);
        bgArcade.draw(canvas);

        // TODO: Temporary fix, find better solution
        ShapeGraphics blackBackground =
                new ShapeGraphics(new Circle(300), Color.BLACK, Color.BLACK, 0.0f, 1 - alpha, 14000);
        blackBackground.draw(canvas);
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
