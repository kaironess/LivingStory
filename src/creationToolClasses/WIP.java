package creationToolClasses;

import sharedClasses.*;
import java.util.*;

import javax.imageio.ImageIO;

import createdGameClasses.GameController;

import java.awt.Image;
//import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;

public final class WIP {
    private static WIP wip = new WIP();
    
    //First few bgs are for the diff menu screens
    public enum BGIndex {MAIN_MENU, PAUSE_MENU, SETTINGS, SAVE, LOAD, GALLERY}
    
    public List<Frame> frames;
    public List<Image> bgs;
    public List<Music> musics;
    public List<Stat> stats;
    public List<StoryChar> chars;
    
    private WIP() {
        frames = new LinkedList<>();
        bgs = new LinkedList<>();
        musics = new LinkedList<>();
        stats = new LinkedList<>();
        chars = new LinkedList<>();
        
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
}
