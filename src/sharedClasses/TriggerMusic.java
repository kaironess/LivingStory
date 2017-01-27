package sharedClasses;

public class TriggerMusic {
    private Music music;
    private boolean play;
    private boolean loop;
    
    public TriggerMusic(Music toTrigger) {
        this.music = toTrigger;
        this.play = false;
        this.loop = false;
    }
    
    public boolean getPlayStatus() {
        return play;
    }
    
    public boolean getLoopStatus() {
        return loop;
    }
    
    public void musicOn() {
        this.play = true;
    }
    
    public void musicOff() {
        this.play = false;
    }
    
    public void setLoop(boolean isLooped) {
        this.loop = isLooped;
    }
}
