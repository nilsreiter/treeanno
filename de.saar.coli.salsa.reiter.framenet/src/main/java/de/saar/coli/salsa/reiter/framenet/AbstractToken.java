package de.saar.coli.salsa.reiter.framenet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * Makes a basic implementation of the IToken interface. Different corpus
 * readers extend it.
 * 
 * @author Nils Reiter
 * @since 0.4
 */
public abstract class AbstractToken implements IToken {

    /**
     * Stores properties of the token.
     */
    Properties data;

    /**
     * 
     */
    IPartOfSpeech partOfSpeech;

    /**
     * A list of frames, evoked by this token.
     */
    List<RealizedFrame> evokedFrames = new LinkedList<RealizedFrame>();

    /**
     * Constructor.
     */
    public AbstractToken() {
	data = new Properties();
    }

    @Override
    public abstract CharacterRange getCharacterRange();

    @Override
    public abstract TokenRange getTokenRange();

    @Override
    public abstract ISentence getSentence();

    @Override
    public void setProperty(final String key, final String value) {
	if (key.equalsIgnoreCase("POS")) {
	    throw new UnsupportedOperationException();
	}
	data.setProperty(key, value);
    }

    @Override
    public String getProperty(final String key) {
	if (key.equalsIgnoreCase("POS")) {
	    throw new UnsupportedOperationException();
	}
	return data.getProperty(key);
    }

    @Override
    public Collection<String> getPropertyKeys() {
	return data.stringPropertyNames();
    }

    @Override
    public Collection<RealizedFrame> getEvokedFrames() {
	return this.evokedFrames;
    }

    @Override
    public void addEvokedFrame(final RealizedFrame rf) {
	this.evokedFrames.add(rf);
    };

    /**
     * @return the partOfSpeech
     */
    @Override
    public IPartOfSpeech getPartOfSpeech() {
	return partOfSpeech;
    }

    /**
     * @param partOfSpeech
     *            the partOfSpeech to set
     */

    public void setPartOfSpeech(final IPartOfSpeech partOfSpeech) {
	this.partOfSpeech = partOfSpeech;
    }

}
