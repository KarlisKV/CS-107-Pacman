package ch.epfl.cs107.play.game.superpacman.menus;

public enum Option {
    PLAY("Play"),
    OPTIONS("Options"),
    HELP("Help"),
    QUIT("Quit"),
    LEADERBOARD("Leaderboard"),
    CLEAR_LEADERBOARD("Clear Leaderboard"),
    CREDITS("Credits"),
    SOUND("Sound"),
    CAMERA_SHAKE("Camera Shake"),
    FPS("Show fps"),
    DIFFICULTY("Difficulty"),
    BACK("Back"),
    NAME("Enter name"),
    RESTART("Restart"),
    BACK_TO_MAIN_MENU("Back to main menu");

    public final String text;

    Option(String text) {
        this.text = text;
    }

    public static Option get(int ordinal) {
        for (Option option : Option.values()) {
            if (option.ordinal() == ordinal) {
                return option;
            }
        }
        // Option not found
        return null;
    }
}
