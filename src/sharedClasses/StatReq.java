package sharedClasses;

import java.util.*;

public class StatReq implements Requirement {

    private int neededCount;
    private String statName;
    
    public StatReq(String statName, int neededCount) {
        this.statName = statName;
        this.neededCount = neededCount;
    }
    
    @Override
    public boolean isFulfilled(List<Stat> curStats) {
        Stat relStat = null;
        
        for (Stat s : curStats) {
            if (s.getName().equals(statName))
                relStat = s;
        }
        
        return neededCount <= relStat.getCount();
    }

}
