package ttr.Controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ttr.Constants.ClientConstants;
import ttr.Constants.ColorConstants;
import ttr.Constants.Locations;
import ttr.Model.*;
import ttr.Services.*;
import ttr.Services.FirestoreService;
import ttr.Views.*;
import java.io.IOException;
import java.util.*;
import static ttr.Constants.ClientConstants.*;
import static java.lang.Math.toIntExact;

public class BoardController implements Controller {
    private final StationModel sm = new StationModel();
    private final SelectOpenCardModel som = new SelectOpenCardModel();
    private final TrainModel tm = new TrainModel();
    private final FirebaseModel fbm = new FirebaseModel();
    private final FirestoreService fs = new FirestoreService();
    private final ClientConstants cc = new ClientConstants();
    private final FirebaseModel fm = new FirebaseModel();
    private final SoundService sc;
    private final ConnectionModel cm = new ConnectionModel();
    private static BoardController boardController;
    private TicketCardDeckModel tcdm;
    private PlayerModel player;
    private int currentPlayer;
    private ArrayList<Integer> players;

    private BoardController() {
        this.sc = SoundService.getInstance();
        updatePlayerList((Map) fs.get(cc.getID()).get(PLAYERS));
    }

    public static BoardController getInstance() {
        if (boardController == null) {
            boardController = new BoardController();
        }

        return boardController;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
        this.tcdm = player.getTicketCardDeck();
        checkPlayerTurn();
    }

    public void clickCard(MouseEvent event) {
        if (this.player.isPlayerTurn()) {
            ImageView image = (ImageView) event.getSource();
            String id = image.getId();
            som.Put_in_hand_and_replace(id, player.getTrainCardDeck(), player.getPlayerHand());
        }
    }

    public void setOpenCards() {
        ArrayList<String> col = new ArrayList<>();
        try {
            while (col.size() != 5) {
                col.add(player.getTrainCardDeck().get(0).getCardColor());
                player.getTrainCardDeck().remove(0);
            }
            som.setOpen_cards(col);
        } catch (IndexOutOfBoundsException ignored) {
            setOpenCards();
        }

    }

    public void setCurrentPlayer(DocumentSnapshot ds) {
        String value = ds.get(CURRENT_PLAYER).toString();
        this.currentPlayer = Integer.parseInt(value);
    }


    public void checkPlayerTurn() {
        if (this.player.getPlayerNumber() == currentPlayer) {
            this.player.setPlayerTurn(true);
        }
        if (this.player.getPlayerNumber() != currentPlayer) {
            this.player.setPlayerTurn(false);
        }
    }

    public void updatePlayerList(Map playerMap) {
        players = new ArrayList<>();
        List<String> playerList = new ArrayList<>(playerMap.keySet());
        for (int i = 0; i < playerMap.size(); i++) {
            String[] numString = playerList.get(i).split("_");
            players.add(Integer.parseInt(numString[1]));
        }
    }

    private void finalTurnCheck() {
        String s = fs.get(cc.getID()).get(FINAL_TURN).toString();
        boolean final_turn = Boolean.parseBoolean(s);
        if (final_turn) {
            //submit score
            fm.setFinal_turn(true);
        }
        if (this.player.getTrainCount() <= 2 && !final_turn) {
            this.player.setInitialisedFinalTurn(true);
            fs.updateValue(FINAL_TURN, true);
        }
    }

    private void lastPlayerTurnCheck() {
        if (this.player.hasInitialisedFinalTurn()) {
            fs.updateValue(GAME_FINISHED, true);
        }
    }

    private void gameFinishedCheck() {
        String s = fs.get(cc.getID()).get(GAME_FINISHED).toString();
        boolean game_finished = Boolean.parseBoolean(s);
        if (game_finished) {
            this.fm.setGameFinished(true);
        }
    }

