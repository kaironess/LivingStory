package createdGameClasses;

import java.awt.Image;
import java.util.*;
import sharedClasses.*;

public class GallerySave {
    private List<Image> bgsSeen;
    private List<DisplayChar> charsSeen;
    private List<Frame> framesSeen;
    
    public GallerySave() {
        bgsSeen = new LinkedList<>();
        charsSeen = new LinkedList<>();
        framesSeen = new LinkedList<>();
    }
    
    public void updateSave(List<Image> bgs, List<DisplayChar> charsSeen, 
     List<Frame> framesSeen) {
        this.bgsSeen = bgs;
        this.charsSeen = charsSeen;
        this.framesSeen = framesSeen;
    }
}
