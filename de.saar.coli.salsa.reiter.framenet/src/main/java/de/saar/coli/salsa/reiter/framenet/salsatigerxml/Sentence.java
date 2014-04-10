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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.AHasTarget;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.uniheidelberg.cl.reiter.util.CharacterRange;

/**
 * This class represents a sentence in the SalsaTigerXML package.
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public class Sentence extends de.saar.coli.salsa.reiter.framenet.Sentence {

    /**
     * The syntactic tree of this sentence.
     */
    private Tree tree = null;

    /**
     * The semantics of this sentence.
     */
    private Semantics semantics = null;

    /**
     * The SalsaTigerXML object this sentence belongs to.
     */
    private SalsaTigerXML salsaTigerXML = null;

    /**
     * Creates a new Sentence object, based on the FrameNet object and an XML
     * element representing the sentence. In order to process cross-sentence
     * annotations, we also need the SalsaTigerXML object.
     * 
     * @param frameNet
     *            The FrameNet object
     * @param element
     *            The XML element
     * @param stx
     *            The SalsaTigerXML object
     * @since 0.4.1
     */
    public Sentence(final SalsaTigerXML stx, final FrameNet frameNet,
	    final Element element) {
	super(element.attributeValue("id"));
	salsaTigerXML = stx;
	tree = new Tree(this, element.element("graph"));

	semantics =
		new Semantics(salsaTigerXML, this, element.element("sem"),
			tree, frameNet);
	// this.linkOpenTargets();
	setText(tree.toString2());

    }

    /**
     * This method tries to link the open targets in this corpus to tree
     * elements in this sentence. The returned number represents the number of
     * linked targets.
     * 
     * @return The number of linked targets
     */
    protected int linkOpenTargets() {
	Set<String> toRemove = new HashSet<String>();

	for (RealizedFrame rf : this.getRealizedFrames()) {
	    String id = rf.getIdString();
	    if (salsaTigerXML.getOpenTargets().containsKey(id)) {
		for (AHasTarget rfe : salsaTigerXML.getOpenTargets()
			.get(id)) {
		    rfe.setTarget(rf.getTarget());
		    toRemove.add(id);
		}
	    }
	}

	for (String id : tree.getTreeElements().keySet()) {
	    if (salsaTigerXML.getOpenTargets().containsKey(id)) {
		for (RealizedFrameElement rfe : salsaTigerXML.getOpenTargets()
			.get(id)) {
		    rfe.setTarget(tree.getTreeElements().get(id));
		    rfe.getRealizedFrame().addRealizedFrameElement(rfe);
		}
		toRemove.add(id);
	    }
	}
	int n = 0;
	for (String id : toRemove) {
	    salsaTigerXML.getOpenTargets().remove(id);
	    n++;
	}
	return n;

    }

    @Override
    public final List<RealizedFrame> getRealizedFrames() {
	return getSemantics().getRealizedFrames();
    }

    /**
     * This method returns the Semantics object for this sentence.
     * 
     * @return The Semantics object
     */
    public final Semantics getSemantics() {
	return semantics;
    }

    /**
     * 
     */
    @Override
    protected final IToken addToken(final CharacterRange range)
	    throws UnsupportedOperationException {
	return null;
    };

    /**
     * Returns the syntactic tree of this sentence.
     * 
     * @return The tree.
     */
    protected final Tree getTree() {
	return tree;
    }

    @Override
    public final List<IToken> getTokens() {
	SortedSet<IToken> tokenList = new TreeSet<IToken>();
	tokenList.addAll(tree.getTerminalNodes());
	List<IToken> ret = new LinkedList<IToken>(tokenList);
	return ret;
    }

    @Override
    public Iterator<? extends IToken> getTokenIterator() {
	return this.tree.getTerminalNodes().iterator();
    }

}
