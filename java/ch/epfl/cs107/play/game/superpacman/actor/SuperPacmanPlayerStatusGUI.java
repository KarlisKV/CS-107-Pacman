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
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerStatusGUI implements Graphics {
    private static final float DEPTH = 2000.f;
    private static final float HP_SPACING = 1.25f;
    private final int currentHp;
    private final int MAX_HP;
    private final int score;
    private ImageGraphics life;
    private TextGraphics scoreText;

    protected SuperPacmanPlayerStatusGUI(int currentHp, int MAX_HP, int score) {
        this.currentHp = currentHp;
        this.MAX_HP = MAX_HP;
        this.score = score;
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        int m = 0;
        for (int i = 0; i < MAX_HP; ++i) {
            if (i < currentHp) {
                m = 0;
            } else {
                m = 14;
            }
            life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplaySmall"), 1.f, 1.f,
                                     new RegionOfInterest(m, 0, 14, 14),
                                     anchor.add(new Vector(HP_SPACING * i + 0.375f, 0.375f)), 1, DEPTH);
            life.draw(canvas);
        }
        scoreText = new TextGraphics("Score: " + score, 1.0f, Color.YELLOW, Color.BLACK, 0.0f, false, false, anchor.add(new Vector(width / 3f, height - 1.375f)));
        scoreText.setFontName("emulogic");
        scoreText.draw(canvas);
    }
}
