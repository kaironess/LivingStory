package sharedClasses;

import java.util.*;

public interface Requirement {
    boolean isFulfilled(List<?> vals);
}
