package sharedClasses;

import java.awt.Image;

public class DisplayChar {
    private StoryCharacter character;
    private int imgIndex;
    private int leftMargin; //% of the screen away from left edge 
    private int height, width; //ratio between original picture height / width and screen height / width
    
    public DisplayChar(StoryCharacter character, int index) {
        this.character = character;
        this.imgIndex = index;
    }
    
    public Image getCharImg() {
        return character.getImage(imgIndex);
    }
    
    public String getCharName() {
        return character.getName();
    }
}
