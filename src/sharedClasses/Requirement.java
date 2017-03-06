package sharedClasses;

import java.io.Serializable;
import java.util.*;

public interface Requirement extends Serializable {
    static final long serialVersionUID = 7L;
    
    boolean isFulfilled(List<?> vals);
}
