package sharedClasses;

import java.io.Serializable;
import java.util.*;

public class Frame implements Serializable {
    static final long serialVersionUID = 4L;
    
    private int bg;
    private ArrayList<DisplayChar> curChars;
    private String dialog;
    private Frame prevFrame;
    private ArrayList<Decision> dialogOptions;
    private ArrayList<Decision> nextDecisions;
    private ArrayList<StatChange> statChanges;
    private ArrayList<MusicTrigger> musicTriggers;
    
    public Frame(Frame prev) {
        // default so the user can update it?
        this.bg = -1;
        this.curChars = new ArrayList<DisplayChar>();
        this.dialog = "";
        this.prevFrame = prev;
        this.dialogOptions = new ArrayList<Decision>();
        this.nextDecisions = new ArrayList<Decision>();
        this.statChanges = new ArrayList<>();
        this.musicTriggers = new ArrayList<>();
    }
    
    public int getBG() {
        return bg;
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
    
    public void applyStatChanges(List<Stat> stats) {
        for (StatChange sc : statChanges) 
            sc.updateStats(stats);
    }
    
    public void applyMusicTriggers() {
        for (MusicTrigger trigger : musicTriggers)
            trigger.trigger();
    }
    
    public void addChar(DisplayChar character) { curChars.add(character); }
    public ArrayList<DisplayChar> getChars() { return curChars; }
    
    public void addMusicTrigger(MusicTrigger trigger) { musicTriggers.add(trigger); }
    public List<MusicTrigger> getMusicTriggers() { return musicTriggers; }
    
    public void addStatChange(StatChange sc) { statChanges.add(sc); }
    public List<StatChange> getStatChanges() { return statChanges; }
}
