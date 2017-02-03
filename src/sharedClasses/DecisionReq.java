package sharedClasses;

import java.util.*;

public class DecisionReq implements Requirement {

    private ArrayList<Decision> neededDecisions;
    private ArrayList<Decision> currDecisions;
    
    public DecisionReq(ArrayList<Decision> currDec) {
        this.currDecisions = currDec;
    }
    
    private ArrayList<Integer> getNeededId(List<Decision> myDec) {
        // Get all the IDs of the needed decisions for later use
        ArrayList<Integer> allId = new ArrayList<Integer>();
        for (Decision d : myDec) {
            allId.add(d.getId());
        }
        return allId;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean isFulfilled(List<?> pastDec) {
        boolean check = true;
        ArrayList<Integer> neededId = getNeededId((List<Decision>)(Object)pastDec);
        
        for (Decision d : currDecisions) {
            int index = neededId.indexOf(d.getId());
            if (index < 0) {
                check = false;
            }
        }
        return check;
    }

}
