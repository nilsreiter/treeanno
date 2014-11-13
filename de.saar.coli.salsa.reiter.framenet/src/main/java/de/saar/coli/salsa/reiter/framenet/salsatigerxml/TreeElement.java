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
package de.saar.coli.salsa.reiter.framenet.salsatigerxml;

import java.util.SortedSet;

import de.saar.coli.salsa.reiter.framenet.AbstractToken;
import de.saar.coli.salsa.reiter.framenet.IHasID;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * An abstract class as base class for all classes building up the tree
 * (terminal and non-terminal graph nodes, so to speak).
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public abstract class TreeElement extends AbstractToken implements IHasID,
	Comparable<TreeElement> {

    byte[] surface = null;

    String id = null;

    Sentence sentence = null;

    CharacterRange range = null;
    /**
     * Range measured in tokens.
     */
    protected TokenRange tokenRange = null;

    /**
     * The default constructor.
     * 
     * @param sentenceArgument
     *            The sentence that this tree element belongs to
     */
    protected TreeElement(final Sentence sentenceArgument) {
	this.sentence = sentenceArgument;
    }

    @Override
    public String getIdString() {
	return id;
    }

    /**
     * Returns the surface string under this node, tokens sep. with a space
     * 
     * @return The tokenized surface string
     */
    @Override
    public abstract String toString();

    /**
     * Returns the surface string under this node, tokens unseparated.
     * 
     * @return The surface string without any tokenization
     */
    public abstract String toString2();

    /**
     * Returns true, if the tree node is a terminal node. False otherwise
     * 
     * @return true or false
     */
    public abstract boolean isTerminal();

    /**
     * Retrieves the TreeElement objects by their id and stores them internally
     * for each object.
     * 
     * @param tree
     *            The tree
     */
    protected abstract Range populate(Tree tree);

    /**
     * Returns the length in characters of this subtree.
     * 
     * @return The length of this subtree
     */
    protected abstract int length();

    protected abstract int calculateRange(int start);

    @Override
    public CharacterRange getCharacterRange() {
	return range;
    }

    /**
     * Returns the range of tokens this element spans. Terminal nodes span only
     * one token and have the same token as begin and end of the range.
     * 
     * @return A pair of first and last token under this node
     */
    @Override
    public TokenRange getTokenRange() {
	return tokenRange;
    };

    @Override
    public Sentence getSentence() {
	return sentence;
    }

    /**
     * Compares this TreeElement to another TreeElement in order to sort them in
     * order of their ranges.
     * 
     * @return -1, 0, 1
     */
    @Override
    public int compareTo(final TreeElement other) {
	return getCharacterRange().compareTo(other.getCharacterRange());
    }

    public abstract SortedSet<TreeTerminal> getTerminalNodes();

    protected abstract boolean hasSpaceBefore();

    protected abstract boolean hasSpaceAfter();
}
