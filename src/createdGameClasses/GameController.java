package createdGameClasses;

import java.awt.Frame;
import java.awt.Image;
import java.util.*;

import sharedClasses.*;

public class GameController {
    private List<Frame> frameList;
    private List<Image> bgList;
    //private List<Music> musicList;
    private int curFrame;
    private List<Stat> stats;
    private Settings settings;
    private List<Decision> decisionsMade;
    
    public GameController() {
        this.frameList = new LinkedList<>();
        this.bgList = new LinkedList<>();
        this.stats = new LinkedList<>();
        this.settings = new Settings();
        this.decisionsMade = new LinkedList<>();
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
        if (curFrame + 1 < frameList.size()) {
            curFrame++;
        }
        else {
            endGame();
        }
    }
    
    public Frame getCurFrame() {
        frameList.get(curFrame);
    }
    
    public Image getBG(int index) {
        bgList.get(index);
    }
    
    // Anything that needs to be done before ending the game and returning
    // to the main menu
    public void endGame() {}
}
