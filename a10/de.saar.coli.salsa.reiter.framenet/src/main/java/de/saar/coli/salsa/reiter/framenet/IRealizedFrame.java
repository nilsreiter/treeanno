package de.saar.coli.salsa.reiter.framenet;

import java.util.Map;
import java.util.SortedSet;

/**
 * General interface for realized frames
 * 
 * @author reiter
 * 
 */
public interface IRealizedFrame {
    SortedSet<? extends IToken> getTargetList();

    boolean isNullInstantiated();

    ISentence getSentence();

    IToken getTarget();

    Map<String, ? extends IRealizedFrameElement> getOvertFrameElements();

    Map<String, ? extends IRealizedFrameElement> getFrameElements();

    Frame getFrame();

    public abstract SortedSet<? extends IRealizedFrameElement> overtFrameElements();

    public abstract SortedSet<? extends IRealizedFrameElement> frameElements();
}
