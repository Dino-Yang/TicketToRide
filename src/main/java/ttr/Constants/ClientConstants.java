package ttr.Constants;

public class ClientConstants {
    String ID = new Token().getToken();
    public final static int screenX = 1380;
    public final static int screenY = 880;
    //firebase field reference constants
    public static final String BOARD_STATE = "BoardState";
    public static final String STATION = "stationColor";
    public static final String TRAIN = "trainColor";
    public static final String FINAL_SCORES = "final_scores";
    public static final String PLAYERS = "players";
    public static final String GAME_FINISHED = "game_finished";
    public static final String FINAL_TURN = "final_turn";
    public static final String CURRENT_PLAYER = "current_player";
    public static final String TRAINCARD_DECK = "TraincardDeck";
    public static final String DISCARD_DECK = "DiscardDeck";
    public static final String TICKET_DECK = "TicketDeck";
    //sfx sound reference constants
    public static final String SFX_PULLCARD = "pullCard";
    public static final String SFX_BUTTONCLICK = "buttonClick";
    public static final String SFX_PLACETRAIN = "placeTrain";
    public static final String SFX_STARTGAME = "startGame";

    public String getID() {
        return ID;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
}
