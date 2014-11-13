package de.saar.coli.salsa.reiter.framenet;

import java.util.Collection;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * Interface for token-based annotation. Internally, a token is represented with
 * a range and a sentence. The (character) range is begin and end of the token
 * in the sentence, such that {@link java.lang.String#substring(int, int)}
 * returns the correct surface.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public interface IToken {

    /**
     * Returns the range of the token.
     * 
     * @return The range
     */
    CharacterRange getCharacterRange();

    TokenRange getTokenRange();

    /**
     * Returns the surface of the token.
     * 
     * @return A string
     */
    @Override
    String toString();

    /**
     * 
     * @return the part of speech of this token
     */
    IPartOfSpeech getPartOfSpeech();

    /**
     * Gives a reference to the sentence in which this token appears.
     * 
     * @return A sentence
     */
    ISentence getSentence();

    /**
     * Sets a property of the token.
     * 
     * @param key
     *            The key
     * @param value
     *            The value
     */
    void setProperty(String key, String value);

    /**
     * Retrieves the property with the given key.
     * 
     * @param key
     *            The key
     * @return The value of the property.
     */
    String getProperty(String key);

    /**
     * Returns a collection of all keys defined in this token.
     * 
     * @return A collection of strings
     */
    Collection<String> getPropertyKeys();

    /**
     * 
     * @return
     */
    Collection<RealizedFrame> getEvokedFrames();

    /**
     * 
     * @param rf
     */
    void addEvokedFrame(RealizedFrame rf);
}
