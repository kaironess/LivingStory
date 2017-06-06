package createdGameClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import sharedClasses.*;

public class Save implements Serializable {
    static final long serialVersionUID = 234531L;
    
    private Frame curFrame;
    private List<Decision> decisionsMade;
    private Settings settings;
    private List<Stat> stats;
    private HashMap<Music, Music.State> musicStatus;
    
    public Save(Frame curFrame, List<Decision> decisions, 
     Settings curSettings, List<Stat> stats, HashMap<Music, Music.State> musicStatus) {
        this.curFrame = curFrame;
        this.decisionsMade = decisions;
        this.settings = curSettings;
        this.stats = stats;
        this.musicStatus = musicStatus;
    }
    
    public Frame getCurFrame() { return curFrame; }
    public List<Decision> getDecisionsMade() { return decisionsMade; }
    public Settings getSettings() { return settings; }
    public List<Stat> getStats() { return stats; }
    public HashMap<Music, Music.State> getMusicStatus() { return musicStatus; } 
}
