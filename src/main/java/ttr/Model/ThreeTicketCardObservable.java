package ttr.Model;

import ttr.Views.OpenCardObserver;
import ttr.Views.TicketCardObserver;

public interface ThreeTicketCardObservable {
    void notifyObservers();

    void addObserver(TicketCardObserver ticketCardObserver);

    void removeObserver();
}
