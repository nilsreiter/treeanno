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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * Represents the syntactic tree.
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public class Tree {
    /**
     * The id of the root element.
     */
    private final String root;

    /**
     * A map of ids and tree elements.
     */
    private final Map<String, TreeElement> treeElements;

    /**
     * A list of terminal nodes.
     * 
     * 
     */

    private List<TreeTerminal> terminalNodes = null;

    /**
     * A list of ids that are used by terminal nodes.
     */
    private final List<String> terminalIds;

    /**
     * The root node of the tree.
     */
    private final TreeElement rootNode;

    /**
     * Creates a new Tree object. Takes a "graph" XML element as argument
     * 
     * @param node
     *            The XML element
     * @param sentence
     *            The sentence to which this tree object belongs
     */
    protected Tree(final Sentence sentence, final Element node) {
	root = node.attributeValue("root");
	treeElements = new HashMap<String, TreeElement>();
	terminalIds = new LinkedList<String>();

	List<?> nl = node.element("terminals").elements("t"); // new
	// Dom4jXPath("terminals/t").selectNodes(node);

	if (nl != null) {
	    for (int i = 0; i < nl.size(); i++) {
		Element term = (Element) nl.get(i);
		TreeTerminal terminal = new TreeTerminal(sentence, term);
		treeElements.put(terminal.getIdString(), terminal);
		terminalIds.add(terminal.getIdString());

	    }
	}

	nl = node.element("nonterminals").elements("nt"); // new
							  // Dom4jXPath("nonterminals/nt").selectNodes(node);
	if (nl != null) {
	    for (int i = 0; i < nl.size(); i++) {
		Element nterm = (Element) nl.get(i);
		TreeNonTerminal terminal = new TreeNonTerminal(sentence, nterm);
		treeElements.put(terminal.getIdString(), terminal);
	    }
	}

	rootNode = treeElements.get(root);

	rootNode.populate(this);

	rootNode.calculateRange(0);

	/*
	 * for (String terminalId : terminalIds) {
	 * sentence.addToken(treeElements.get(terminalId).getRange()); }
	 */
    }

    /**
     * Returns the surface string of the tree.
     * 
     * @return The surface string of the tree.
     */
    @Override
    public final String toString() {
	return rootNode.toString();
    };

    /**
     * This method returns the surface of the sentence. TODO: Needs another
     * name!
     * 
     * @return The surface of the string, without leading or trailing
     *         whitespaces
     */
    public String toString2() {
	return rootNode.toString2().trim();
    }

    /**
     * Returns an element of the tree by its id.
     * 
     * @param id
     *            The id of the element
     * @return the TreeElement
     */
    protected final TreeElement getTreeElement(final String id) {
	return treeElements.get(id);
    }

    /**
     * @return the treeElements
     */
    protected final Map<String, TreeElement> getTreeElements() {
	return treeElements;
    }

    /**
     * @return the terminalNodes
     */
    protected final List<TreeTerminal> getTerminalNodes() {
	if (terminalNodes == null) {
	    terminalNodes = new LinkedList<TreeTerminal>();
	    for (String terminalId : this.getTerminalIds()) {
		terminalNodes.add((TreeTerminal) this.treeElements
			.get(terminalId));
	    }
	}
	return terminalNodes;
    }

    /**
     * @return the terminalIds
     */
    protected final List<String> getTerminalIds() {
	return terminalIds;
    }

}
