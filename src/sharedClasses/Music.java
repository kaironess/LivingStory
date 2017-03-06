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
    
    public void lowerVol() {
        double newVol = player.getVolume() - .5;
        if (newVol >= 0)
            player.setVolume(newVol);
    }
    
    public void raiseVol() {
        double newVol = player.getVolume() + .5;
        if (newVol <= 1)
            player.setVolume(newVol);
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
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Music music = new Music("poke3", "testMusic.mp3");
        //Music music = new Music("pokeWav", "testMusicWAV.wav");
        Music music = new Music("short", "shortClip.mp3");
        
        while(scanner.hasNext()) {
            String nextInput = scanner.nextLine();
            
            if (nextInput.equals("play")) {
                System.out.println("Now Playing...");
                music.play();
            }
            else if (nextInput.equals("pause")) {
                System.out.println("Pausing...");
                music.pause();
            }
            else if (nextInput.equals("stop")) {
                System.out.println("Stopping...");
                music.stop();
            }
            else if (nextInput.equals("loop")) {
                System.out.println("Enabling looping...");
                music.stop();
                music.loop();
                music.play();
            }
            else if (nextInput.equals("no loop")) {
                System.out.println("Disabling looping...");
                music.stop();
                music.noLoop();
                music.play();
            }
            else if (nextInput.equals("quiet")) {
                music.lowerVol();
                System.out.println("Lowering volume to: " + music.player.getVolume());
            }
            else if (nextInput.equals("loud")) {
                music.raiseVol();
                System.out.println("Raising volume to: " + music.player.getVolume());
            }
        }      
    }
}
