package ttr.Model;

import ttr.Shared.StationObservable;
import ttr.Views.StationObserver;
import java.util.ArrayList;
import java.util.List;

public class StationModel implements StationObservable {
    private List<StationObserver> observers = new ArrayList<StationObserver>();

    private String color;
    private String groupName;

    public String getColor() {
        return color;
    }

    public String getGroupName() {
        return groupName;
    }

    public void placeStation(String rectangleGroupName, String stationColor){
        // eerst firebase hier stop ik station;
        this.color = stationColor;
        this.groupName = rectangleGroupName;

        // daarna een update naar de view;
        this.notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (StationObserver observer: this.observers){
            observer.update(this);
        }
    }

    @Override
    public void addObserver(StationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(StationObserver observer) {
        observers.remove(observer);
    }
}


