package ttr.Model;

import java.util.ArrayList;

public class RequirementModel {

    private final ArrayList<String> requirements;

    public RequirementModel(ArrayList<String> requirements) {
        this.requirements = requirements;
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }
}
