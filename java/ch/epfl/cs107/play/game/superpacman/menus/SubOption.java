package ch.epfl.cs107.play.game.superpacman.menus;

public enum SubOption {
    TOGGLE_ON("On"),
    TOGGLE_OFF("Off"),
    DIFFICULTY_EASY("Easy"),
    DIFFICULTY_NORMAL("Normal"),
    DIFFICULTY_HARD("Hard"),
    DIFFICULTY_IMPOSSIBLE("Impossible");

    public final String text;

    SubOption(String text) {
        this.text = text;
    }

}
