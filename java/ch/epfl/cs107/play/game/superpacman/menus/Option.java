package ch.epfl.cs107.play.game.superpacman.menus;

public enum Option {
    PLAY("Play"),
    OPTIONS("Options"),
    HELP("Help"),
    QUIT("Quit"),
    LEADERBOARD("Leaderboard"),
    CLEAR_LEADERBOARD("Clear Leaderboard"),
    RESTORE_DEFAULT("Restore to default"),
    CREDITS("Credits"),
    PAUSE("Paused"),
    END_GAME("End Game"),
    RESUME("Resume"),
    SOUND("Sound"),
    GLOW("Glow Effect"),
    CAMERA_SMOOTHING("Camera smoothing"),
    CAMERA_SHAKE("Camera Shake"),
    FPS("Show fps"),
    DIFFICULTY("Difficulty"),
    BACK("Back"),
    MORE_GHOSTS("More"),
    MORE_POINTS("More"),
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
