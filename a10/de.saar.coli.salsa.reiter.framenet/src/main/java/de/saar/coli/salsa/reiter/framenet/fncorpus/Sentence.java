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
package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;
import de.uniheidelberg.cl.reiter.util.SortedMapIterator;

/**
 * This class represents a single sentence in the FrameNet corpus. Multiple
 * annotation sets belong to one sentence.
 * 
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public abstract class Sentence extends
	de.saar.coli.salsa.reiter.framenet.Sentence {
    /**
     * The FN corpus.
     */
    protected CorpusReader corpusReader = null;

    /**
     * A list of annotation sets for this sentence.
     */
    protected final List<AnnotationSet> annotationSets =
	    new LinkedList<AnnotationSet>();

    /**
     * A list of tokens in this sentence.
     */
    protected SortedMap<Range, IToken> tokenList;

    /**
     * 
     * @param id
     *            A string identifier for this sentence
     * @param text
     *            The surface text of this sentence
     */
    protected Sentence(final String id, final String text) {
	super(id, text);
	tokenList = new TreeMap<Range, IToken>();
    }

    @Override
    public List<RealizedFrame> getRealizedFrames() {
	// TODO: Improve by caching
	List<RealizedFrame> ret = new LinkedList<RealizedFrame>();
	for (AnnotationSet aset : annotationSets) {
	    if (aset.getRealizedFrame() != null) {
		ret.add(aset.getRealizedFrame());
	    }
	}
	return ret;
    }

    /**
     * Returns a reference to the FrameNet corpus.
     * 
     * @return the FrameNet corpus
     */
    public CorpusReader getCorpus() {
	return corpusReader;
    }

    @Override
    public Token addToken(final CharacterRange range) {
	Token token = new Token(this, range);
	tokenList.put(range, token);
	return token;
    }

    /**
     * @return the annotationSets
     */
    public List<AnnotationSet> getAnnotationSets() {
	return annotationSets;
    }

    /**
     * @param e
     *            the annotation set to add
     * @return true
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean addAnnotationSet(final AnnotationSet e) {
	return annotationSets.add(e);
    }

    /**
     * @param index
     *            The index of the set
     * @return returns the annotation set with the given index
     * @see java.util.List#get(int)
     */
    public AnnotationSet getAnnotationSet(final int index) {
	return annotationSets.get(index);
    }

    /**
     * @return the number of annotation sets in this sentence
     * @see java.util.List#size()
     */
    public int annotationSets() {
	return annotationSets.size();
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

    public Collection<IToken> getTokens(final CharacterRange range) {
	Set<IToken> tokens = new HashSet<IToken>();
	for (Range existingRange : tokenList.keySet()) {
	    if (range.isSubRange(existingRange)) {
		tokens.add(tokenList.get(existingRange));
	    }
	}

	if (tokens.isEmpty()) {
	    tokens.add(addToken(range));
	}

	return tokens;
    }

    /**
     * Returns an iterator over the tokens in this sentence. *
     * 
     * @return The iterator
     * 
     * @since 0.4.3
     */
    @Override
    public Iterator<IToken> getTokenIterator() {
	return new SortedMapIterator<IToken>(tokenList);
    }
}
