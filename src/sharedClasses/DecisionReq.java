package sharedClasses;

import java.util.ArrayList;

public class DecisionReq implements Requirement {

    private ArrayList<Decision> neededDecisions;
    private ArrayList<Decision> currDecisions;
    
    public DecisionReq(ArrayList<Decision> currDec) {
        this.currDecisions = currDec;
    }
    
    @Override
    public boolean isFulfilled() {
        // Check if all decisions are there
        return false;
    }

}
