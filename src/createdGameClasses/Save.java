package createdGameClasses;

import java.awt.Frame;
import java.util.List;

import sharedClasses.*;

public class Save {
    private int curFrame;
    private List<Decision> decisionsMade;
    private Settings curSettings;
    private List<Stat> stats;
    
    public Save(Frame curFrame, List<Decision> decisions, 
     Settings curSettings, List<Stat> stats) {
        this.curFrame = curFrame;
        this.decisionsMade = decisions;
        this.curSettings = curSettings;
        this.stats = stats;
    }
}
