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
import java.util.TreeSet;

import org.dom4j.Attribute;
import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.Constants;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.uniheidelberg.cl.reiter.pos.PTB;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * Represents a terminal node in the syntactic tree.
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public class TreeTerminal extends TreeElement implements IToken {

    /**
     * Creates the terminal node and stores its attributes.
     * 
     * @param sentence
     *            The Sentence, in which this tree occurres
     * @param element
     *            The XML element
     */
    protected TreeTerminal(final Sentence sentence, final Element element) {
	super(sentence);
	id = element.attributeValue("id");
	for (Object obj : element.attributes()) {
	    Attribute attr = (Attribute) obj;
	    if (attr.getName().equalsIgnoreCase("POS")) {
		this.setPartOfSpeech(PTB.fromString(attr.getValue()));
	    } else {
		this.setProperty(attr.getName().toUpperCase(), attr.getValue());
	    }
	}

	surface =
		element.attributeValue("word").getBytes(Constants.characterSet);

    }

    @Override
    public String toString2() {

	return new String(surface, Constants.characterSet);
    }

    @Override
    public String toString() {
	return new String(surface, Constants.characterSet);
    }

    @Override
    public TokenRange getTokenRange() {
	return tokenRange;
    }

    @Override
    public boolean isTerminal() {
	return true;
    }

    @Override
    protected TokenRange populate(final Tree tree) {
	tokenRange =
		new TokenRange(tree.getTerminalIds().indexOf(id), tree
			.getTerminalIds().indexOf(id));
	return tokenRange;
    };

    @Override
    protected int length() {
	return new String(surface, Constants.characterSet).length();
    }

    @Override
    protected int calculateRange(final int start) {
	range = new CharacterRange(start, start + length());
	return range.getElement2();
    }

    @Override
    public SortedSet<TreeTerminal> getTerminalNodes() {
	SortedSet<TreeTerminal> ret = new TreeSet<TreeTerminal>();
	ret.add(this);
	return ret;
    }

    @Override
    protected boolean hasSpaceBefore() {
	if (this.getPartOfSpeech() != null) {
	    return this.getPartOfSpeech().hasSpaceBefore();
	}
	return true;
    }

    @Override
    protected boolean hasSpaceAfter() {
	if (this.getPartOfSpeech() != null) {
	    return this.getPartOfSpeech().hasSpaceAfter();
	}
	return true;
    }
}
