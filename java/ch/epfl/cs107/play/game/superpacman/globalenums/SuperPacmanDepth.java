package ch.epfl.cs107.play.game.superpacman.globalenums;

public enum SuperPacmanDepth {
    GLOW(-5000),
    GATE(-3500),
    WALL(-2000),
    COLLECTABLES(-1000),
    PLAYER_DEATH(50),
    GHOSTS(200),
    PLAYER(600),
    COMBO_TEXT(800),
    FOREGROUND(1000),
    GHOST_PATHING(1000),
    SCREEN_FADE_DOORS(4000),
    PAYER_GUI(5000),
    ARCADE(10000),
    ARCADE_JOYSTICK(10100),
    MENU(10250),
    STATUS_GUI(15000),
    MENU_SCREEN_FADE(25000);

    public final float value;

    SuperPacmanDepth(float value) {
        this.value = value;
    }
}
