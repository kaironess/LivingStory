package sharedClasses;

import java.awt.Image;
import java.util.*;

public class StoryCharacter {
    private List<Image> images;
    private String name;
    
    public StoryCharacter(Image img, String charName) {
        this.images = new LinkedList<Image>();
        this.images.add(img);
        this.name = charName;
    }
    
    public void addImg(Image img) {
        this.images.add(img);
    }
    
    public void deleteImg(Image img) {
        this.images.remove(img);
    }
    
    public Image getImage(int index) {
        return this.images.get(index);
    }
    
    public String getName() {
        return name;
    }
}
