package creationToolClasses;

import sharedClasses.*;
import java.awt.Image;

public class CharManager {
    private CharManager() {}
    private static WIP wip = WIP.getWIP();
    
    public static void createChar(String charName, Image img) {
        StoryChar newChar = new StoryChar(img, charName);
        wip.chars.add(newChar);
    }
    
    public static void deleteChar(String charName) {
        wip.chars.remove(wip.getCharByName(charName));
    }
    
    public static void renameChar(String curName, String newName) {
        wip.getCharByName(curName).setName(newName);
    }
    
    public static void addImgToChar(String charName, Image img) {
        wip.getCharByName(charName).addImg(img);
    }
    
    public static void deleteCharImg(String charName, Image img) {
        wip.getCharByName(charName).deleteImg(img);
    }
    
    public static void deleteCharImg(String charName, int index) {
        wip.getCharByName(charName).deleteImg(index);
    }
}
