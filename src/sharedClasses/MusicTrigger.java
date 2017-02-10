package sharedClasses;

import sharedClasses.Music;

public class MusicTrigger {
    private Music music;
    private Trigger trigger;
    private boolean loop;
    
    enum Trigger {PLAY, PAUSE, STOP}
    
    public MusicTrigger(Music toTrigger, Trigger trigger) {
        this.music = toTrigger;
        this.trigger = trigger;
        this.loop = false;
    }
    
    public Trigger getPlayStatus() {
        return trigger;
    }
    
    public boolean getLoopStatus() {
        return loop;
    }
    
    public void trigger() {
        if (loop)
            music.loop();
        else
            music.noLoop();
        
        if (trigger == Trigger.PLAY)
            music.play();
        else if (trigger == Trigger.PAUSE)
            music.pause();
        else
            music.stop();
    }
    
    public void setAction(Trigger trigger) {
        this.trigger = trigger;
    }
    
    public void setLoop(boolean isLooped) {
        this.loop = isLooped;
    }
}
