package ttr.Services;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Objects;
import static ttr.Constants.ClientConstants.*;

public class SoundService {
    private static SoundService controller;
    private MediaPlayer player;
    private double sfxVolume = 20;

    public SoundService() {
        Media backgroundMusic = new Media(getClass().getResource("/ttr/music/europe.mp3").toString());
        player = new MediaPlayer(backgroundMusic);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(20);
    }

    public void setMusicVolume(double volume) {
        player.setVolume(volume / 100);
    }

    public void setSfxVolume(double sfxVolume) {
        this.sfxVolume = sfxVolume / 100;
    }

    public void playMusic() {
        if (player.getStatus() != MediaPlayer.Status.PLAYING) {
            player.play();
        }
    }

    public void playSFX(String choice) {
        AudioClip clip;
        if (Objects.equals(choice, SFX_PULLCARD)) {
            clip = new AudioClip(getClass().getResource("/ttr/sfx/card_dealt3.mp3").toString());

        } else if (Objects.equals(choice, SFX_BUTTONCLICK)) {
            clip = new AudioClip(getClass().getResource("/ttr/sfx/ButtonClick.ogg").toString());

        } else if (Objects.equals(choice, SFX_PLACETRAIN)) {
            clip = new AudioClip(getClass().getResource("/ttr/sfx/train_horn2.mp3").toString());

        } else if (Objects.equals(choice, SFX_STARTGAME)) {
            clip = new AudioClip(getClass().getResource("/ttr/sfx/startGame.mp3").toString());
        } else {
            return;
        }
        clip.stop();
        clip.setVolume(sfxVolume);
        clip.play();
    }

    public static SoundService getInstance() {
        if (controller == null) {
            controller = new SoundService();
        }
        return controller;
    }
}
