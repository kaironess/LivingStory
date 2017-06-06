package sharedClasses;

import java.util.*;

public class StatReq implements Requirement {
    static final long serialVersionUID = 10L;

    private int neededCount;
    private String statName;
    
    public StatReq() {}
    
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
    
    public String getStatName() {
        return statName;
    }
    
    public int getCount() {
        return neededCount;
    }
    
    public void setStatName(String statName) {
        this.statName = statName;
    }
    
    public void setCount(int count) {
        this.neededCount = count;
    }

}
