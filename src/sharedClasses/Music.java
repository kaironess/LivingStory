package sharedClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.regex.Pattern;

import createdGameClasses.Save;
import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.embed.swing.JFXPanel;

public class Music implements Serializable {
    static final long serialVersionUID = 2L;
    
    private static int musicIndex = 0;
    private static String musicDir = System.getProperty("user.dir") + File.separator + 
     "GameMusic" + File.separator;
    
    private String name;
    private transient MediaPlayer player;
    private File mediaFile;
    private State curState;
    
    public enum State {PLAYING, PAUSED, STOPPED};

    private String getMatPath(String addOn) {
        return "src\\testing\\TestingMaterials\\" + addOn;
    }
    
    public Music(String musicName, String filePath) {
        this.name = musicName;
        new JFXPanel(); // Needed to initialize stuff for MediaPlayer
        setupMusicFiles(filePath);
        setupMediaPlayer();
    }
    
    private void setupMusicFiles(String filePath) {
        Path savePath = Paths.get(this.musicDir);
        
        // Make the save folder for music if it doesn't exist
        if (!Files.exists(savePath)) {
            boolean madeSaveFile = new File(savePath.toString()).mkdirs();
        }
            
        Path origFile = Paths.get(filePath);
        String[] fileDirs = filePath.split(Pattern.quote(System.getProperty("file.separator")));
        Path copyFile = Paths.get(savePath + File.separator + fileDirs[fileDirs.length - 1]);
        
        // Save the file
        try {
            Files.copy(origFile, copyFile, StandardCopyOption.COPY_ATTRIBUTES);
        }
        catch (FileAlreadyExistsException e) { 
            try {
                Files.delete(copyFile);
                Files.copy(origFile, copyFile, StandardCopyOption.COPY_ATTRIBUTES);
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        mediaFile = new File(copyFile.toUri());
    }
    
    public void setupMediaPlayer() {
        player = new MediaPlayer(new Media(mediaFile.toURI().toString()));
        loop();
        
        // Update curState depending on player activity
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                curState = State.STOPPED;
                System.out.println("Done Playing. Music State is now: " + curState.toString());
            }
        });
        
        // Update curState depending on player activity
        player.setOnRepeat(new Runnable() {
            @Override
            public void run() {
                curState = State.PLAYING;
                System.out.println("Playing. Music State is now: " + curState.toString());
            }
        });
        
        curState = State.STOPPED;
    }

    public void play() {
        player.play();
        curState = State.PLAYING;
    }
    
    public void pause() {
        player.pause();
        curState = State.PAUSED;
    }
    
    public void stop() {
        player.stop();
        curState = State.STOPPED;
    }
    
    public void setVol(double musicVol) {
        if (musicVol < 0)
            musicVol = 0;
        else if (musicVol > 1.0)
            musicVol = 1.0;
        
        player.setVolume(musicVol);
    }
    
    public void loop() {
        player.setCycleCount(MediaPlayer.INDEFINITE);
    }
    
    public void noLoop() {
        player.setCycleCount(1);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }
    
    public State getState() {
        return this.curState;
    }
    
    public void changeMediaFile(String filePath) {
        mediaFile = new File(filePath);
        setupMediaPlayer();
    }
    
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        setupMediaPlayer();
    }
}
