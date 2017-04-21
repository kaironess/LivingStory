package sharedClasses;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Scanner;

import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.embed.swing.JFXPanel;

public class Music implements Serializable {
    static final long serialVersionUID = 5L;
    
    private String name;
    private MediaPlayer player;
    private State curState;
    
    enum State {PLAYING, PAUSED, STOPPED};

    private String getMatPath(String addOn) {
        return "src\\testing\\TestingMaterials\\" + addOn;
    }
    
    public Music(String musicName, String fileName) {
        this.name = musicName;
        new JFXPanel(); // Needed to initialize stuff for MediaPlayer
        updateMusicPlayer(fileName);
    }
    
    public void updateMusicPlayer(String fileName) {
        String filePath = getMatPath(fileName);
        player = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
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
}
