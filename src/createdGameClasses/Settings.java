package createdGameClasses;

import java.io.Serializable;

import sharedClasses.Font;

public class Settings implements Serializable {
    public int musicVol, sfxVol;
    public boolean isWindowed;
    public Font curFont;

    public Settings() {
        isWindowed = false;
        sfxVol = musicVol = 100;
        //TODO
        // add a default curFont
    }
}
