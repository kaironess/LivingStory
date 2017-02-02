package sharedClasses;

import java.util.ArrayList;

public class Decision {
    private ArrayList<Requirement> requirements;
    private Frame nextFrame;
    private Frame myFrame;
    private String dialogChoice;
    
    public enum Type {
        DIALOG_OPTION, STATS, PREV_DECISION;
    };
    private Type type;
    
    public Decision(Frame curr) {
        // default so the user can update it?
        this.requirements = new ArrayList<Requirement>();
        this.nextFrame = null;
        this.myFrame = curr;
        this.dialogChoice = "";
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
    
    public void setNextFrame(Frame next) {
        this.nextFrame = next;
    }
    
    public void setDialog(String message) {
        this.dialogChoice = message;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public boolean satisfiesAllReqs() {
        for (Requirement r : requirements) {
            if (!r.isFulfilled()) {
                return false;
            }
        }
        
        return true;
    }

}
