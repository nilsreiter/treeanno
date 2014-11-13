package de.saar.coli.salsa.reiter.framenet.flatformat;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;
import de.uniheidelberg.cl.reiter.util.SortedMapIterator;

/**
 * This class represents a sentence in the flat, plain-text based format.
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public class Sentence extends de.saar.coli.salsa.reiter.framenet.Sentence {

	/**
	 * A list of realized frames found in this sentence
	 */
	List<RealizedFrame> realizedFrames = new LinkedList<RealizedFrame>();
	/**
	 * A list of tokens in this sentence.
	 */
	protected SortedMap<Range, IToken> tokenList;

	/**
	 * A constructor taking a sentence id and the text of the sentence
	 * 
	 * @param id
	 *            The id
	 * @param text
	 *            The surface string of the sentence
	 */
	protected Sentence(final String id, final String text) {
		super(id, text);
	}

	/**
	 * A constructor that takes an integer as identifier as well as a string as
	 * surface form of the sentence. The identifier is transformed into a string
	 * by the constructor.
	 * 
	 * @param id
	 *            The identifier
	 * @param text
	 *            The surface form of the sentence
	 */
	protected Sentence(final int id, final String text) {
		super(String.valueOf(id), text);
	}

	/**
	 * This method adds a RealizedFrame object to the sentence
	 * 
	 * @param realizedFrame
	 *            The realized frame
	 */
	protected void addRealizedFrame(final RealizedFrame realizedFrame) {
		realizedFrames.add(realizedFrame);
	}

	/**
	 * This method returns the list of realized frames in the sentence.
	 * 
	 * @return An ordered list of RealizedFrame objects
	 */
	@Override
	public List<RealizedFrame> getRealizedFrames() {
		return realizedFrames;
	}

	@Override
	protected IToken addToken(final CharacterRange range) {
		Token token = new Token(this, range);
		tokenList.put(range, token);
		return token;

	}

	/**
	 * Returns a token object for the given range. Creates the object if non
	 * existing.
	 * 
	 * @param range
	 *            The range
	 * @return The token
	 */
	public IToken getToken(final CharacterRange range) {
		if (!tokenList.containsKey(range)) {
			addToken(range);
		}
		return tokenList.get(range);
	}

	@Override
	public Iterator<IToken> getTokenIterator() {
		return new SortedMapIterator<IToken>(tokenList);
	}
}
