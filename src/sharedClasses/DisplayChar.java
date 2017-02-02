package sharedClasses;

import java.awt.Image;

public class DisplayChar {
    private StoryCharacter character;
    private int imgIndex;
    
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
