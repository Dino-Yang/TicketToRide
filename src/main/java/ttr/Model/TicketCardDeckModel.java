package ttr.Model;

import ttr.Constants.Locations;
import ttr.Services.FirestoreService;
import ttr.Views.TicketCardObserver;
import java.util.*;

public class TicketCardDeckModel implements ThreeTicketCardObservable {
    private ArrayList<TicketCardModel> ticketCardDeck = new ArrayList<TicketCardModel>();
    private ArrayList<TicketCardObserver> observerlist = new ArrayList<>();
    private ArrayList<TicketCardModel> returnHand = new ArrayList<>();
    private FirestoreService fs = new FirestoreService();

    public TicketCardModel searchForTicket(String loc1, String loc2) {
        for (TicketCardModel ticket : ticketCardDeck) {
            String dest1 = ticket.getFirstDestString();
            String dest2 = ticket.getSecondDestString();
            if (Objects.equals(dest1, loc1) && Objects.equals(dest2, loc2)) {
                return ticket;
            }
        }
        return null;
    }

    public void updateTicketDeck(HashMap<String, Object> map) {
        ticketCardDeck.clear();
        for (Map.Entry<String, Object> ticket : map.entrySet()) {
            String[] locsString = ticket.getKey().toLowerCase(Locale.ROOT).split("_");
            Locations loc1 = stringToLocation(locsString[0]);
            Locations loc2 = stringToLocation(locsString[1]);
            long score = (long) ticket.getValue();
            ticketCardDeck.add(new TicketCardModel("eu", loc1, loc2, score, false));
        }
    }

    public Locations stringToLocation(String string) {
        for (Locations loc : Locations.values()) {
            if (Objects.equals(loc.toString().toLowerCase(Locale.ROOT), string)) {
                return loc;
            }
        }
        return null;
    }

    public void removeTicket(ArrayList<TicketCardModel> tickets) {
        ticketCardDeck.removeAll(tickets);
        for (TicketCardModel ticket : tickets) {
            fs.removeTicket(ticket.getFirstDestString() + "_" + ticket.getSecondDestString());
        }
    }

    public void pullThreeCards() {
        returnHand.clear();
        for (int i = 0; i < 3; i++) {
            returnHand.add(ticketCardDeck.get(i));
        }
        notifyObservers();
    }

    public ArrayList<TicketCardModel> getReturnHand() {
        return returnHand;
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < observerlist.size(); i++) {
            observerlist.get(i).update(this);
        }
    }

    @Override
    public void addObserver(TicketCardObserver boardview) {
        this.observerlist.add(boardview);
    }

    @Override
    public void removeObserver() {

    }
}
