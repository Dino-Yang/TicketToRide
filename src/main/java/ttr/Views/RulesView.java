package ttr.Views;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import ttr.Controllers.GameLoginController;

public class RulesView {
    public ScrollPane scrollpane;
    private GameLoginController glc = new GameLoginController();

    public void backClicked(MouseEvent event) {
        glc.loadFile(event, "game_login.fxml");

    }
}
