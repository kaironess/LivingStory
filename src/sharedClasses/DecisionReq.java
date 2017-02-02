package sharedClasses;

import java.util.*;

public class DecisionReq implements Requirement {

    private ArrayList<Decision> neededDecisions;
    private ArrayList<Decision> currDecisions;
    
    public DecisionReq(ArrayList<Decision> currDec) {
        this.currDecisions = currDec;
    }
    
    @Override
    public boolean isFulfilled(List<Stat> stats) {
        // Check if all decisions are there
        return false;
    }

}
