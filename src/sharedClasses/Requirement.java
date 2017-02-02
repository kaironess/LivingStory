package sharedClasses;

import java.util.*;

public interface Requirement {
    boolean isFulfilled(List<Stat> stats);
}
