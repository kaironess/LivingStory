package sharedClasses;

import java.awt.Image;
import java.io.Serializable;

public class DisplayChar implements Serializable {
    static final long serialVersionUID = 3L;
    
    private StoryChar character;
    private int imgIndex;
    private int leftMargin; // pixels of the screen away from left edge 
    private int topMargin; // pixels of the screen away from the top edge
    private int height, width; //ratio between original picture height / width and screen height / width
    
    public DisplayChar(StoryChar character, int index) {
        this(character, index, 0, 0, 0, 0);
    }
    
    public DisplayChar(StoryChar character, int index, int leftMargin, int topMargin,
     int heightRatio, int widthRatio) {
        this.character = character;
        this.imgIndex = index;
        this.leftMargin = leftMargin;
        this.height = heightRatio;
        this.width = widthRatio;
    }
    
    public Image getCharImg() {
        return character.getImage(imgIndex);
    }
    
    public String getCharName() {
        return character.getName();
    }
    
    public int getLeftMargin() {
        return leftMargin;
    }
    
    public int getTopMargin() {
        return topMargin;
    }
    
    public StoryChar getStoryChar() {
        return character;
    }
}
