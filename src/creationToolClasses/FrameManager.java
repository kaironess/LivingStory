package creationToolClasses;

import sharedClasses.*;
import java.util.*;

public class FrameManager {
    private FrameManager() {}
    
    private WIP wip = WIP.getWIP();
    private static Frame curFrame;
    private static Decision curDec;
    
    public static void createFrame() {
        //Frame newFrame = new Frame();
    }
    
    public static void setCurFrame(Frame newFrame) { curFrame = newFrame; }
    public static Frame getCurFrame() { return curFrame; }
    
    public static void setCurDec(Decision newDec) {
        curDec = newDec;
    }
    public static Decision getCurDec() { return curDec; }
    
    public static void editBG(int index) { curFrame.setBG(index); }
    public static void editDialog(String dialog) { curFrame.setDialog(dialog); }
    
    public static void editDialogRGB(int r, int g, int b) {
        curFrame.setDialogRGB(r, g, b);
    }

    public static void addDialogOption(String dialog) { 
        Decision decision = new Decision(curFrame);
        decision.setDialog(dialog);
        
        curFrame.addDialog(decision);
    }
    public static void updateDialogOption(Frame nextFrame, String dialogOption) {
        for (Decision d : curFrame.getDialogOptions()) {
            if (d.getDialog().equals(dialogOption)) {
                d.setNextFrame(nextFrame);
            }
        }
    }
    public static List<Decision> getDialogOptions() { return curFrame.getDialogOptions(); }
    
    public static Decision fromID(int given) {
        return curFrame.fromID(given);
    }
    
    public static Decision fromText(String dialog) {
        Decision result = null;
        for (Decision d : curFrame.getDialogOptions()) {
            if (d.getDialog().equals(dialog)) {
                result = d;
                break;
            }
        }
        return result;
    }
    
    public static void addCharacter(DisplayChar character) { curFrame.addChar(character); }
    public static void removeCharacter(String charName) { curFrame.removeChar(charName); }
    public static void moveCharacter() {}
    public static List<DisplayChar> getCharacters() { return curFrame.getChars(); }
    
    public static MusicTrigger addMusicTrigger(Music music) {
        MusicTrigger mt = new MusicTrigger(music, MusicTrigger.Trigger.PLAY);
        curFrame.addMusicTrigger(mt);
        return mt;
    }
    public static void removeMusicTrigger() {}
    public static void updateMusicTrigger(int trigger) {}
    public static List<MusicTrigger> getTriggers() { return curFrame.getMusicTriggers(); }
    
    public static void addStatChange(String statname) {
        StatChange sc = new StatChange(curFrame, statname, 0);
        curFrame.addStatChange(sc);
    }
    public static void removeStatChange() {}
    public static void updateStatChange(String statname, int change) {
        
    }
    public static StatChange hasStatChange(String statname) {
        StatChange result = null;
        for (StatChange sc : curFrame.getStatChanges()) {
            if (sc.getStatName().equals(statname)) {
                result = sc;
                break;
            }
        }
        return result;
    }
    public static List<StatChange> getStatChanges() { return curFrame.getStatChanges(); }
}
