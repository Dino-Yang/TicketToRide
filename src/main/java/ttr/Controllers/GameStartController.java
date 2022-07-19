package ttr.Controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ttr.App;
import ttr.Constants.ClientConstants;
import ttr.Model.GameStartModel;
import ttr.Model.PlayerModel;
import ttr.Services.FirestoreService;
import ttr.Services.SoundService;
import ttr.Views.GameStartObserver;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import static ttr.Constants.ClientConstants.PLAYERS;
import static ttr.Constants.ClientConstants.SFX_STARTGAME;

public class GameStartController implements Controller {
    private SoundService sc;
    private PlayerModel player = new PlayerModel();
    private GameStartModel gsm = new GameStartModel();
    private ClientConstants cc = new ClientConstants();
    private FirestoreService fs;
    public static GameStartController gsc;

    private Stage stage;
    private Scene scene;

    private GameStartController() {
        fs = FirestoreService.getInstance();
        sc = SoundService.getInstance();
    }

    public static GameStartController getInstance() {
        if (gsc == null) {
            gsc = new GameStartController();
        }
        return gsc;
    }

    private Map playerMap() {
        return (Map) fs.get(cc.getID()).get(PLAYERS);
    }

    public void playerSelect(String id) {
        player.setPlayerColor(getSelectedPlayerColor(id));
        player.setPlayerNumber(getSelectedPlayerNumber(id));
    }

    public void playerNameSubmit(TextField nameField) {
        if (nameField.getText() != null || !nameField.getText().trim().isEmpty()) {
            player.setPlayerName(nameField.getText());
        }
    }

    private String getSelectedPlayerColor(String id) {
        String[] parts = id.split("_");
        int playerNumber = Integer.parseInt(parts[1]);
        if (playerNumber == 1) {
            return "red";
        } else if (playerNumber == 2) {
            return "blue";
        } else if (playerNumber == 3) {
            return "green";
        } else if (playerNumber == 4) {
            return "yellow";
        } else if (playerNumber == 5) {
            return "purple";
        }
        return "";
    }

    private int getSelectedPlayerNumber(String id) {
        String[] parts = id.split("_");
        return Integer.parseInt(parts[1]);
    }

    public void startGame(MouseEvent event) throws IOException {
        //change fxml file if following conditions are met: min 3 players with name, player starting is player 1 (red)
        Map playerMap = playerMap();
        int playerCount = playerMap.size();

        if (this.player.getPlayerColor() != null && playerCount >= 1) {
            //load file
            BoardController bc = BoardController.getInstance();
            bc.setPlayer(this.player);
            sc.playSFX(SFX_STARTGAME);
            loadFile(event, "game_interface.fxml");
            new App();
        }
    }

    public void loadFile(MouseEvent event, String file) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ttr/fxml/" + file)));

        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.scene = new Scene(root, cc.getScreenX(), cc.getScreenY());
        this.stage.setScene(scene);
        stage.show();
    }

    public void addGameStartObserver(GameStartObserver gameStartView) {
        this.gsm.addObserver(gameStartView);
    }

    @Override
    public void update(DocumentSnapshot ds) {
        gsm.setPlayerCount((Map) ds.get(PLAYERS));
    }
}