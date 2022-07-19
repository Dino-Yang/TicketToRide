package ttr.Model;

import ttr.Constants.Locations;

public class RouteModel {
    private Locations location1, location2;
    private int length;

    public RouteModel(Locations location1, Locations location2, int length) {
        this.location1 = location1;
        this.location2 = location2;
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    public Locations[] getLocations() {
        return new Locations[]{this.location1, this.location2};
    }
}
