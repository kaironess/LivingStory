package sharedClasses;

import java.util.*;

public class Frame {
    private int bg;
    private ArrayList<DisplayChar> curChars;
    private String dialog;
    private Frame prevFrame;
    private ArrayList<Decision> dialogOptions;
    private ArrayList<Decision> nextDecisions;
    private ArrayList<StatChange> statChanges;
    
    public Frame(Frame prev) {
        // default so the user can update it?
        this.bg = -1;
        this.curChars = new ArrayList<DisplayChar>();
        this.dialog = "";
        this.prevFrame = prev;
        this.dialogOptions = new ArrayList<Decision>();
        this.nextDecisions = new ArrayList<Decision>();
        this.statChanges = new ArrayList<>();
    }
    
    public int getBG() {
        return bg;
    }
    
    public ArrayList<DisplayChar> getChars() {
        return curChars;
    }
    
    public String getDialog() {
        return dialog;
    }
    
    public Frame getPrevFrame() {
        return prevFrame;
    }
    
    public ArrayList<Decision> getDialogOptions() {
        return dialogOptions;
    }
    
    public ArrayList<Decision> getNextDecisions() {
        return nextDecisions;
    }
    
    public void setBG(int index) {
        this.bg = index;
    }
    
    public void addChar(DisplayChar character) {
        curChars.add(character);
    }
    
    public void setDialog(String message) {
        this.dialog = message;
    }
    
    public void setPrev(Frame previous) {
        this.prevFrame = previous;
    }
    
    public void addDialog(Decision option) {
        dialogOptions.add(option);
    }
    
    public void addDecision(Decision next) {
        nextDecisions.add(next);
    }
    
    public void addStatChange(StatChange sc) {
        statChanges.add(sc);
    }

    public void applyStatChanges(List<Stat> stats) {
        for (StatChange sc : statChanges) 
            sc.updateStats(stats);
    }
}
