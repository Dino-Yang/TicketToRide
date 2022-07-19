package ttr.Views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ttr.Controllers.GameLoginController;
import ttr.Controllers.VolumeController;
import java.io.IOException;
import java.util.Locale;

public class GameLoginView {
    public VBox mainMenu;
    public VBox login;
    public VBox settings;
    public Slider sfxSlider;
    public Slider musicSlider;
    GameLoginController glc = new GameLoginController();
    VolumeController vc = new VolumeController();

    @FXML
    protected TextField tokenTextField;

    @FXML
    protected void setGameToken(MouseEvent event) throws IOException {
        String token = tokenTextField.getText().toUpperCase(Locale.ROOT);

        if (token.length() == 6) {
            glc.joinGame(token, event);
        }
    }

    public void playClicked() {
        mainMenu.setVisible(false);
        login.setVisible(true);
    }

    public void closeClicked() {
        Platform.exit();
        System.exit(0);
    }

    public void settingsClicked() {
        mainMenu.setVisible(false);
        settings.setVisible(true);
    }

    public void rulesClicked(MouseEvent event){
        glc.loadFile(event,"rules.fxml");
    }

    public void volumeChanged() {
        vc.setMusicVolume(musicSlider.getValue());
    }

    public void sfxChanged() {
        vc.setSfxVolume(sfxSlider.getValue());
    }

    public void backClicked() {
        if (settings.isVisible()) {
            settings.setVisible(false);
            mainMenu.setVisible(true);
        }
    }
}
