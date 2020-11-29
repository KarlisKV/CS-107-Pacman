/*
 *	Author:      Leonard Cseres
 *	Date:        29.11.20
 *	Time:        02:15
 */


package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerStatusGUI implements Graphics {
    private static final float DEPTH = 2000.0f;
    private static final float HP_SPACING = 1.25f;
    private static final float EDGE_PADDING = 0.375f;
    private static final int LIFE_SPRITE_SIZE = 14;
    private static final String FONT = "emulogic";
    private static final int LIFE = 0;
    private static final int NO_LIFE = LIFE_SPRITE_SIZE;
    private final int currentHp;
    private final int maxHp;
    private final int score;

    protected SuperPacmanPlayerStatusGUI(int currentHp, int maxHp, int score) {
        this.currentHp = currentHp;
        this.maxHp = maxHp;
        this.score = score;
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        // TODO: Temporary fix, find better solution
        if (SuperPacman.CAMERA_SCALE_FACTOR < 55) {
            for (int i = 0; i < maxHp; ++i) {
                int x = i < currentHp ? LIFE : NO_LIFE;

                ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplaySmall"),
                                                       1.f,
                                                       1.f,
                                                       new RegionOfInterest(x, 0, LIFE_SPRITE_SIZE, LIFE_SPRITE_SIZE),
                                                       anchor.add(new Vector(HP_SPACING * i + EDGE_PADDING,
                                                                             EDGE_PADDING)),
                                                       1,
                                                       DEPTH);
                life.draw(canvas);
            }
            TextGraphics scoreText = new TextGraphics("Score: " + score,
                                                      1.0f,
                                                      Color.YELLOW,
                                                      Color.BLACK,
                                                      0.0f,
                                                      false,
                                                      false,
                                                      anchor.add(new Vector(width / 3, height - (1 + EDGE_PADDING))));

            scoreText.setFontName(FONT);
            scoreText.draw(canvas);
        }
    }
}
