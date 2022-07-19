package ttr.Model;

import ttr.Constants.Locations;
import ttr.Shared.ConnectionAndLengthPair;
import java.util.*;

public class ConnectionModel {
    private List<ConnectionAndLengthPair> connections = new ArrayList<>();
    private final int NOT_A_SET = -1;

    public void addLocations(Locations location1, Locations location2, int length) {
        int indexSet1 = getSetForLocation(location1);
        int indexSet2 = getSetForLocation(location2);
        if (isMergeable(indexSet1, indexSet2)) {
            mergeSets(indexSet1, indexSet2);
        } else if (isAddable(indexSet1)) {
            addLocationToSet(indexSet1, location2, length);
        } else if (isAddable(indexSet2)) {
            addLocationToSet(indexSet2, location1, length);
        } else {
            createNewSet(location1, location2, length);
        }
    }//Adds a route to the Model through the use of the 2 locations, who connect the route.

    public void addRoute(RouteModel route) {
        Locations[] locs = route.getLocations();
        addLocations(locs[0], locs[1], route.getLength());
    }//Adds the locations within a route to the ConnectionModel by making use of addLocations


    private boolean isMergeable(int set1, int set2) {
        return set1 != set2 && set1 != NOT_A_SET && set2 != NOT_A_SET;
    }

    private boolean isAddable(int set) {
        return set != NOT_A_SET;
    }

    private int getSetForLocation(Locations location1) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getLocationsSet().contains(location1)) {
                return i;
            }
        }
        return NOT_A_SET;
    }//returns the index of the set that a given location is in.

    private void addLocationToSet(int set, Locations loc, int length) {
        connections.get(set).getLocationsSet().add(loc);
        connections.get(set).setLength(connections.get(set).getLength() + length);
    }//Adds a Location to a given set

    private void createNewSet(Locations location1, Locations location2, int length) {
        Set<Locations> newSet = new HashSet<>();
        newSet.add(location1);
        newSet.add(location2);
        connections.add(new ConnectionAndLengthPair(newSet, length));
    }//Creates a new set for the given connection

    private void mergeSets(int set1, int set2) {
        Set<Locations> locSet1 = connections.get(set1).getLocationsSet();
        Set<Locations> locSet2 = connections.get(set2).getLocationsSet();
        int set1Length = connections.get(set1).getLength();
        int set2Length = connections.get(set2).getLength();
        locSet1.addAll(locSet2);
        connections.get(set1).setLength(set1Length + set2Length);
        connections.remove(set2);
    }//Merges 2 sets of locations and length with each other

    public boolean checkForRouteCompleted(Locations location1, Locations location2) {
        int set1 = getSetForLocation(location1);
        int set2 = getSetForLocation(location2);
        return set1 == set2 && set1 != NOT_A_SET;
    }//Checks whether 2 locations are connected or not

    public boolean isRouteCardCompleted(TicketCardModel ticketCard) {
        return checkForRouteCompleted(ticketCard.getFirst_Destination(), ticketCard.getSecond_Destination());
    }
}

