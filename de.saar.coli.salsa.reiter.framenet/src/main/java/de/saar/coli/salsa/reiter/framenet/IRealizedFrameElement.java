package de.saar.coli.salsa.reiter.framenet;

import java.util.SortedSet;

public interface IRealizedFrameElement {

    public abstract SortedSet<? extends IToken> getTargetList();

    public abstract boolean isCrossingSentenceBoundaries();

    public abstract String getIType();

    public abstract boolean isNullInstantiated();

    public abstract IRealizedFrame getRealizedFrame();

    public abstract FrameElement getFrameElement();

}
