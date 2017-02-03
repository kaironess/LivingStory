package sharedClasses;

import java.util.*;

public class DecisionReq implements Requirement {

    private ArrayList<Decision> neededDecisions;
    private ArrayList<Decision> currDecisions;
    
    public DecisionReq(ArrayList<Decision> currDec) {
        this.currDecisions = currDec;
    }
    
    private ArrayList<Integer> getNeededId() {
        // Get all the IDs of the needed decisions for later use
        ArrayList<Integer> allId = new ArrayList<Integer>();
        for (Decision d : neededDecisions) {
            allId.add(d.getId());
        }
        return allId;
    }
    
    @Override
    public boolean isFulfilled(List<Stat> stats) {
        boolean check = true;
        ArrayList<Integer> neededId = getNeededId();
        
        for (Decision d : currDecisions) {
            int index = neededId.indexOf(d.getId());
            if (index < 0) {
                check = false;
            }
        }
        return check;
    }

}
