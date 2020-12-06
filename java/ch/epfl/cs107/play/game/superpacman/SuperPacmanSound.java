package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;

public enum SuperPacmanSound {
    ARCADE_ON(new SoundAcoustics(ResourcePath.getSounds("superpacman/game_start"), 1.f, false, false, false, false)),
    ARCADE_OFF(
            new SoundAcoustics(ResourcePath.getSounds("superpacman/menu/close_004"), 1.f, false, false, false, true)),
    RETREATING(new SoundAcoustics(ResourcePath.getSounds("superpacman/retreating"), 1.f, false, false, true, true)),
    SIREN(new SoundAcoustics(ResourcePath.getSounds("superpacman/siren_1"), 1.f, false, false, true, false)),
    POWER_PELLET(new SoundAcoustics(ResourcePath.getSounds("superpacman/power_pellet"), 1.f, false, false, true, true)),
    PACMAN_DEATH(new SoundAcoustics(ResourcePath.getSounds("superpacman/death_1"), 1.f, false, false, false, true)),
    MUNCH(new SoundAcoustics(ResourcePath.getSounds("superpacman/munch_1"), 1.f, false, false, false, false)),
    EAT_FRUIT(new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_fruit"), 1.f, false, false, false, false)),
    EAT_GHOST(new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_ghost"), 1.f, false, false, false, false)),
    MENU_SELECT(
            new SoundAcoustics(ResourcePath.getSounds("superpacman/menu/select_007"), 1.f, false, false, false, true)),
    MENU_ENTER(
            new SoundAcoustics(ResourcePath.getSounds("superpacman/menu/select_005"), 1.f, false, false, false, true)),
    MENU_EXIT(new SoundAcoustics(ResourcePath.getSounds("superpacman/menu/close_004"), 1.f, false, false, false, true)),
    MENU_KEY_CLICK(
            new SoundAcoustics(ResourcePath.getSounds("superpacman/menu/switch4"), 1.f, false, false, false, false)),
    MENU_ERROR(
            new SoundAcoustics(ResourcePath.getSounds("superpacman/menu/error_003"), 0.7f, false, false, false, true)),
    COLLECT_KEY(new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_key"), 1.f, false, false, false, false));

    public final SoundAcoustics sound;

    SuperPacmanSound(SoundAcoustics sound) {
        this.sound = sound;
    }
}

