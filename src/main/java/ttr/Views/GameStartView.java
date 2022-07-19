package ttr.Views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ttr.Controllers.GameStartController;
import ttr.Model.GameStartModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class GameStartView implements GameStartObserver {
    private GameStartController gsc;
    private ArrayList<ImageView> playerButtons = new ArrayList<>();

    @FXML
    public VBox list;

    @FXML
    public Label optionLabel;

    @FXML
    protected ToggleGroup group;

    @FXML
    protected TextField nameField;

    @FXML
    protected Label playerCountLabel;

    @FXML
    public ImageView startButton;

    @FXML
    public Label nameLabel;

    @FXML
    protected ImageView player_1;

    @FXML
    protected ImageView player_2;

    @FXML
    protected ImageView player_3;

    @FXML
    protected ImageView player_4;

    @FXML
    protected ImageView player_5;

    @FXML
    protected void initialize() {
        this.gsc = GameStartController.getInstance();
        this.gsc.addGameStartObserver(this);
        Collections.addAll(playerButtons, player_1, player_2, player_3, player_4, player_5);
    }

    @FXML
    protected void playerSelect(MouseEvent event) {
        gsc.playerSelect(event.getPickResult().getIntersectedNode().getId());
        nameField.setVisible(true);
        nameLabel.setVisible(true);
        startButton.setVisible(true);
        optionLabel.setVisible(false);
        playerCountLabel.setVisible(false);
        for (int i = 0; i < playerButtons.size(); i++) {
            playerButtons.get(i).setVisible(false);
        }
    }

    private void showPlayerCount(int playerCount) {
        Platform.runLater(() -> {
            playerCountLabel.setText("Current Players: " + playerCount);
        });
    }

    private void hideSelectedPlayers(Map playerMap) {
        for (int i = 0; i < playerButtons.size(); i++) {
            if (playerMap.get(playerButtons.get(i).getId()) != null) {
                playerButtons.get(i).setVisible(false);
            }
        }
    }

    @FXML
    protected void startGame(MouseEvent event) throws IOException {
        gsc.playerNameSubmit(nameField);
        gsc.startGame(event);
    }

    @Override
    public void update(GameStartModel gameStartModel) {
        showPlayerCount(gameStartModel.getPlayerCount().size());
        hideSelectedPlayers(gameStartModel.getPlayerCount());
    }
}