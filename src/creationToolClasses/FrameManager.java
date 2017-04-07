package creationToolClasses;

import sharedClasses.*;
import java.util.*;

public class FrameManager {
    private WIP wip = WIP.getWIP();
    private static Frame curFrame;
    
    public static void createFrame() {
        //Frame newFrame = new Frame();
    }
    
    public static void setCurFrame(Frame newFrame) { curFrame = newFrame; }
    public static Frame getCurFrame() { return curFrame; }
    
    public static void editBG(int index) { curFrame.setBG(index); }
    public static void editDialog(String dialog) { curFrame.setDialog(dialog); }

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
    
    public static void addCharacter() {}
    public static void removeCharacter() {}
    public static void moveCharacter() {}
    public static List<DisplayChar> getCharacters() { return curFrame.getChars(); }
    
    public static void addMusicTrigger() {}
    public static void removeMusicTrigger() {}
    public static void updateMusicTrigger() {}
    public static List<MusicTrigger> getTriggers() { return curFrame.getMusicTriggers(); }
    
    public static void addStatChange() {}
    public static void removeStatChange() {}
    public static void updateStatChange() {}
    public static List<StatChange> getStatChanges() { return curFrame.getStatChanges(); }
}
