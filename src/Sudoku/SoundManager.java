package Sudoku;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private Clip correctSoundClip;
    private Clip wrongSoundClip;
    private Clip backSoundClip;

    public SoundManager() {
        loadSounds();  // Memuat suara saat objek SoundManager dibuat
    }

    // Memuat file suara
    private void loadSounds() {
        try {
            // Memuat suara "benar"
            AudioInputStream correctStream = AudioSystem.getAudioInputStream(new File("correct_sound.wav"));
            correctSoundClip = AudioSystem.getClip();
            correctSoundClip.open(correctStream);

            // Memuat suara "salah"
            AudioInputStream wrongStream = AudioSystem.getAudioInputStream(new File("sounds/wrong_sound.wav"));
            wrongSoundClip = AudioSystem.getClip();
            wrongSoundClip.open(wrongStream);

            // Memuat backsound
            AudioInputStream backStream = AudioSystem.getAudioInputStream(new File("sounds/background_music.wav"));
            backSoundClip = AudioSystem.getClip();
            backSoundClip.open(backStream);
            backSoundClip.loop(Clip.LOOP_CONTINUOUSLY);  // Memutar backsound secara berulang
        } catch (Exception e) {
            e.printStackTrace();  // Menangani error jika file suara tidak ditemukan
        }
    }

    // Memulai backsound
    public void startBackSound() {
        if (backSoundClip != null) {
            backSoundClip.start();
        }
    }

    // Menghentikan backsound
    public void stopBackSound() {
        if (backSoundClip != null) {
            backSoundClip.stop();
        }
    }

    // Memainkan suara benar
    public void playCorrectSound() {
        if (correctSoundClip != null) {
            correctSoundClip.setFramePosition(0);  // Mulai dari awal
            correctSoundClip.start();
        }
    }

    // Memainkan suara salah
    public void playWrongSound() {
        if (wrongSoundClip != null) {
            wrongSoundClip.setFramePosition(0);  // Mulai dari awal
            wrongSoundClip.start();
        }
    }
}
