package sharedClasses;

import java.util.*;

public class StatReq implements Requirement {
    static final long serialVersionUID = 10L;

    private int neededCount;
    private String statName;
    
    public StatReq(String statName, int neededCount) {
        this.statName = statName;
        this.neededCount = neededCount;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean isFulfilled(List<?> curStats) {
        Stat relStat = null;
        
        for (Stat s : (List<Stat>)(Object)curStats) {
            if (s.getName().equals(statName))
                relStat = s;
        }
        
        return neededCount <= relStat.getCount();
    }

}
