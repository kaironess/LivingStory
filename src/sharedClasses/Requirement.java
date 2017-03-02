package sharedClasses;

import java.io.Serializable;
import java.util.*;

public interface Requirement extends Serializable {
    boolean isFulfilled(List<?> vals);
}
