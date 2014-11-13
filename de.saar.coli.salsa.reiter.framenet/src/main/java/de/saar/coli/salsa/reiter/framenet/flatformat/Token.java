package de.saar.coli.salsa.reiter.framenet.flatformat;

import de.saar.coli.salsa.reiter.framenet.AbstractToken;
import de.saar.coli.salsa.reiter.framenet.ISentence;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * This class represents tokens in the flat format.
 * 
 * @author reiter
 * 
 */
public class Token extends AbstractToken {
    Sentence sentence = null;
    CharacterRange range = null;

    protected Token(final Sentence sentence, final CharacterRange range) {
	this.sentence = sentence;
	this.range = range;
    }

    @Override
    public CharacterRange getCharacterRange() {
	return range;
    }

    @Override
    public ISentence getSentence() {
	return sentence;
    }

    @Override
    public String toString() {
	// return this.range.toString();
	return sentence.getSurface(range);
    }

    @Override
    public TokenRange getTokenRange() {
	// TODO Auto-generated method stub
	return null;
    }
}