    public void endTurn() {
        if (this.player.isPlayerTurn()) {
            lastPlayerTurnCheck();
            finalTurnCheck();
            currentPlayer += 1;

            if (currentPlayer == Collections.max(players) + 1) {
                currentPlayer = 1;
            }

            while (true) {
                if (!players.contains(currentPlayer)) {
                    currentPlayer += 1;
                } else {
                    break;
                }
            }
            fbm.setCurrentPlayer(currentPlayer);
        }
    }

    public void getThreeTicketCards() {
        if (this.player.isPlayerTurn()) {
            tcdm.updateTicketDeck(fs.getTicketDeck());
            tcdm.pullThreeCards();
        }
    }

    public void pullCards() {
        if (this.player.isPlayerTurn()) {
            this.sc.playSFX(SFX_PULLCARD);
            this.player.pullCard();
            endTurn();
        }
    }

    public ArrayList<Locations> getRoute(String id) {
        String[] routes = id.split("_");
        ArrayList<Locations> locations = new ArrayList<>();
        for (Locations loc : Locations.values()) {
            if (routes[0].toLowerCase(Locale.ROOT).equals(loc.toString().toLowerCase(Locale.ROOT))) {
                locations.add(loc);
            }
            if (routes[1].toLowerCase(Locale.ROOT).equals(loc.toString().toLowerCase(Locale.ROOT))) {
                locations.add(loc);
            }
        }
        return locations;
    }

    public void checkTicketCards(ArrayList<TicketCardModel> playerTicketHand) {
        for (TicketCardModel ticketCard : playerTicketHand) {
            if (!ticketCard.getCompleted()) {
                if (this.cm.isRouteCardCompleted(ticketCard)) {
                    this.player.setScore(this.player.getScore() + toIntExact(ticketCard.getRewardPoints()));
                    ticketCard.setCompleted(true);
                }
            }
        }
    }

    public void placeTrain(String id, int size) {
        ArrayList<Locations> routes = getRoute(id);
        RouteModel route = new RouteModel(routes.get(0), routes.get(1), size);
        this.cm.addRoute(route);
        this.player.awardPoints(size);
        this.fs.updateTrainOrStation(id, TRAIN, this.player.getPlayerColor());
        this.player.reduceTrainCount(size);
        this.sc.playSFX(SFX_PLACETRAIN);
        endTurn();
        checkTicketCards(player.getPlayerTicketHand());
    }

    public void placeStation(String id, int size) {
        ArrayList<Locations> routes = getRoute(id);
        RouteModel route = new RouteModel(routes.get(0), routes.get(1), size);
        this.cm.addRoute(route);
        this.player.awardPoints(size);
        this.fs.updateTrainOrStation(id, STATION, this.player.getPlayerColor());
        this.player.reduceStationCount(1);
        endTurn();
        this.sc.playSFX("placeStation");
    }

    public void trainOrStation(Rectangle r,MouseEvent event) {
        if (fs.getTrainOrStation(r.getParent().getId(), TRAIN) == (null)) {
            Group route = (Group) r.getParent();
            payForTrain(route,event);
        } else if (fs.getTrainOrStation(r.getParent().getId(), STATION) == (null)) {
            placeStation(r.getParent().getId(), r.getParent().getChildrenUnmodifiable().size());
        }
    }

    public void endGame(MouseEvent event) {
        submitScore();
        loadFile(event, "End_Screen.fxml");
    }

    public void submitScore() {
        String playerName = this.player.getPlayerName();
        int score = this.player.getScore();

        fs.updateField(FINAL_SCORES, playerName, score);
    }

    public void checkCurrentPlayerName(HashMap<String, String> players) {
        for (Map.Entry<String, String> entry : players.entrySet()) {
            if (Objects.equals(entry.getKey(), "player_" + currentPlayer)) {
                String playerName = entry.getValue();
                fm.setCurrentPlayerName(playerName);
            }
        }
    }

