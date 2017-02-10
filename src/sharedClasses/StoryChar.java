package sharedClasses;

import java.awt.Image;
import java.util.*;

public class StoryChar {
    private List<Image> images;
    private String name;
    
    public StoryChar(Image img, String charName) {
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
    
    public void setName(String newName) {
        this.name = newName;
    }
}
