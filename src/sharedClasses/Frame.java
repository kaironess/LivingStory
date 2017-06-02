package sharedClasses;

import java.io.Serializable;
import java.util.*;

public class Frame implements Serializable {
    static final long serialVersionUID = 4L;
    
    private int bg;
    private ArrayList<DisplayChar> curChars;
    private String dialog;
    private int dialogRGB[];
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
        this.dialogRGB = new int[]{255, 255, 255};
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
    
    public int[] getDialogRGB() {
        return dialogRGB;
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
    
    public void setDialogRGB(int r, int g, int b) {
        this.dialogRGB[0] = r;
        this.dialogRGB[1] = g;
        this.dialogRGB[2] = b;
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
    
    public Decision fromID(int given) {
        Decision result = null;
        for (Decision d : dialogOptions) {
            if (d.getId() == given) {
                result = d;
                break;
            }
        }
        return result;
    }
    
    public void applyStatChanges(List<Stat> stats) {
        for (StatChange sc : statChanges) 
            sc.updateStats(stats);
    }
    
    public void applyMusicTriggers() {
        for (MusicTrigger trigger : musicTriggers) {
            System.out.println("triggered");
            trigger.trigger();
        }
    }
    
    public void addChar(DisplayChar character) { curChars.add(character); }
    public void removeChar(String charName) {
        int i;
        for (i = 0; i < curChars.size(); i++) {
            if (curChars.get(i).getCharName().equals(charName)) {
                curChars.remove(i);
                return;
            }
            i++;
        }
    }
    public ArrayList<DisplayChar> getChars() { return curChars; }
    public DisplayChar getChar(String charName) {
        int i;
        for (i = 0; i < curChars.size(); i++) {
            if (curChars.get(i).getCharName().equals(charName)) {
                return curChars.get(i);
            }
            i++;
        }
        return null;
    }
    
    public void addMusicTrigger(MusicTrigger trigger) { musicTriggers.add(trigger); }
    public List<MusicTrigger> getMusicTriggers() { return musicTriggers; }
    
    public void addStatChange(StatChange sc) { statChanges.add(sc); }
    public List<StatChange> getStatChanges() { return statChanges; }
}