    public void removeCardsFromPlayerHand(ArrayList<String> req) {
        ArrayList<TrainCardModel> hand = this.player.getPlayerHand();
        for (int i = 0; i < req.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                if (Objects.equals(req.get(i), hand.get(j).getCardColor())) {
                    hand.remove(j);
                    break;
                }
            }
        }
    }

    public void addTickets(ArrayList<Node> list) {
        tcdm.updateTicketDeck(fs.getTicketDeck());
        ArrayList<TicketCardModel> addHand = new ArrayList<>();
        for (Node ticket : list) {
            String[] tickets = ticket.getId().split("_");
            addHand.add(tcdm.searchForTicket(tickets[0], tickets[1]));
        }
        if (addHand != null) {
            this.player.addCardsToTicketHand(addHand);
            tcdm.removeTicket(addHand);
        }
        endTurn();
    }

    public void checkBoardState() {
        HashMap<Object, HashMap> boardState = fs.getBoardState();

        for (Map.Entry<Object, HashMap> entry : boardState.entrySet()) {
            String key = (String) entry.getKey();
            key = key.toLowerCase(Locale.ROOT);
            HashMap map = entry.getValue();
            if (map.get(TRAIN) != null) {
                this.tm.placeTrain(key, map.get(TRAIN).toString());
            }
            if (map.get(STATION) != null) {
                this.sm.placeStation(key, map.get(STATION).toString());
            }
        }
    }

    public void updateView() {
        this.player.notifyObservers();
    }

    public void registerPlayerObserver(PlayerObserver boardView) {
        this.player.addObserver(boardView);
    }

    public void registerTicketObserver(TicketCardObserver boardView) {
        this.tcdm.addObserver(boardView);
    }

    public void registerTrainObserver(TrainObserver boardView) {
        this.tm.addObserver(boardView);
    }

    public void register_open_card_observer(OpenCardObserver boardview) {
        this.som.addObserver(boardview);
    }

    public void registerFirebaseObserver(FirebaseObserver boardview) {
        this.fm.addObserver(boardview);
    }

    public void registerStationObserver(StationObserver boardView) {
        this.sm.addObserver(boardView);
    }

    public void loadFile(MouseEvent event, String file) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ttr/fxml/" + file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, cc.getScreenX(), cc.getScreenY());
        stage.setScene(scene);
        stage.show();
    }

    public void update(DocumentSnapshot ds) {
        gameFinishedCheck();
        updatePlayerList((Map) ds.get(PLAYERS));
        checkBoardState();
        checkCurrentPlayerName((HashMap<String, String>) ds.get(PLAYERS));
        setCurrentPlayer(ds);
        checkPlayerTurn();
        this.player.updateDeck();
    }

    public void payForTrain(Group group, MouseEvent event) {
        this.player.setHasPaidForTrain(false);
        int amountOfTrains = player.getTrainCount();
        int size = group.getChildren().size();

        ArrayList<String> requirements = new ArrayList<>();

        for (int i = 0; i < group.getChildren().size(); i++) {
            Rectangle rec = (Rectangle) group.getChildren().get(i);


            ArrayList<String[]> colorCodes = ColorConstants.getColorCodes();
//        colorCode omzetten naar text.
            String color = rec.getFill().toString();
            for (int j = 0; j < colorCodes.size(); j++) {
                if (Objects.equals(colorCodes.get(j)[1], color)) {
                    String trainColor = colorCodes.get(j)[0];
                    requirements.add(trainColor);
                }
            }
        }

        if (size > amountOfTrains) {
            return;
        }

        RequirementModel requirementModel = new RequirementModel(requirements);
        TrainCardDeckController trainCardDeckController = TrainCardDeckController.getInstance();
        trainCardDeckController.setRequirement(requirementModel);
        trainCardDeckController.setRoute(group);
        trainCardDeckController.setPlayer(this.player);

        // player model -> notifyObserver die selectTrainCardView, update je hand
        loadFile(event, "selectCardsScreen.fxml");
    }
}


