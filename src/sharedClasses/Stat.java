package sharedClasses;

public class Stat {
    private String name;
    private int count;
    
    public Stat(String statName) {
        this.name = statName;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
