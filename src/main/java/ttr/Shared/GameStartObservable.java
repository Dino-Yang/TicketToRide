package ttr.Shared;

import ttr.Views.GameStartObserver;

public interface GameStartObservable {
    void notifyObservers();
    void addObserver(GameStartObserver observer);
    void removeObserver(GameStartObserver observer);
}
