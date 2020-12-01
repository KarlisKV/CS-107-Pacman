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
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.transitions.EaseInOutCubic;
import ch.epfl.cs107.play.math.transitions.Transition;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;

// TODO: check if implementation is good
public class Arcade implements Actor {
    private static final float DEPTH = 15000.0f;
    private static final String TITLE_PATH = "superpacman/pacmanTitleMenu";
    private final SoundAcoustics gameStartSound;
    private final Transition transitionOverlay = new EaseInOutCubic(0.02f);
    private final Transition transitionTitle = new EaseInOutCubic(0.02f);
    private final boolean doFadeIn;
    String arcadePathName;
    private float xScaledInit;
    private float yScaledInit;
    private boolean areInitPosSaved = false;
    private boolean isArcadeTurnedOn;
    private boolean showTitle = true;
    private boolean fadeTitle = false;

    /**
     * Default Arcade Constructor, initialising the sound
     */
    public Arcade(boolean isArcadeTurnedOn, boolean doFadeIn) {
        setArcadeTurnedOn(isArcadeTurnedOn);
        this.doFadeIn = doFadeIn;

        gameStartSound =
                new SoundAcoustics(ResourcePath.getSounds("superpacman/game_start"), 1.f, false, false, false, false);
    }

    public void fadeTitle() {
        this.fadeTitle = true;
    }

    public boolean isArcadeTurnedOn() {
        return isArcadeTurnedOn;
    }

    /* ----------------------------------- ACCESSORS ----------------------------------- */
    public void setArcadeTurnedOn(boolean isArcadeTurnedOn) {
        this.isArcadeTurnedOn = isArcadeTurnedOn;
        if (isArcadeTurnedOn) {
            gameStartSound.shouldBeStarted();
            showTitle = false;
            arcadePathName = "superpacman/pacmanArcadeOn";
        } else {
            arcadePathName = "superpacman/pacmanArcadeOff";
        }
    }

    @Override
    public void bip(Audio audio) {
        gameStartSound.bip(audio);
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        if (!areInitPosSaved) {
            xScaledInit = canvas.getScaledWidth();
            yScaledInit = canvas.getScaledHeight();
            areInitPosSaved = true;
        }

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        ImageGraphics arcade = new ImageGraphics(ResourcePath.getBackgrounds(arcadePathName),
                                                 xScaledInit,
                                                 yScaledInit,
                                                 new RegionOfInterest(0, 0, 1100, 1100),
                                                 anchor.add(new Vector((width / 2) - (xScaledInit / 2),
                                                                       (height / 2) - (yScaledInit / 2))),
                                                 1.0f,
                                                 DEPTH);
        arcade.draw(canvas);

        float alphaOverlay = 0.0f;
        if (doFadeIn && !transitionOverlay.isFinished()) {
            alphaOverlay = 1 - transitionOverlay.getProgress();
        }
        // TODO: Check if better solution exists
        ShapeGraphics overlay =
                new ShapeGraphics(new Circle(300, new Vector(xScaledInit, yScaledInit)), Color.BLACK, Color.BLACK, 0.0f,
                                  alphaOverlay, DEPTH + 5000);

        overlay.draw(canvas);
        if (showTitle) {
            float alphaTitle = 1.0f;
            if (fadeTitle && !transitionTitle.isFinished()) {
                alphaTitle = 1.0f - transitionTitle.getProgress();
            } else if (transitionTitle.isFinished()) {
                alphaTitle = 0.0f;
                showTitle = false;
            }
            ImageGraphics title = new ImageGraphics(ResourcePath.getBackgrounds(TITLE_PATH),
                                                    xScaledInit,
                                                    yScaledInit,
                                                    new RegionOfInterest(0, 0, 1100, 1100),
                                                    anchor.add(new Vector((width / 2) - (xScaledInit / 2),
                                                                          (height / 2) - (yScaledInit / 2))),
                                                    alphaTitle,
                                                    DEPTH + 500);
            title.draw(canvas);
        }
        if (isArcadeTurnedOn) {
            ImageGraphics[] joystick = new ImageGraphics[4];
            final float yDisplacement = 12.45f;
            final float xDisplacement = 1.4f;
            for (int i = 0; i < 4; ++i) {
                joystick[i] = new ImageGraphics(ResourcePath.getSprite("superpacman/joystickArcade"), 3, 3,
                                                new RegionOfInterest(0, 28 * i, 28, 28), anchor.add(
                        new Vector((width / 2) - xDisplacement, (height / 2) - yDisplacement)), 1.0f,
                                                DEPTH + 1500);
            }

            ImageGraphics joystickDefault =
                    new ImageGraphics(ResourcePath.getSprite("superpacman/joystickArcadeDefault"), 3, 3,
                                      new RegionOfInterest(0, 0, 28, 28), anchor.add(
                            new Vector((width / 2) - xDisplacement, (height / 2) - yDisplacement)), 1.0f,
                                      DEPTH + 1000);

            if (SuperPacman.player.getKeyboard() != null) {
                if (SuperPacman.player.getKeyboard().get(Keyboard.UP).isDown()) {
                    joystick[Orientation.UP.ordinal()].draw(canvas);
                } else if (SuperPacman.player.getKeyboard().get(Keyboard.RIGHT).isDown()) {
                    joystick[Orientation.RIGHT.ordinal()].draw(canvas);
                } else if (SuperPacman.player.getKeyboard().get(Keyboard.LEFT).isDown()) {
                    joystick[Orientation.LEFT.ordinal()].draw(canvas);
                } else if (SuperPacman.player.getKeyboard().get(Keyboard.DOWN).isDown()) {
                    joystick[Orientation.DOWN.ordinal()].draw(canvas);
                } else {
                    joystickDefault.draw(canvas);
                }
            } else {
                joystickDefault.draw(canvas);
            }
        }

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
