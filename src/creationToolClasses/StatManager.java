package creationToolClasses;

import java.util.List;

import sharedClasses.*;

public class StatManager {
    private StatManager() {}
    private static WIP wip = WIP.getWIP();
    
    public static List<Stat> getStats() {
        return wip.stats;
    }
    
    public static void createStat(String statName) {
        Stat newStat = new Stat(statName);
        wip.stats.add(newStat);
    }
    
    public static void adjustDefaultStat(String statName, int newStart) {
        wip.getStatByName(statName).setCount(newStart);
    }
    
    public static void deleteStat(String statName) {
        wip.stats.remove(wip.getStatByName(statName));
    }
    
    public static void renameStat(String curName, String newName) {
        wip.getStatByName(curName).rename(newName);
    }
}
