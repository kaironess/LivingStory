package sharedClasses;

import java.util.ArrayList;

public class StatReq implements Requirement {

    private int neededCount;
    private int statIndex;
    private ArrayList<Stat> stats;
    
    public StatReq(int statIndex) {
        this.statIndex = statIndex;
    }
    
    @Override
    public boolean isFulfilled() {
        // pass stats down from Game Controller?
        // check if neededCount >= current stat
        return neededCount >= stats.get(statIndex).getCount();
    }

}
