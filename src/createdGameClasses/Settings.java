package createdGameClasses;

import java.io.Serializable;

import sharedClasses.Font;

public class Settings implements Serializable {
    public double musicVol; //Between 0 to 1.0

    public Settings() {
        musicVol = 1.0;
    }
}
