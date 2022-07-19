package ttr.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ttr.App;
import ttr.Constants.ClientConstants;
import ttr.Constants.Token;
import ttr.Services.SoundService;

import java.io.IOException;

public class GameLoginController {
    private Stage stage;
    private Scene scene;
    private ClientConstants cc = new ClientConstants();
    private SoundService sc;

    public GameLoginController() {
        this.sc = SoundService.getInstance();
        sc.playMusic();
    }

    public void joinGame(String token, MouseEvent event) throws NullPointerException {
        new Token().createToken(token);
        new App();
        Platform.runLater(() -> {
            try {
                loadFile(event, "game_start.fxml");
            } catch (NullPointerException ignored) {
            }
        });
    }

    public void loadFile(MouseEvent event, String file) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ttr/fxml/" + file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.scene = new Scene(root, cc.getScreenX(), cc.getScreenY());
        stage.setScene(scene);
        stage.show();
    }
}
