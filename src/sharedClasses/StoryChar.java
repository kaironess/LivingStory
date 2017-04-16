package sharedClasses;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import javax.imageio.ImageIO;

public class StoryChar implements Serializable {
    static final long serialVersionUID = 11L;
    
    private transient List<Image> images;
    private String name;
    
    public StoryChar(Image img, String charName) {
        this.images = new LinkedList<Image>();
        if (img != null) {
            this.images.add(img);
        }
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
    
    public int getImgNum() {
        return this.images.size();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }
    
    // Overridden so that we can serialize BufferedImages
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(images.size());
        
        for (Image img : images) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)img, "jpg", buffer);
            
            out.writeInt(buffer.size());
            buffer.writeTo(out);
        }
    }
    
    // Overridden so that we can serialize BufferedImages
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        int imageCount = in.readInt();
        images = new ArrayList<Image>();
        
        for (int i = 0; i < imageCount; i++) {
            int size = in.readInt();
            
            byte[] buffer = new byte[size];
            in.readFully(buffer);
            
            images.add(ImageIO.read(new ByteArrayInputStream(buffer)));
        }
    }
}
