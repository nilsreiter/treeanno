package de.saar.coli.salsa.reiter.framenet;

import java.io.Serializable;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

/**
 * This class represents a lexeme in FrameNet.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public class Lexeme implements Serializable, IHasID {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The XML id of the lexeme.
     */
    String id = null;

    /**
     * The part of speech of this lexeme.
     */
    IPartOfSpeech partOfSpeech = null;

    boolean breakBefore;

    boolean headword;

    String value;

    /**
     * Returns the part of speech tag of the lexeme.
     * 
     * @deprecated
     * @return A string: N, V or A
     */
    @Deprecated
    public String getPos() {
	return partOfSpeech.toString();
    }

    /**
     * Returns the part of speech tag of this lexeme.
     * 
     * @return The part of speech tag
     */
    public IPartOfSpeech getPartOfSpeech() {
	return partOfSpeech;
    }

    /**
     * Returns true, if the lexeme is marked with breakBefore.
     * 
     * @return true or false
     */
    public boolean isBreakBefore() {
	return breakBefore;
    }

    /**
     * Returns true, if the lexeme is marked as a headword.
     * 
     * @return true or false
     */
    public boolean isHeadword() {
	return headword;
    }

    /**
     * Returns the string value of the lexeme.
     * 
     * @return A string
     */
    public String getValue() {
	return value;
    }

    @Override
    public String getIdString() {
	return id;
    }

    @Override
    public String toString() {
	return getValue();
    }

    /**
     * Returns true if this lexeme is a multi word expression.
     * 
     * @return true or false
     */
    public boolean isMultiWord() {
	return this.getValue().contains(" ");
    }

}
