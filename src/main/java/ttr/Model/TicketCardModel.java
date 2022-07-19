package ttr.Model;

import ttr.Constants.Locations;
import java.util.Locale;

public class TicketCardModel {
    private String type;
    private Locations first_Destination;
    private Locations second_Destination;
    private long rewardPoints;
    private boolean completed;

    public TicketCardModel(String type, Locations first_Destination, Locations second_Destination, long rewardPoints,
                           boolean completed) {
        this.type = type;
        this.first_Destination = first_Destination;
        this.second_Destination = second_Destination;
        this.rewardPoints = rewardPoints;
        this.completed = completed;
    }

    public Locations getFirst_Destination() {
        return first_Destination;
    }

    public Locations getSecond_Destination() {
        return second_Destination;
    }

    public long getRewardPoints() {
        return rewardPoints;
    }

    public boolean getCompleted() {
        return completed;
    }

    public String getFirstDestString() {
        return first_Destination.toString().toLowerCase(Locale.ROOT);
    }

    public String getSecondDestString() {
        return second_Destination.toString().toLowerCase(Locale.ROOT);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
