package createdGameClasses;

import java.io.Serializable;
import java.util.List;

import sharedClasses.*;

public class Save implements Serializable {
    private Frame curFrame;
    private List<Decision> decisionsMade;
    private Settings settings;
    private List<Stat> stats;
    
    public Save(Frame curFrame, List<Decision> decisions, 
     Settings curSettings, List<Stat> stats) {
        this.curFrame = curFrame;
        this.decisionsMade = decisions;
        this.settings = curSettings;
        this.stats = stats;
    }
    
    public Frame getCurFrame() { return curFrame; }
    public List<Decision> getDecisionsMade() { return decisionsMade; }
    public Settings getSettings() { return settings; }
    public List<Stat> getStats() { return stats; }
    
}
