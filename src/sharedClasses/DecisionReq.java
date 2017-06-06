package sharedClasses;

import java.io.Serializable;
import java.util.*;

public class DecisionReq implements Requirement, Serializable {
    static final long serialVersionUID = 2L;

    public ArrayList<Decision> neededDecisions;
    private ArrayList<Decision> currDecisions;
    
    public DecisionReq(ArrayList<Decision> currDec) {
        this.neededDecisions = currDec;
    }
    
    public DecisionReq(Decision currDec) {
        neededDecisions = new ArrayList<Decision>();
        neededDecisions.add(currDec);
    }
    
    public boolean needsDecision(Decision dec) {
        return neededDecisions.contains(dec);
    }
    
    public List<Decision> getNeededDecisions() {
        return neededDecisions;
    }
    
    public ArrayList<Integer> getCurrDecReq() {
        return getNeededId(currDecisions);
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
        
        for (Decision d : neededDecisions) {
            if (!((List<Decision>)(Object)pastDec).contains(d))
                return false;
        }
        return check;
    }

}
