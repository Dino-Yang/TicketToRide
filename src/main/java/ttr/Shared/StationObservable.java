package ttr.Shared;

import ttr.Views.StationObserver;

public interface StationObservable {
    void notifyObservers();
    void addObserver(StationObserver observer);
    void removeObserver(StationObserver observer);
}
