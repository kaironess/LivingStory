package creationToolClasses;

import sharedClasses.*;
import java.util.*;

import javax.imageio.ImageIO;

import createdGameClasses.GameController;
import createdGameClasses.Save;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import javafx.scene.image.Image;
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

public final class WIP implements Serializable {
    static final long serialVersionUID = 123512357876L;
    private static WIP wip = new WIP();
    
    //First few bgs are for the diff menu screens
    public enum BGIndex {MAIN_MENU, PAUSE_MENU, SETTINGS, SAVE, LOAD, GALLERY}
    
    public List<Frame> frames;
    public transient List<Image> bgs;
    public List<String> bg_paths;
    public List<Music> musics;
    public List<Stat> stats;
    public List<StoryChar> chars;
    
    public static class BadWIPException extends Exception {}
    
    private WIP() {
        frames = new LinkedList<>();
        bgs = new LinkedList<>();
        musics = new LinkedList<>();
        stats = new LinkedList<>();
        chars = new LinkedList<>();
        bg_paths = new LinkedList<>();
        
        // Add one white picture for every default menu
        for (BGIndex index : BGIndex.values()) {
            try {
                bgs.add(ImageIO.read(new File("src/creationToolClasses/whitePic.png")));
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static WIP getWIP() { return wip; }
    
    public Music getMusicByName(String name) {
        for (Music music : wip.musics) {
            if (music.getName().equals(name))
                return music;
        }
        
        return null;
    }

    public Stat getStatByName(String name) {
        for (Stat stat : wip.stats) {
            if (stat.getName().equals(name))
                return stat;
        }
        
        return null;
    }
    
    public StoryChar getCharByName(String name) {
        for (StoryChar chara : wip.chars) {
            if (chara.getName().equals(name))
                return chara;
        }
        
        return null;
    }
    
    public void deleteStat(Stat stat) {
        // Erase all StatChanges that are using this Stat
        for (Frame frame : wip.frames) {
            for (StatChange sc : frame.getStatChanges()) {
                if (sc.forStat(stat)) {
                    frame.getStatChanges().remove(sc);
                }
            }
        }
        
        wip.stats.remove(stat);
    }
    
    public void deleteMusic(Music music) {
        // Erase all MusicTriggers that are using this Music
        for (Frame frame : wip.frames) {
            for (MusicTrigger trigger : frame.getMusicTriggers()) {
                if (trigger.forMusic(music)) {
                    frame.getMusicTriggers().remove(trigger);
                }
            }
        }
        
        wip.musics.remove(music);
    }
    
    public static void saveWIP(String path) {
        // Trim if user entered the file extension too
        if (path.endsWith(".LSWIP")) {
            path = path.substring(0, path.length() - 6);
        }
        
        Path savePath = Paths.get(path);
        
        // Save the wip
        try {
            FileOutputStream fos = new FileOutputStream(savePath + ".LSWIP");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(wip);
            oos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadWIP(String path) throws BadWIPException {
        String filePath = path;
        
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            wip = (WIP)ois.readObject();
            
            ois.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BadWIPException();
        }
    }
    
    // Overridden so that we can serialize BufferedImages
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(bgs.size());
        
        for (Image img : bgs) {
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
        bgs = new ArrayList<Image>();
        
        for (int i = 0; i < imageCount; i++) {
            int size = in.readInt();
            
            byte[] buffer = new byte[size];
            in.readFully(buffer);
            
            bgs.add(ImageIO.read(new ByteArrayInputStream(buffer)));
        }
    }
}
