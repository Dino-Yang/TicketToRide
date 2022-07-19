package ttr.Shared;

import ttr.Views.FirebaseObserver;

public interface FirebaseObservable {
    void notifyObservers();
    void addObserver(FirebaseObserver observer);
    void removeObserver(FirebaseObserver observer);
}
