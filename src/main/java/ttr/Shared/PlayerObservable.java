package ttr.Shared;

import ttr.Views.PlayerObserver;

public interface PlayerObservable {
    void notifyObservers();
    void addObserver(PlayerObserver observer);
    void removeObserver(PlayerObserver observer);
}
