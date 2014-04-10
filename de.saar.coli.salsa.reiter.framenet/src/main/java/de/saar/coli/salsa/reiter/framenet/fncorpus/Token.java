package de.saar.coli.salsa.reiter.framenet.fncorpus;

import de.saar.coli.salsa.reiter.framenet.AbstractToken;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * This class represents tokens in the original FrameNet corpus.
 * 
 * @author reiter
 * @since 0.4
 * 
 */
public class Token extends AbstractToken {

    /**
     * The character range of this token, such that
     * {@link String#substring(int, int)} returns the surface of the token.
     */
    CharacterRange range;

    /**
     * The sentence containing this token.
     */
    Sentence sentence;

    /**
     * 
     * @param sentence
     *            The sentence containing this token
     * @param range
     *            The character range of this token, such that
     *            {@link String#substring(int, int)} returns the surface of the
     *            token
     */
    protected Token(final Sentence sentence, final CharacterRange range) {
	this.sentence = sentence;
	this.range = range;
    }

    @Override
    public CharacterRange getCharacterRange() {
	return range;
    }

    @Override
    public String toString() {
	return sentence.getText().substring(range.getElement1(),
		range.getElement2());
    }

    @Override
    public Sentence getSentence() {
	return sentence;
    }

    @Override
    public TokenRange getTokenRange() {
	int index = this.getSentence().getTokenList().indexOf(this);
	// TODO Auto-generated method stub
	if (index < 0) {
	    return new TokenRange(0, 0);
	}
	return new TokenRange(index, index);
    }

}
