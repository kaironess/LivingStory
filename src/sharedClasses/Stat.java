package sharedClasses;

public class Stat {
    private String name;
    private int count;
    
    public Stat(String statName) {
        this(statName, 0);
    }
    
    public Stat(String statName, int initialVal) {
        this.name = statName;
        this.count = initialVal;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
