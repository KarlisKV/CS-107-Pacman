/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        16:01
 */


package ch.epfl.cs107.play.game.superpacman.graphics;

import ch.epfl.cs107.play.game.actor.Acoustics;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanSound;
import ch.epfl.cs107.play.game.superpacman.menus.MenuItems;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Arcade implements Graphics, Acoustics {
    private static final float DEPTH = 10000;
    private static final SoundAcoustics GAME_START_SOUND = SuperPacmanSound.ARCADE_ON.sound;
    private static final SoundAcoustics TURN_OFF_SOUND = SuperPacmanSound.ARCADE_OFF.sound;
    private final Window window;
    String arcadePathName = "superpacman/pacmanArcadeOff";
    private float alpha = 1.0f;
    private float xScaledInit;
    private float yScaledInit;
    private boolean areInitPosSaved = false;
    private boolean isArcadeTurnedOn = false;

    /**
     * Default Arcade Constructor, initialising the sound
     */
    public Arcade(Window window) {
        this.window = window;
    }


    /* ----------------------------------- ACCESSORS ----------------------------------- */

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isArcadeTurnedOn() {
        return isArcadeTurnedOn;
    }

    public void setArcadeTurnedOn(boolean isArcadeTurnedOn) {
        this.isArcadeTurnedOn = isArcadeTurnedOn;
        if (isArcadeTurnedOn) {
            if (!MenuItems.isSoundDeactivated()) {
                GAME_START_SOUND.shouldBeStarted();
            }
            arcadePathName = "superpacman/pacmanArcadeOn";
        } else {
            TURN_OFF_SOUND.shouldBeStarted();
            arcadePathName = "superpacman/pacmanArcadeOff";
        }
    }

    @Override
    public void bip(Audio audio) {
        GAME_START_SOUND.bip(audio);
        TURN_OFF_SOUND.bip(audio);
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

        // DRAW ARCADE
        ImageGraphics arcade = new ImageGraphics(ResourcePath.getBackgrounds(arcadePathName), xScaledInit, yScaledInit,
                                                 new RegionOfInterest(0, 0, 1100, 1100), anchor.add(
                new Vector((width / 2) - (xScaledInit / 2), (height / 2) - (yScaledInit / 2))), alpha, DEPTH);
        arcade.draw(canvas);


        // DRAW JOYSTICK ON ARCADE
        if (isArcadeTurnedOn) {
            ImageGraphics[] joystick = new ImageGraphics[4];
            final float yDisplacement = 12.45f;
            final float xDisplacement = 1.4f;
            for (int i = 0; i < 4; ++i) {
                joystick[i] = new ImageGraphics(ResourcePath.getSprite("superpacman/joystickArcade"), 3, 3,
                                                new RegionOfInterest(0, 28 * i, 28, 28), anchor.add(
                        new Vector((width / 2) - xDisplacement, (height / 2) - yDisplacement)), alpha,
                                                DEPTH + 50);
            }

            ImageGraphics joystickDefault =
                    new ImageGraphics(ResourcePath.getSprite("superpacman/joystickArcadeDefault"), 3, 3,
                                      new RegionOfInterest(0, 0, 28, 28), anchor.add(
                            new Vector((width / 2) - xDisplacement, (height / 2) - yDisplacement)), alpha,
                                      DEPTH + 50);

            // Set joystick orientation
            if (window.getKeyboard() != null) {
                if (window.getKeyboard().get(Keyboard.UP).isDown()) {
                    joystick[Orientation.UP.ordinal()].draw(canvas);
                } else if (window.getKeyboard().get(Keyboard.RIGHT).isDown()) {
                    joystick[Orientation.RIGHT.ordinal()].draw(canvas);
                } else if (window.getKeyboard().get(Keyboard.LEFT).isDown()) {
                    joystick[Orientation.LEFT.ordinal()].draw(canvas);
                } else if (window.getKeyboard().get(Keyboard.DOWN).isDown()) {
                    joystick[Orientation.DOWN.ordinal()].draw(canvas);
                } else {
                    joystickDefault.draw(canvas);
                }
            } else {
                joystickDefault.draw(canvas);
            }
        }

    }
}
