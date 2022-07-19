package ttr.Model;

import ttr.Services.FirestoreService;
import ttr.Shared.PlayerObservable;
import ttr.Views.PlayerObserver;
import java.util.ArrayList;
import java.util.List;
import static ttr.Constants.ClientConstants.PLAYERS;

public class PlayerModel implements PlayerObservable {
    private FirestoreService fs = new FirestoreService();
    private String playerColor;
    private int playerNumber;
    private String playerName;
    private int score;
    private int trainCount = 45;
    private int stationCount = 3;
    private boolean initialisedFinalTurn = false;
    private boolean hasPaidForTrain = false;
    private ArrayList<TrainCardModel> playerHand;
    private ArrayList<TicketCardModel> playerTicketHand;
    private TrainCardDeckModel trainCardDeck;
    private TicketCardDeckModel ticketCardDeck;
    private boolean playerTurn;
    private List<PlayerObserver> observers = new ArrayList<PlayerObserver>();

    public PlayerModel() {
        ticketCardDeck = new TicketCardDeckModel();
        trainCardDeck = new TrainCardDeckModel();
        playerHand = new ArrayList<>();
        playerTicketHand = new ArrayList<>();
    }

    public TicketCardDeckModel getTicketCardDeck() {
        return ticketCardDeck;
    }

    public void updateDeck() {
        this.trainCardDeck.updateDecks();
    }

    public void awardPoints(int trainAmount) {
        if (trainAmount == 1) {
            this.score += 1;
        } else if (trainAmount == 2) {
            this.score += 2;
        } else if (trainAmount == 3) {
            this.score += 4;
        } else if (trainAmount == 4) {
            this.score += 7;
        } else if (trainAmount == 6) {
            this.score += 15;
        } else if (trainAmount == 8) {
            this.score += 21;
        }
        notifyObservers();
    }//updates score based on the amount of trains placed

    public void pullCard() {
        ArrayList<TrainCardModel> hulpList = trainCardDeck.pullCards();
        playerHand.addAll(hulpList);
        notifyObservers();
    }

    public void reduceTrainCount(int trainAmount) {
        trainCount = trainCount - trainAmount;
        notifyObservers();
    }

    public void addCardsToTicketHand(ArrayList<TicketCardModel> addHand) {
        this.playerTicketHand.addAll(addHand);
        notifyObservers();
    }

    public ArrayList<TicketCardModel> getPlayerTicketHand() {
        return playerTicketHand;
    }

    public void reduceStationCount(int stationAmount) {
        stationCount = stationCount - stationAmount;
        notifyObservers();
    }

    public ArrayList<TrainCardModel> getTrainCardDeck() {
        return trainCardDeck.getTrainCardDeck();
    }

    public int getDeckSize() {
        return trainCardDeck.getDeckCount();
    }

    public int getTrainCount() {
        return trainCount;
    }

    public int getStationCount() {
        return stationCount;
    }

    public ArrayList<TrainCardModel> getPlayerHand() {
        return playerHand;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        fs.updateField(PLAYERS, ("player_" + this.playerNumber), playerName);
    }

    public boolean hasInitialisedFinalTurn() {
        return initialisedFinalTurn;
    }

    public void setInitialisedFinalTurn(boolean initialisedFinalTurn) {
        this.initialisedFinalTurn = initialisedFinalTurn;
    }


    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void notifyObservers() {
        for (PlayerObserver observer : this.observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(PlayerObserver observer) {
        this.observers.add(observer);
        this.notifyObservers();
    }

    @Override
    public void removeObserver(PlayerObserver observer) {

    }

    public void setHasPaidForTrain(boolean hasPaidForTrain) {
        this.hasPaidForTrain = hasPaidForTrain;
    }

    public void setScore(int i) {
        this.score = i;
    }
}
