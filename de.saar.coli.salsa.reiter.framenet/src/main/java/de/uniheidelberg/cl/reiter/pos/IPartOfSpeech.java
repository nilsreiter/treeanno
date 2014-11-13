package de.uniheidelberg.cl.reiter.pos;

/**
 * This interface defines methods for all inventories of POS-tags.
 * 
 * @author reiter
 * 
 */
public interface IPartOfSpeech {

    /**
     * Returns the word class of a part of speech, i.e., a very broad inventory
     * of POS-tags.
     * 
     * @return the word class.
     */
    WordClass getWordClass();

    /**
     * Mapping to FrameNet pos-tags.
     * 
     * @return a FrameNet-style tag
     */
    FN asFN();

    /**
     * Returns a very short representation of the pos tag.
     * 
     * @return a short string
     */
    String toShortString();

    boolean hasSpaceBefore();

    boolean hasSpaceAfter();
}
