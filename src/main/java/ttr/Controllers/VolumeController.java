package ttr.Controllers;

import ttr.Services.SoundService;

public class VolumeController {
    SoundService sc;

    public void setMusicVolume(double volume) {
        this.sc = SoundService.getInstance();
        sc.setMusicVolume(volume);
    }

    public void setSfxVolume(double volume) {
        this.sc = SoundService.getInstance();
        sc.setSfxVolume(volume);
        sc.playSFX("pullCard");
    }
}
