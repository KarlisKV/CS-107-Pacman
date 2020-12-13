/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        19:14
 */


package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.actor.Acoustics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.superpacman.menus.MenuStateManager;
import ch.epfl.cs107.play.window.Audio;

import java.util.*;

public class SoundUtility implements Acoustics {
    private static boolean stopAudio = false;
    private final List<SoundAcoustics> soundQueue = new ArrayList<>();
    private final Map<SoundAcoustics, Boolean> soundMap = new HashMap<>();
    private boolean audioPaused;


    /**
     * Constructor for SoundUtility class
     * @param soundAcousticsList array of sounds to be remembered
     * @param audioPaused        if the audio is paused at the start
     */
    public SoundUtility(SoundAcoustics[] soundAcousticsList, boolean audioPaused) {
        for (SoundAcoustics sound : soundAcousticsList) {
            soundMap.put(sound, false);
        }
        this.audioPaused = audioPaused;
    }

    @Override
    public void bip(Audio audio) {

        // Stop all sounds
        if (stopAudio) {
            SoundAcoustics.stopAllSounds(audio);
            stopAudio = false;
        }
        if (!audioPaused) {
            // Request the sounds to be played
            Iterator<SoundAcoustics> iterator = soundQueue.iterator();
            if (!soundQueue.isEmpty()) {
                while (iterator.hasNext()) {
                    SoundAcoustics sound = iterator.next();
                    if (Boolean.TRUE.equals(soundMap.get(sound))) {
                        sound.shouldBeStarted();
                        soundMap.replace(sound, false);
                        iterator.remove();
                    }
                }
            }
        }
        // Play the requested sounds
        for (SoundAcoustics sound : soundMap.keySet()) {
            sound.bip(audio);
        }
    }

    /**
     * Method to request to play a sound
     * @param sound the sound to play
     */
    public void play(SoundAcoustics sound) {
        play(sound, false);
    }

    /**
     * Method to request to play a sound
     * @param sound      the sound to play
     * @param stopOthers if the other sounds should be stopped when playing the sound
     */
    public void play(SoundAcoustics sound, boolean stopOthers) {
        if (!MenuStateManager.isSoundDeactivated()) {
            if (stopOthers) {
                stopAll();
            }
            soundQueue.add(sound);
            soundMap.replace(sound, true);
        }

    }

    /**
     * Method to stop all sound from playing, interrupting them
     */
    public void stopAll() {
        for (SoundAcoustics sound : soundMap.keySet()) {
            soundMap.replace(sound, false);
        }
        soundQueue.clear();
        stopAudio = true;
    }

    /**
     * Method to stop a specific sound to be played
     */
    public void stop(SoundAcoustics sound) {
        soundMap.replace(sound, false);
    }

    /**
     * Setter to pause and unpause the sound requests
     */
    public void setAudioPaused(boolean audioPaused) {
        this.audioPaused = audioPaused;
    }
}
