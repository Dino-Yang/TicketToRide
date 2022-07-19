package ttr.Model;

import ttr.Shared.GameStartObservable;
import ttr.Views.GameStartObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameStartModel implements GameStartObservable {
    Map playerCount;
    private List<GameStartObserver> observers = new ArrayList<>();

    public Map getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Map map) {
        playerCount = map;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (GameStartObserver observer: observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(GameStartObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(GameStartObserver observer) {
        this.observers.remove(observer);
    }
}
