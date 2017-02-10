package creationToolClasses;

import sharedClasses.*;

public class MusicManager {
    private static WIP wip = WIP.getWIP();
    
    public static void createMusic(String musicName, String statName) {
        Music newMusic = new Music(musicName, statName);
        wip.musics.add(newMusic);
    }
    
    public static void deleteMusic(String name) {
        wip.musics.remove(wip.getMusicByName(name));
    }
    
    public static void renameMusic(String curName, String newName) {
        wip.getMusicByName(curName).setName(newName);
    }
    
    public static void changeFile(String musicName, String fileName) {
        wip.getMusicByName(musicName).updateMusicPlayer(fileName);
    }

}
