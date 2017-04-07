package createdGameClasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.util.*;

import javax.imageio.ImageIO;

import sharedClasses.*;

public class GameController implements Serializable {
    static final long serialVersionUID = 0L;
    
    //First few bgs are for the diff menu screens
    public enum BGIndex {MAIN_MENU, PAUSE_MENU, SETTINGS, SAVE, LOAD, GALLERY}
    private static final int bgOffset = BGIndex.values().length;
    private List<Frame> frameList;
    private transient List<Image> bgList; //First few are for the diff menu screens
    private List<Music> musicList;
    private Frame curFrame;
    private List<Stat> stats;
    private Settings settings;
    private List<Decision> decisionsMade;
    
    public GameController(List<Frame> frames, List<Image> bgs, List<Stat> stats, List<Music> music) {
        this.frameList = frames;
        this.bgList = bgs;
        this.musicList = music;
        this.stats = stats;
        this.settings = new Settings();
        this.decisionsMade = new LinkedList<>();
        
        if (frameList.size() > 0)
            this.curFrame = frameList.get(0);
        else
            this.curFrame = null;
    }
    
    // Things to do when the game is first turned on
    public void setup() {
        System.out.println("Game has been started.");
    }
    
    public void loadSave(Save save) {
        this.curFrame = save.getCurFrame();
        this.stats = save.getStats();
        this.decisionsMade = save.getDecisionsMade();
        this.settings = save.getSettings();
    }
    
    public void createSave() {
        Save newSave = new Save(curFrame, decisionsMade, settings, stats);
//        TODO
//         Check if save folder exists
//         Figure out what the save file should be named
//         Save the |newSave|
    }
    
    public void nextFrame() {
        List<Decision> decs = curFrame.getNextDecisions();
        Decision activeDec = null;
        
        // Run through list and choose which decision to take
        // Prioritize earlier decisions as opposed to later ones
        for (int i = decs.size() - 1; i >= 0; i--) {
            if (decs.get(i).satisfiesAllReqs(this.stats)) {
                activeDec = decs.get(i);
            }
        }
        
        // Get the next frame from the decision made
        if (activeDec != null && activeDec.getNextFrame() != null) {
            this.decisionsMade.add(activeDec);
            curFrame = activeDec.getNextFrame();
            curFrame.applyStatChanges(this.stats);
        }
        // Otherwise if we cannot progress, end the game
        else {
            endGame();
        }
    }
    
    public Frame getCurFrame() {
        return curFrame;
    }
    
    public Image getBG(BGIndex bg) {
        return bgList.get(bg.ordinal());
    }
    
    public Image getCurBG() {
        return bgList.get(curFrame.getBG() + bgOffset);
    }
    
    // Anything that needs to be done before ending the game and returning
    // to the main menu
    public void endGame() {
        System.out.println("GameController: No more frames.");
    }
    
    // Overridden so that we can serialize BufferedImages
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(bgList.size());
        
        for (Image img : bgList) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)img, "jpg", buffer);
            
            out.writeInt(buffer.size());
            buffer.writeTo(out);
        }
    }
    
    // Overridden so that we can serialize BufferedImages
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        int imageCount = in.readInt();
        bgList = new ArrayList<Image>();
        
        for (int i = 0; i < imageCount; i++) {
            int size = in.readInt();
            
            byte[] buffer = new byte[size];
            in.readFully(buffer);
            
            bgList.add(ImageIO.read(new ByteArrayInputStream(buffer)));
        }
    }
}
