package ttr.Model;

import ttr.Shared.trainObservable;
import ttr.Views.TrainObserver;
import java.util.ArrayList;
import java.util.List;

public class TrainModel implements trainObservable {
    private List<TrainObserver> observers = new ArrayList<TrainObserver>();
    private String color;
    private String groupName;
    public String getColor() {
        return color;
    }
    public String getGroupName() {
        return groupName;
    }

    public void placeTrain(String rectangleGroupName, String trainColor){
        // eerst firebase hier stop ik trein;
        this.color = trainColor;
        this.groupName = rectangleGroupName;
        // daarna een update naar de view;
        this.notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (TrainObserver observer: this.observers){
            observer.update(this);
        }
    }

    @Override
    public void addObserver(TrainObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(TrainObserver observer) {
        observers.remove(observer);
    }
}
