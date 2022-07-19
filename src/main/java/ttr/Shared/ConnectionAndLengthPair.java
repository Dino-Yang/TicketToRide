package ttr.Shared;

import ttr.Constants.Locations;

import java.util.Set;

public class ConnectionAndLengthPair {
    private Set<Locations> locationsSet;
    private Integer length;

    public ConnectionAndLengthPair(Set<Locations> locationsSet, Integer length) {
        this.locationsSet = locationsSet;
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Set<Locations> getLocationsSet() {
        return locationsSet;
    }
}
