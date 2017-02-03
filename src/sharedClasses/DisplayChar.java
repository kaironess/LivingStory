package sharedClasses;

import java.awt.Image;

public class DisplayChar {
    private StoryCharacter character;
    private int imgIndex;
    private int leftMargin; //% of the screen away from left edge 
    private int height, width; //ratio between original picture height / width and screen height / width
    
    public DisplayChar(StoryCharacter character, int index) {
        this(character, index, 0, 0, 0);
    }
    
    public DisplayChar(StoryCharacter character, int index, int leftMargin, 
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
}
