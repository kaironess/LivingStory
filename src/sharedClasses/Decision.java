package sharedClasses;

import java.io.Serializable;
import java.util.*;

public class Decision implements Serializable {
    static final long serialVersionUID = 1L;
    
    private ArrayList<Requirement> requirements;
    private Frame nextFrame;
    private Frame myFrame;
    private String dialogChoice;
    private int id;
    
    static int decisionId = 0;
    
    public Decision(Frame curr) {
        // default so the user can update it?
        this.requirements = new ArrayList<Requirement>();
        this.nextFrame = null;
        this.myFrame = curr;
        this.dialogChoice = "";
        this.id = decisionId++;
    }
    
    public int getId() {
        return id;
    }
    
    public int decisionNum() {
        return decisionId;
    }
    
    public ArrayList<Requirement> getReqs() {
        return requirements;
    }
    
    public Frame getNextFrame() {
        return nextFrame;
    }
    
    public Frame getCurrFrame() {
        return myFrame;
    }
    
    public String getDialog() {
        return dialogChoice;
    }
    
    public void addReq(Requirement re) {
        this.requirements.add(re);
    }
    
    public void removeReq(Requirement re) {
        this.requirements.remove(re);
    }
    
    public void setNextFrame(Frame next) {
        this.nextFrame = next;
    }
    
    public void setDialog(String message) {
        this.dialogChoice = message;
    }
    
    public boolean satisfiesAllReqs(List<Stat> curStats) {
        for (Requirement r : requirements) {
            if (!r.isFulfilled(curStats)) {
                return false;
            }
        }
        
        return true;
    }

}
