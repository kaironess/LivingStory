package sharedClasses;

import java.io.Serializable;
import java.util.*;

public class StatChange implements Serializable {
    static final long serialVersionUID = 9L;
    
    private int change;
    private String statName;
    private Frame myFrame;
    
    public StatChange(Frame myFrame, String statName, int change) {
        this.myFrame = myFrame;
        this.statName = statName;
        this.change = change;
    }
    
    public List<Stat> updateStats(List<Stat> stats) {
        for (Stat s : stats) {
            if (s.getName().equals(this.statName)) {
                s.updateStat(change);
            }
        }
        
        return stats;
    }
    
    public boolean forStat(Stat stat) {
        return stat.getName().equals(statName);
    }
}
