package ttr.Views;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import ttr.Constants.ColorConstants;
import ttr.Controllers.BoardController;
import ttr.Model.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.*;

public class BoardView implements PlayerObserver, OpenCardObserver, TrainObserver, FirebaseObserver, StationObserver,
        TicketCardObserver {
    public ImageView Card_1;
    public ImageView Card_2;
    public ImageView Card_3;
    public ImageView Card_4;
    public ImageView Card_5;
    public HBox PlayerHandHbox;
    public VBox PlayerInfoVbox;
    public HBox PlayerHandInfoHbox;
    public HBox TrainTicketDecksHbox;
    public AnchorPane ticketCardPane;
    public VBox ticketCardsVBOX;
    public ImageView goTicketButton;
    public HBox PlayerHandTicketHbox;
    private BoardController bc;
    private ArrayList<ImageView> imageview = new ArrayList();
    private ArrayList<Node> groups;

    @FXML
    private Label CurrentPlayer;

    @FXML
    private Button endGameButton;

    @FXML
    public Group groupgroup;

    @FXML
    protected void initialize() {
        this.groups = new ArrayList<>(groupgroup.getChildren());
        this.bc = BoardController.getInstance();
        Collections.addAll(imageview, Card_1, Card_2, Card_3, Card_4, Card_5);
        this.bc.register_open_card_observer(this);
        this.bc.registerPlayerObserver(this);
        this.bc.registerTrainObserver(this);
        this.bc.registerPlayerObserver(this);
        this.bc.registerFirebaseObserver(this);
        this.bc.registerStationObserver(this);
        this.bc.registerTicketObserver(this);
        Platform.runLater(() -> {
            this.bc.setOpenCards();
        });
    }

    @FXML
    private void createTrainTicketCardDeckView(PlayerModel player) {
        TrainTicketDecksHbox.getChildren().clear();
        int deckSize = player.getDeckSize();
        String trainImageUrl = "/ttr/decks/trainDeck/deck-cardLevel-" + chooseDeckImage(deckSize) + ".png";
        Image trainDeckImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(trainImageUrl)));
        ImageView trainDeckImageView = new ImageView(trainDeckImage);
        String ticketImageUrl = "/ttr/decks/ticketDeck/deck-destiLevel-60.png";
        Image ticketDeckImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ticketImageUrl)));
        ImageView ticketDeckImageView = new ImageView(ticketDeckImage);
        ticketDeckImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                pullTicketCards();
            }
        });

        trainDeckImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                bc.pullCards();
            }
        });//On mouse click event
        trainDeckImageView.setFitWidth(150);
        ticketDeckImageView.setFitWidth(150);
        TrainTicketDecksHbox.getChildren().addAll(trainDeckImageView, ticketDeckImageView);
    }//Creates an image of the TrainCardDeck and TicketCardDeck, so that you can pull/pick cards from

    public void pullTicketCards() {
        bc.getThreeTicketCards();
        ticketCardPane.setVisible(true);
    }

    public void closeCardView() {
        ticketCardPane.setVisible(false);
    }

    public void updateTicketView(ArrayList<TicketCardModel> list) {
        ArrayList<Node> listOfChosenCards = new ArrayList<>();
        ColorAdjust greyOut = new ColorAdjust();
        greyOut.setSaturation(-1);
        goTicketButton.setEffect(greyOut);
        ticketCardsVBOX.getChildren().clear();

        for (TicketCardModel ticket : list) {
            String dest1 = ticket.getFirstDestString();
            String dest2 = ticket.getSecondDestString();
            String imageUrl = "/ttr/cards/tickets/horizontal/eu-" + dest1 + "-" + dest2 + ".png";
            Image ticketImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrl)));
            ImageView ticketView = new ImageView(ticketImage);
            ticketView.setId(dest1 + "_" + dest2);
            ticketView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    highlightCard(ticketView);
                    if (listOfChosenCards.contains(ticketView)) {
                        listOfChosenCards.remove(ticketView);
                        if (listOfChosenCards.size() == 0) {
                            goTicketButton.setEffect(greyOut);
                        }
                    } else {
                        listOfChosenCards.add(ticketView);
                        if (goTicketButton.getEffect() == greyOut) {
                            goTicketButton.setEffect(null);
                        }
                        if (listOfChosenCards.size() != 0) {
                            goTicketButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                                @Override
                                public void handle(MouseEvent event) {
                                    goTicketButtonPress(listOfChosenCards);
                                }
                            });
                        }
                    }
                }
            });
            ticketCardsVBOX.getChildren().add(ticketView);
        }
    }

    public void goTicketButtonPress(ArrayList<Node> listOfChosenCards) {
        bc.addTickets(listOfChosenCards);
        ticketCardPane.setVisible(false);
    }

    public void highlightCard(ImageView imageView) {
        Glow glow = new Glow(0.3);
        if (imageView.getEffect() == null) {
            imageView.setEffect(glow);
        } else {
            imageView.setEffect(null);
        }
    }

    public String chooseDeckImage(int deckSize) {
        if (deckSize > 70) {
            return "100";
        } else if (deckSize > 40) {
            return "70";
        } else if (deckSize > 10) {
            return "40";
        } else {
            return "10";
        }
    }//Helper function to decide what image to use for the deck

    @FXML
    private void createPlayerInfoVbox(PlayerModel player) {
        HBox stationHBox = new HBox();
        HBox trainHBox = new HBox();
        Label stationLabel = new Label(" X " + player.getStationCount());
        stationLabel.setFont(new Font(20));
        Label trainLabel = new Label(" X " + player.getTrainCount());
        trainLabel.setFont(new Font(20));
        PlayerInfoVbox.getChildren().clear();
        String stationUrl = "/ttr/station/station-" + player.getPlayerColor() + ".png";
        Image stationImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(stationUrl)));
        ImageView stationImageView = new ImageView(stationImage);
        String trainUrl = "/ttr/trains/train-" + player.getPlayerColor() + "-Claimed.png";
        Image trainImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(trainUrl)));
        ImageView trainImageView = new ImageView(trainImage);
        trainImageView.setFitWidth(50);
        stationHBox.getChildren().add(stationImageView);
        stationHBox.getChildren().add(stationLabel);
        trainHBox.getChildren().add(trainImageView);
        trainHBox.getChildren().add(trainLabel);
        stationHBox.setAlignment(Pos.CENTER);
        trainHBox.setAlignment(Pos.CENTER);
        stationLabel.setTextFill(Color.rgb(153, 88, 42));
        trainLabel.setTextFill(Color.rgb(153, 88, 42));
        String swapCardHandUrl = "/ttr/menu/swapHand.png";
        Image swapHandImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(swapCardHandUrl)));
        ImageView swapHandView = new ImageView(swapHandImage);
        swapHandView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                changeHands(mouseEvent.getSource());
            }
        });
        PlayerInfoVbox.getChildren().addAll(stationHBox, trainHBox, swapHandView);
    }//dynamically creates the view of amount of Stations and Trains the player has left

    @FXML
    private void changeHands(Object mouseEvent) {
        if (PlayerHandHbox.isVisible()) {
            PlayerHandHbox.setVisible(false);
            PlayerHandTicketHbox.setVisible(true);
            this.bc.updateView();
        } else {
            PlayerHandTicketHbox.setVisible(false);
            PlayerHandHbox.setVisible(true);
            this.bc.updateView();
        }
    }

    @FXML
    private void createPlayerTicketHand(PlayerModel player) {
        ColorAdjust greyOut = new ColorAdjust();
        greyOut.setSaturation(-1);
        PlayerHandTicketHbox.getChildren().clear();
        for (TicketCardModel ticket : player.getPlayerTicketHand()) {
            VBox cardBox = new VBox();
            String loc1 = ticket.getFirstDestString();
            String loc2 = ticket.getSecondDestString();
            String imgUrl = "/ttr/cards/tickets/vertical/eu-" + loc1 + "-" + loc2 + ".png";
            Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgUrl)));
            ImageView cardImageView = new ImageView(cardImg);
            cardImageView.setFitWidth(129);
            cardImageView.setFitHeight(200);
            cardBox.getChildren().add(cardImageView);
            if (ticket.getCompleted()) {
                cardImageView.setEffect(greyOut);
            }
            PlayerHandTicketHbox.getChildren().add(cardBox);
        }
    }

    @FXML
    private void createPlayerHandHBox(PlayerModel player) {
        ColorAdjust greyOut = new ColorAdjust();
        greyOut.setSaturation(-1);
        PlayerHandHbox.getChildren().clear();
        ArrayList<String> colors = ColorConstants.getColors();
        for (String colorTypes : colors) {
            VBox cardBox = new VBox();
            Label cardCounter = new Label();
            int cardCount = 0;
            String cardColorString = colorTypes.toLowerCase();
            String url = "/ttr/cards/vertical/eu_WagonCard_" + cardColorString + ".png";
            Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)));
            ImageView cardImageView = new ImageView(cardImg);
            cardImageView.setFitWidth(100);
            cardImageView.setFitHeight(200);
            cardCounter.setFont(new Font(20));

            for (TrainCardModel card : player.getPlayerHand()) {
                if (Objects.equals(card.getCardColor(), cardColorString)) {
                    cardCount++;
                }
            }//goes through playerHand and counts how many cards there are of the color

            if (cardCount == 0) {
                cardImageView.setEffect(greyOut);
            }

            cardBox.getChildren().add(cardCounter);
            cardBox.getChildren().add(cardImageView);
            giveHoverEffect(cardImageView, cardBox, cardCounter);
            cardCounter.setText("X " + cardCount);
            cardCounter.setTextFill(Color.rgb(153, 88, 42));
            PlayerHandHbox.getChildren().add(cardBox);
        }
    }//dynamically creates the view of the playerHand

    public void giveHoverEffect(ImageView cardImageView, VBox cardBox, Label cardCounter) {
        cardImageView.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cardBox.getChildren().remove(cardCounter);
            }
        });

        cardImageView.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cardBox.getChildren().add(0, cardCounter);
            }
        });
    }//removes label from cardBox, to create a visual effect when hovering over a card

    @FXML
    public void highlight(MouseEvent event) {
        // light up event source
        Shape glowRec = (Shape) event.getSource();
        glowRec.setEffect(new Glow(1));
    }

    @FXML
    public void no_highlight(MouseEvent event) {
        // removes event source effect
        Shape glowRec = (Shape) event.getSource();
        glowRec.setEffect(null);
    }

    @FXML
    public void place_train_or_station(MouseEvent event) {
        Rectangle r = (Rectangle) event.getSource();

        bc.trainOrStation(r, event);
    }

    public void paintStation(String groupName, String color) {
        String url = "/ttr/station/station-" + color + ".png";
        Image station = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)));
        Group group = new Group();
        for (int i = 0; i < groups.size(); i++) {
            if (Objects.equals(groups.get(i).getId().toLowerCase(Locale.ROOT), groupName.toLowerCase(Locale.ROOT))) {
                group = (Group) groups.get(i);
            }
        }
        Rectangle rec = (Rectangle) group.getChildren().get(0);
        rec.setFill(new ImagePattern(station));
    }

    @FXML
    public void change_OpenCardImage(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String url = "/ttr/cards/horizontal/eu_WagonCard_" + arrayList.get(i).toString() + ".png";
            imageview.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(url))));
        }
    }

    private void showPlayerCount(String playerName) {
        Platform.runLater(() -> {
            CurrentPlayer.setText("Current Player: " + playerName);
            CurrentPlayer.setFont(new Font(5));
        });
    }

    @FXML
    public void Put_in_hand_and_replace(MouseEvent event) {
        bc.clickCard(event);
    }

    @FXML
    public void paintTrain(String groupName, String color) {
        String url = "/ttr/trains/train-" + color + "-Claimed.png";
        Image train = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)));
        for (int i = 0; i < groups.size(); i++) {
            if (Objects.equals(groups.get(i).getId().toLowerCase(Locale.ROOT), groupName.toLowerCase(Locale.ROOT))) {
                Group group = (Group) groups.get(i);
                for (Node node : group.getChildren()) {
                    Rectangle rec = (Rectangle) node;
                    if (!(rec.getFill() instanceof ImagePattern))
                        rec.setFill(new ImagePattern(train));
                }
            }
        }
    }

    private void showEndGameButton(Boolean gameFinished) {
        if (gameFinished) {
            try {
                endGameButton.setVisible(true);
            } catch (NullPointerException ignored) {

            }
        }
    }

    public void endGame(MouseEvent event) {
        this.bc.endGame(event);
    }

    @Override
    public void update(SelectOpenCardModel openCardModel) {
        change_OpenCardImage(openCardModel.getOpen_cards());
        this.bc.updateView();
    }

    @Override
    public void update(PlayerModel playerModel) {
        createPlayerInfoVbox(playerModel);
        createPlayerTicketHand(playerModel);
        createPlayerHandHBox(playerModel);
        createTrainTicketCardDeckView(playerModel);
    }

    @Override
    public void update(TrainModel trainModel) {
        paintTrain(trainModel.getGroupName(), trainModel.getColor());
    }

    @Override
    public void update(FirebaseModel firebaseModel) {
        showPlayerCount(firebaseModel.getCurrentPlayerName());
        showEndGameButton(firebaseModel.isGameFinished());
    }

    @Override
    public void update(TicketCardDeckModel ticketCardDeckModel) {
        updateTicketView(ticketCardDeckModel.getReturnHand());
    }

    @Override
    public void update(StationModel stationModel) {
        paintStation(stationModel.getGroupName(), stationModel.getColor());
    }
}
