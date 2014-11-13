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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dom4j.Element;

import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * Represents non terminal nodes in the tree.
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public class TreeNonTerminal extends TreeElement {
    /**
     * A list of ids of the daughters if this element.
     */
    private List<String> daughterIds;
    /**
     * A set of daughters of this element.
     */
    private SortedSet<TreeElement> daughters;

    /**
     * Creates a new non-terminal node by providing the XML element.
     * 
     * @param sentence
     *            The sentence
     * @param element
     *            The XML element
     */
    protected TreeNonTerminal(final Sentence sentence, final Element element) {
	super(sentence);
	id = element.attributeValue("id");
	daughterIds = new LinkedList<String>();
	daughters = new TreeSet<TreeElement>(new Comparator<TreeElement>() {
	    @Override
	    public int compare(final TreeElement t1, final TreeElement t2) {
		return t1.getTokenRange().getElement1()
			.compareTo(t2.getTokenRange().getElement2());
	    };
	});
	List<?> edges = element.elements("edge");
	if (edges != null) {
	    for (int i = 0; i < edges.size(); i++) {
		Element edge = (Element) edges.get(i);
		daughterIds.add(edge.attributeValue("idref"));
	    }
	}
    }

    @Override
    public String toString() {
	StringBuffer buf = new StringBuffer();
	for (TreeElement te : daughters) {
	    buf.append(te.toString());
	    buf.append(" ");
	}
	return buf.toString().trim();
    }

    @Override
    public String toString2() {
	StringBuffer buf = new StringBuffer();
	TreeElement last = null;
	Iterator<TreeElement> elementIterator = this.daughters.iterator();
	while (elementIterator.hasNext()) {
	    TreeElement te = elementIterator.next();
	    if (last != null && last.hasSpaceAfter() && te.hasSpaceBefore()) {
		buf.append(' ');
		buf.append(te.toString2());
	    } else {
		buf.append(te.toString2());
	    }
	    last = te;
	}
	return buf.toString().trim();
    }

    @Override
    protected Range populate(final Tree tree) {
	int b = Integer.MAX_VALUE, e = 0;
	for (String id : daughterIds) {
	    Range r = tree.getTreeElement(id).populate(tree);
	    if (r.getElement1() < b) {
		b = r.getElement1();
	    }
	    if (r.getElement2() > e) {
		e = r.getElement2();
	    }
	    daughters.add(tree.getTreeElement(id));
	}
	tokenRange = new TokenRange(b, e);
	return tokenRange;
    }

    @Override
    public boolean isTerminal() {
	return false;
    }

    @Override
    protected int length() {
	int n = 0;

	TreeElement last = null;
	Iterator<TreeElement> elementIterator = this.daughters.iterator();
	while (elementIterator.hasNext()) {
	    TreeElement te = elementIterator.next();
	    if (last != null && last.hasSpaceAfter() && te.hasSpaceBefore()) {
		n += te.length() + 1;
	    } else {
		n += te.length();
	    }
	    last = te;
	}
	return n;
    }

    @Override
    public TokenRange getTokenRange() {
	return tokenRange;
    }

    @Override
    protected int calculateRange(final int start) {
	int position = start;

	TreeElement last = null;
	Iterator<TreeElement> elementIterator = this.daughters.iterator();
	while (elementIterator.hasNext()) {
	    TreeElement te = elementIterator.next();
	    if (last != null && last.hasSpaceAfter() && te.hasSpaceBefore()) {
		position = te.calculateRange(position + 1);
	    } else {
		position = te.calculateRange(position);
	    }
	    last = te;
	}
	range = new CharacterRange(start, position);
	return position;
    }

    @Override
    public SortedSet<TreeTerminal> getTerminalNodes() {
	SortedSet<TreeTerminal> ret =
		new TreeSet<TreeTerminal>(new Comparator<TreeTerminal>() {

		    @Override
		    public int compare(final TreeTerminal arg0,
			    final TreeTerminal arg1) {
			return arg0.getTokenRange().compareTo(
				arg1.getTokenRange());
		    }

		});
	for (TreeElement node : this.daughters) {
	    ret.addAll(node.getTerminalNodes());
	}
	return ret;
    }

    @Override
    protected boolean hasSpaceBefore() {
	return this.daughters.first().hasSpaceBefore();
    }

    @Override
    protected boolean hasSpaceAfter() {
	return this.daughters.last().hasSpaceAfter();
    }
}
