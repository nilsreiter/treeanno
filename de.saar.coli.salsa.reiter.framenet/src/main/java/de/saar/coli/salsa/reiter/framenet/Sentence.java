/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.saar.coli.salsa.reiter.framenet;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;

/**
 * This abstract class is the base class for any sentence reading class for a
 * specific corpus format. It basically provides the fields id and text,
 * representing a sentence identifier and the surface string of the sentence
 * itself.
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public abstract class Sentence implements IHasID, ISentence {
    /**
     * An identifier of the sentence.
     */
    private String id;

    /**
     * The surface string of the sentence.
     */
    private byte[] text;

    /**
     * A temporary list for caching the tokens.
     */
    private List<IToken> internalTokenList = null;

    /**
     * A constructor taking only the identifier. Optimally used in combination
     * with {@link Sentence#setText(String)}.
     * 
     * @param arg
     *            The identifier
     */
    public Sentence(final String arg) {
	this.id = arg;
	text = new byte[0];

	init();
    }

    /**
     * A constructor setting both the id and the text.
     * 
     * @param theId
     *            The identifier
     * @param theText
     *            The surface string of the sentence
     */
    public Sentence(final String theId, final String theText) {
	this.id = theId;

	this.text = theText.getBytes(Constants.characterSet);

	init();
    }

    /**
     * Initialises the object.
     */
    private void init() {
    }

    @Override
    public String getIdString() {
	return id;
    }

    /**
     * Sets the identifier.
     * 
     * @param theId
     *            the identifier
     */
    public void setId(final String theId) {
	this.id = theId;
    }

    @Override
    public String getText() {
	return new String(text, Constants.characterSet);
    }

    /**
     * Sets the surface string of the sentence.
     * 
     * @param theText
     *            the sentence
     */
    public void setText(final String theText) {
	this.text = theText.getBytes(Constants.characterSet);
	this.internalTokenList = null;
    }

    /**
     * Returns an unsorted collection of tokens in this sentence.
     * 
     * @return A collection of tokens
     * @deprecated
     */
    @Deprecated
    public Collection<IToken> getTokens() {
	return null;
	// TODO: Implement
    }

    /**
     * This method returns a list representation of the tokens in this sentence.
     * The tokens are sorted according to their position in the sentence.
     * 
     * @return A list, containing the tokens in the sentence.
     */
    public List<? extends IToken> getTokenList() {
	if (internalTokenList == null) {
	    internalTokenList = new LinkedList<IToken>();
	    Iterator<? extends IToken> tIter = this.getTokenIterator();
	    while (tIter.hasNext()) {
		internalTokenList.add(tIter.next());
	    }
	}
	return internalTokenList;

    }

    public List<IToken> getTokenList(final Range range) {
	List<IToken> ret = new LinkedList<IToken>();
	for (IToken token : getTokenList()) {
	    if (range.isSubRange(token.getCharacterRange())) {
		ret.add(token);
	    }
	}
	return ret;
    }

    @Override
    public abstract Iterator<? extends IToken> getTokenIterator();

    /**
     * Adds a token specified by the range.
     * 
     * @param range
     *            The range of the new token
     * @return the newly added token
     */
    protected abstract IToken addToken(CharacterRange range);

    /**
     * Returns a token containing a specific string. Returns null if the string
     * does not appear in the sentence. TODO: This is pretty broken, as a string
     * can appear multiple times in a sentence. This method does not work and
     * returns always <code>null</code>.
     * 
     * @param s
     *            The string
     * @return always null.
     * @deprecated
     */
    @Deprecated
    public IToken getTokenForString(final String s) {

	return null;
    }

    /**
     * Returns the surface string for the given range.
     * 
     * @param range
     *            The range we want to have a string for
     * @return A substring of the sentence
     */
    public String getSurface(final Range range) {
	return getText().substring(range.getElement1(), range.getElement2());
    }

    /**
     * Returns a range that contains the string s.
     * 
     * @param s
     *            The string we want to have the range for
     * @return A range object
     */
    public Range getRangeForString(final String s) {
	int begin = getText().indexOf(s);
	if (begin == -1) {
	    return null;
	}
	Range r = new Range(begin, begin + s.length());
	return r;
    }

    @Override
    public String toString() {
	return getText();
    }

}
