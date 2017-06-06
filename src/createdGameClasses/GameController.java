package createdGameClasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.util.*;

import javax.imageio.ImageIO;

import sharedClasses.*;

public class GameController implements Serializable {
    static final long serialVersionUID = 0L;
    
    // Save folder path
    private String savePath = System.getProperty("user.dir") + File.separator + "saves" + File.separator;
    
    //First few bgs are for the diff menu screens
    public enum BGIndex {MAIN_MENU, PAUSE_MENU, SETTINGS, SAVE, LOAD, GALLERY}
    private static final int bgOffset = BGIndex.values().length;
    private List<Frame> frameList;
    private transient List<Image> bgList; 
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
    
    // Stop the game music
    public void stop() {
        for (Music music : musicList) 
            music.stop();
    }
    
    public void start() {
        this.curFrame.applyStatChanges(this.stats);
        this.curFrame.applyMusicTriggers();
    }
    
    // Resets the game 
    public void reset() {
        
        if (frameList.size() > 0) {
            this.curFrame = frameList.get(0);
        }
        else
            this.curFrame = null;
        
        this.settings = new Settings();
        this.decisionsMade = new LinkedList<>();
    }
    
    public void createSave(String saveName) {
        Save newSave = new Save(curFrame, decisionsMade, settings, stats);
        Path savePath = Paths.get(this.savePath);
        
        // Make the save folder if it doesn't exist
        if (!Files.exists(savePath)) {
            boolean madeSaveFile = new File(savePath.toString()).mkdirs();
            
        }
        
        // Save the file
        try {
            FileOutputStream fos = new FileOutputStream(savePath + File.separator + saveName +  ".save");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(newSave);
            oos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadSave(String fileName) {
        String filePath = savePath + fileName;
        
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            Save save = (Save)ois.readObject();
            
            curFrame = save.getCurFrame();
            decisionsMade = save.getDecisionsMade();
            settings = save.getSettings();
            stats = save.getStats();
            
            ois.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Returns all .save filenames in the saves folder, without the extension
    public List<String> getSaveNames() {
        ArrayList<String> savesList = new ArrayList<>();
        
        Path savePath = Paths.get(this.savePath);
        File savesFolder = null;
        
        // Figure out if the saves folder even exists
        if (Files.exists(savePath)) 
            savesFolder = new File(savePath.toString());

        // If it does, find all the saves
        if (savesFolder != null) {
            File[] saves = savesFolder.listFiles();
            
            Arrays.sort(saves, new Comparator<File>(){
                public int compare(File f1, File f2)
                {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                } 
            });
            
            for (File save : saves) {
                String saveName = save.getName();
                int extIndex = saveName.lastIndexOf('.');
                String ext = saveName.substring(extIndex + 1);
                if (ext.equals("save")) {
                    savesList.add(saveName.substring(0, extIndex));
                }
            }
        }     
        
        return savesList;
    }
    
    public void setMusicVol(double newVol) {
        for (Music music : this.musicList)
            music.setVol(newVol);
        this.settings.musicVol = (double) newVol;
    }
    
    public void choseDialog(Decision dec) {
        if (dec.getCurrFrame().equals(curFrame) && dec.getNextFrame() != null) {
            this.decisionsMade.add(dec);
            curFrame = dec.getNextFrame();
            curFrame.applyStatChanges(this.stats);
            curFrame.applyMusicTriggers();
        }
        // Otherwise if we cannot progress, end the game
        else {
            endGame();
        }
    }
    
    public void nextFrame() {
        // Only auto proceed to a new frame if there are no dialog options
        if (curFrame.getDialogOptions().size() == 0) {
            List<Decision> decs = curFrame.getNextDecisions();
            Decision activeDec = null;
            
            // Run through list and choose which decision to take
            // Prioritize earlier decisions as opposed to later ones
            for (int i = decs.size() - 1; i >= 0; i--) {
                if (decs.get(i).satisfiesAllReqs(this.stats, this.decisionsMade)) {
                    activeDec = decs.get(i);
                }
            }
            
            // Get the next frame from the decision made
            if (activeDec != null && activeDec.getNextFrame() != null) {
                this.decisionsMade.add(activeDec);
                curFrame = activeDec.getNextFrame();
                curFrame.applyStatChanges(this.stats);
                curFrame.applyMusicTriggers();
            }
            // Otherwise if we cannot progress, end the game
            else {
                endGame();
            }
        }
    }
    
    public Frame getCurFrame() {
        return curFrame;
    }
    
    public Image getBG(BGIndex bg) {
        return bgList.get(bg.ordinal());
    }
    
    public Image getCurBG() {
        // For nullified backgrounds
        if (curFrame.getBG() < 0)
            try {
                return ImageIO.read(new File("src" + File.separator + "createdGameClasses" 
                 + File.separator + "whitePic.png"));
            } catch (IOException e) {  e.printStackTrace(); }
        System.out.println(curFrame.getBG());
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
            ImageIO.write((RenderedImage)img, "png", buffer);
            
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
