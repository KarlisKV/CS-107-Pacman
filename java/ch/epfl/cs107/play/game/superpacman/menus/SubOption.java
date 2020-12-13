package ch.epfl.cs107.play.game.superpacman.menus;

public enum SubOption {
    TOGGLE_ON("On"),
    TOGGLE_OFF("Off"),
    DIFFICULTY_EASY("Easy"),
    DIFFICULTY_NORMAL("Normal"),
    DIFFICULTY_HARD("Hard"),
    DIFFICULTY_IMPOSSIBLE("Impossible"),
    CAMERA_NO_SMOOTH("None"),
    CAMERA_SMOOTH("Smooth"),
    CAMERA_VERY_SMOOTH("Very Smooth");

    public final String text;

    SubOption(String text) {
        this.text = text;
    }

}
