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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.FrameElement;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.InteractiveFrameElement;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;

/**
 * This class is used to analyze the "sem" part of the XML tree.
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public class Semantics {
    /**
     * The tree of syntactic annotation.
     */
    private final Tree tree;

    /**
     * The list of realized frames in the semantic part.
     */
    private List<RealizedFrame> realizedFrames = null;

    /**
     * The FrameNet object.
     */
    private FrameNet frameNet = null;

    /**
     * A link to the SalsaTigerXML object.
     */
    private SalsaTigerXML salsaTigerXML = null;

    /**
     * Creates a new Semantics object, based on a XML sem element, an already
     * parsed syntactic tree and a FrameNet object.
     * 
     * @param element
     *            The XML element
     * @param syntacticTree
     *            The syntactic tree
     * @param frameNetObject
     *            The FrameNet object
     * @param sentence
     *            The sentence to which this semantics object belongs
     * @param stx
     *            As of 0.4.1, we need the SalsaTigerXML object for processing
     *            cross-sentence-links.
     * 
     * @since 0.4.1
     */
    public Semantics(final SalsaTigerXML stx, final Sentence sentence,
	    final Element element, final Tree syntacticTree,
	    final FrameNet frameNetObject) {
	this.tree = syntacticTree;
	salsaTigerXML = stx;
	realizedFrames = new LinkedList<RealizedFrame>();
	this.frameNet = frameNetObject;

	if (element.element("frames") != null) {
	    List<?> frames = element.element("frames").elements("frame");
	    for (int i = 0; i < frames.size(); i++) {
		RealizedFrame realizedFrame =
			getRealizedFrame((Element) frames.get(i));
		if (realizedFrame != null) {
		    realizedFrames.add(realizedFrame);
		    realizedFrame.setSentence(sentence);
		}
	    }
	}
    }

    /**
     * Used to access the list of realized frames in this semantic layer.
     * 
     * @return The list of realized frames in this semantic layer
     */
    public final List<RealizedFrame> getRealizedFrames() {
	return realizedFrames;
    }

    private void addNIRealizedFrameElement(final Element fenode,
	    final RealizedFrame realizedFrame, final FrameElement fe) {

	RealizedFrameElement rfe = new RealizedFrameElement(realizedFrame, fe);
	rfe.setNullInstantiated(true);

	// Detect the type of null instantiation, iterating over
	// the flags (this is a preliminary handling of flags,
	// at some point, we want more general flag-handling)
	// Code by Caroline Sporleder
	List<?> flags = fenode.elements("flag");
	for (int l = 0; l < flags.size(); l++) {
	    Element flag = (Element) flags.get(l);
	    String flagValue = flag.attributeValue("name");
	    if (flagValue.equals("Indefinite_Interpretation")
		    || flagValue.equals("Definite_Interpretation")) {
		rfe.setIType(flagValue);
		break;
	    }
	}
	realizedFrame.addRealizedFrameElement(rfe);

    }

    private void processFENode(final Element fenode,
	    final RealizedFrame realizedFrame) {
	String frameElementName = fenode.attributeValue("name");

	List<?> fenodeElements = fenode.elements("fenode");

	FrameElement fe;
	try {
	    fe = realizedFrame.getFrame().getFrameElement(frameElementName);
	} catch (FrameElementNotFoundException e2) {
	    fe = new InteractiveFrameElement(frameElementName);
	    realizedFrame.getFrame().addFrameElement(frameElementName, fe);
	}

	// If there is no fenode-element, we assume that it is
	// a null instantiated frame element
	if (fenodeElements.size() == 0) {

	    addNIRealizedFrameElement(fenode, realizedFrame, fe);

	} else {
	    RealizedFrameElement rfe =
		    new RealizedFrameElement(realizedFrame, fe);
	    realizedFrame.addRealizedFrameElement(rfe);

	    for (Object feNodeObject : fenodeElements) {
		String frameElementTargetId = getIdRef((Element) feNodeObject);
		// If the target is a t/nt in the current sentence
		if (getRealization(frameElementTargetId) != null) {

		    rfe.addTarget(getRealization(frameElementTargetId));

		} else {
		    // Otherwise, the target is outside of this
		    // sentence

		    salsaTigerXML.storeOpenTarget(frameElementTargetId, rfe);
		    // realizedFrame.addRealizedFrameElement(rfe);
		    if (salsaTigerXML.getLogger() != null) {
			salsaTigerXML.getLogger().log(Level.FINE,
				"Cross-sentence link");
		    }
		}

	    }
	}
    }

    /**
     * This method is used internally to navigate through the XML hierarchy.
     * 
     * @param element
     *            The XML element
     * @return A realized frame
     */
    private RealizedFrame getRealizedFrame(final Element element) {
	String framename = element.attributeValue("name");
	Element targetNode = element.element("target");
	String frameTargetId = getIdRef(targetNode.element("fenode"));
	List<?> frameElementNodes = element.elements("fe");
	try {
	    RealizedFrame realizedFrame =
		    new RealizedFrame(frameNet.getFrame(framename),
			    Arrays.asList(this.getRealization(frameTargetId)),
			    element.attributeValue("id"));
	    for (int k = 0; k < frameElementNodes.size(); k++) {

		Element fenode = (Element) frameElementNodes.get(k);
		processFENode(fenode, realizedFrame);

	    }
	    return realizedFrame;
	} catch (FrameNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Returns the token that realizes the given id.
     * 
     * @param id
     *            The id
     * @return The token
     */
    private IToken getRealization(final String id) {
	IToken ret = tree.getTreeElement(id);
	if (ret == null) {
	    for (RealizedFrame rf : this.realizedFrames) {
		if (rf.getIdString().equalsIgnoreCase(id)) {
		    ret = rf.getTarget();
		}
	    }
	}
	return ret;
    }

    /**
     * This method returns the idref attribute value of the element node, if it
     * exists and the element is an fenode-element. If not, an empty string is
     * returned.
     * 
     * @param node
     *            The element
     * @return The value of the idref attribute
     */
    private String getIdRef(final Element node) {
	if (node != null && node.getName().equalsIgnoreCase("fenode")) {
	    return node.attributeValue("idref");
	}
	return "";
    }

    /**
     * Returns the salsatigerxml-object this semantics belong to.
     * 
     * @return The SalsaTigerXML object.
     */
    protected final SalsaTigerXML getSalsaTigerXML() {
	return salsaTigerXML;
    }

}
