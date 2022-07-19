package ttr.Shared;

import ttr.Views.TrainObserver;

public interface trainObservable {
    void notifyObservers();

    void addObserver(TrainObserver observer);

    void removeObserver(TrainObserver observer);
}
