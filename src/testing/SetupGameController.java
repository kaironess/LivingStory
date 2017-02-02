package testing;

import java.util.*;
import java.awt.Image;
import javax.imageio.*;
import java.io.*;

import sharedClasses.*;
import createdGameClasses.*;

public class SetupGameController {
    
    private static String matURL = "src/testing/TestingMaterials/";
    
    public static GameController createGameController() {
        List<Image> bgs = setupBGs();
        List<Stat> stats = setupStats();
        List<Frame> frames = setupFrames();
        
        return new GameController(frames, bgs, stats);
    }
    
    private static List<Image> setupBGs() {
        List<Image> bgs = new LinkedList<>();
        
        // Images to add in for the testing game.
        String[] fileNames = {"puppers.jpg", "amSmols.jpg", "foxy.jpg", "poofball.jpg"};
        
        // Read each file as an image and add it to the list of images
        try {
            for (String file : fileNames) 
                bgs.add(ImageIO.read(new File(matURL + file)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return bgs;
    }
    
    private static List<Stat> setupStats() {
        List<Stat> stats = new LinkedList<>();
        stats.add(new Stat("STR"));
        stats.add(new Stat("DEX", 5));
        stats.add(new Stat("INT", 10));
        stats.add(new Stat("LUCK", 20));
        stats.add(new Stat("HP", 100));
        
        return stats;
    }
    
    private static List<Frame> setupFrames() {
        List<Frame> frames = new LinkedList<>();
        Frame lastFrame = null;

        // Data for creating each frame.
        String[] dialogs = {"This is the first frame.", "Second frame. Different background.",
                            "Third frame. One character.", "Fourth frame."};
        int[] bgOrder = {0, 1, 1, 2};
        
        // Create frames and add them to the overall list of frames.
        for (int i = 0; i < dialogs.length; i++) {
            // Create a new frame and set its personal details
            Frame curFrame = new Frame(lastFrame);
            curFrame.setDialog(dialogs[i]);
            curFrame.setBG(bgOrder[i]);
            // Set characters
            
            // Save the new frame to the totalList
            frames.add(curFrame);
            
            // Set links from the last previous frame to the newly created frame
            if (lastFrame != null) {
                Decision link = new Decision(lastFrame);
                link.setNextFrame(curFrame);
                lastFrame.addDecision(link);
                lastFrame = curFrame;
            }
        }

        return frames;
    }

}
