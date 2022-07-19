package ttr.Model;

import ttr.Views.OpenCardObserver;

public interface OpenCardObservable {
    void notifyObservers();
    void addObserver(OpenCardObserver openCardObserver);
    void removeObserver();
}
