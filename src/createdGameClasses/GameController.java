package createdGameClasses;

import java.awt.Image;
import java.util.*;

import sharedClasses.*;

public class GameController {
    private List<Frame> frameList;
    private List<Image> bgList;
    //private List<Music> musicList;
    private Frame curFrame;
    private List<Stat> stats;
    private Settings settings;
    private List<Decision> decisionsMade;
    
    public GameController(List<Frame> frames, List<Image> bgs, List<Stat> stats) {
        this.frameList = frames;
        this.bgList = bgs;
        this.stats = stats;
        this.settings = new Settings();
        this.decisionsMade = new LinkedList<>();
        
        if (frameList.size() > 0)
            this.curFrame = frameList.get(0);
        else
            this.curFrame = null;
    }
    
    public void loadSave(Save save) {
        this.curFrame = save.getCurFrame();
        this.stats = save.getStats();
        this.decisionsMade = save.getDecisionsMade();
        this.settings = save.getSettings();
    }
    
    public void createSave() {
        Save newSave = new Save(curFrame, decisionsMade, settings, stats);
        //TODO
        // Check if save folder exists
        // Figure out what the save file should be named
        // Save the |newSave|
    }
    
    public void nextFrame() {
        List<Decision> decs = curFrame.getNextDecisions();
        Decision activeDec = null;
        
        // Run through list and choose which decision to take
        // Prioritize earlier decisions as opposed to later ones
        for (int i = decs.size(); i > 0; i--) {
            if (decs.get(i).satisfiesAllReqs()) {
                activeDec = decs.get(i);
            }
        }
        
        if (activeDec != null && activeDec.getNextFrame() != null) {
            curFrame = activeDec.getNextFrame();
        }
        else {
            endGame();
        }
    }
    
    public Frame getCurFrame() {
        return curFrame;
    }
    
    public Image getBG(int index) {
        return bgList.get(index);
    }
    
    // Anything that needs to be done before ending the game and returning
    // to the main menu
    public void endGame() {
        System.out.println("GameController: No more frames.");
    }
}
