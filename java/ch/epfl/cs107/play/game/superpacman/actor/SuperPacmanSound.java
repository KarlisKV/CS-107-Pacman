package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;

public enum SuperPacmanSound {
    GAME_START(new SoundAcoustics(ResourcePath.getSounds("superpacman/game_start"), 1.f, false, false, false, false)),
    RETREATING(new SoundAcoustics(ResourcePath.getSounds("superpacman/retreating"), 1.f, false, false, true, true)),
    SIREN(new SoundAcoustics(ResourcePath.getSounds("superpacman/siren_1"), 1.f, false, false, true, false)),
    POWER_PELLET(new SoundAcoustics(ResourcePath.getSounds("superpacman/power_pellet"), 1.f, false, false, true, true)),
    PACMAN_DEATH(new SoundAcoustics(ResourcePath.getSounds("superpacman/death_1"), 1.f, false, false, false, true)),
    MUNCH(new SoundAcoustics(ResourcePath.getSounds("superpacman/munch_1"), 1.f, false, false, false, false)),
    EAT_FRUIT(new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_fruit"), 1.f, false, false, false, false)),
    EAT_GHOST(new SoundAcoustics(ResourcePath.getSounds("superpacman/eat_ghost"), 1.f, false, false, false, false));

    final SoundAcoustics sound;

    SuperPacmanSound(SoundAcoustics sound) {
        this.sound = sound;
    }
}

