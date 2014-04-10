package de.saar.coli.salsa.reiter.framenet;

import java.util.Iterator;
import java.util.List;

/**
 * General interface for sentences
 * 
 * @author reiter
 * 
 */
public interface ISentence {
    /**
     * Returns an iterator over the tokens in this sentence.
     * 
     * Experimental!
     * 
     * @return The iterator
     * 
     */
    Iterator<? extends IToken> getTokenIterator();

    /**
     * Returns the complete text of the sentence.
     * 
     * @return the text of the sentence
     */
    String getText();

    /**
     * This abstract method returns a (ordered) list of realized frames which
     * occur in this sentence. Has to be implemented in inheriting classes,
     * since it heavily depends on the actual representation of the frames.
     * 
     * @return A list of RealizedFrame objects
     */
    List<? extends IRealizedFrame> getRealizedFrames();
}
