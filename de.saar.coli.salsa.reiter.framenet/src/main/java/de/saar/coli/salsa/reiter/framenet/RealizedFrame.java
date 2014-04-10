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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.reiter.pos.FN;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.util.BasicPair;

/**
 * This class represents a frame and a target (a "word" in a sentence). The
 * target may be some id, depending on the properties of the parsing procedure.
 * 
 * @author Nils Reiter
 * 
 */
public class RealizedFrame extends AHasTarget implements IHasID, IRealizedFrame {
    /**
     * The frame.
     */
    Frame frame;
    /**
     * The sentence in which the target occurs.
     */
    ISentence sentence = null;
    /**
     * An XML id.
     */
    String id;
    /**
     * A map of frame elements.
     */
    Map<String, RealizedFrameElement> frameElements;
    /**
     * A map of generalizations.
     * 
     * @deprecated
     */
    @Deprecated
    Map<Frame, RealizedFrame> generalizations;

    /**
     * The constructor.
     * 
     * @param frame
     *            The frame to be realized
     * @param target
     *            The target of the annotation
     * @param id
     *            An id
     */
    @Deprecated
    public RealizedFrame(final Frame frame, final IToken target, final String id) {
	this.frame = frame;
	this.target_.add(target);
	this.id = id;
	// this.sentence = target;
	frameElements = new HashMap<String, RealizedFrameElement>();
	generalizations = new HashMap<Frame, RealizedFrame>();
    }

    /**
     * The constructor.
     * 
     * @param frame
     *            The frame to be realized
     * @param target
     *            The target of the annotation, a collection of tokens
     * @param id
     *            An id
     */
    public RealizedFrame(final Frame frame, final Collection<IToken> target,
	    final String id) {
	this.frame = frame;

	this.target_.addAll(target);
	this.id = id;
	frameElements = new HashMap<String, RealizedFrameElement>();
	generalizations = new HashMap<Frame, RealizedFrame>();

    }

    /**
     * Adds a realized frame element to the realized frame. The frame element
     * has to belong to this frame
     * 
     * @param realizedFrameElement
     *            The realized frame element
     */
    public void addRealizedFrameElement(
	    final RealizedFrameElement realizedFrameElement) {
	// if (!
	// this.getFrame().frameElements().contains(realizedFrameElement.frameElement))
	// throw new FrameElementNotFoundException(this.getFrame(),
	// realizedFrameElement.frameElement);
	frameElements.put(realizedFrameElement.frameElement.name,
		realizedFrameElement);
    }

    /**
     * Adds a new realized frame element. Sets xmlid to 0.
     * 
     * @param fename
     *            The name of the frame element
     * @param target
     *            The target of the frame element
     * @throws FrameElementNotFoundException
     *             If the frame element does not belong to the frame
     * @return The realized frame element
     */
    public RealizedFrameElement addRealizedFrameElement(final String fename,
	    final IToken target) throws FrameElementNotFoundException {
	RealizedFrameElement rfe =
		new RealizedFrameElement(this, frame.getFrameElement(fename),
			target, "");
	frameElements.put(fename, rfe);
	return rfe;
    }

    /**
     * Similar to {@link #addRealizedFrameElement(String, IToken)}, but allows
     * adding of frame elements spanning over multiple tokens.
     * 
     * @param fename
     *            The name of the frame element
     * @param target
     *            A list of tokens
     * @return The realized frame element
     * @throws FrameElementNotFoundException
     *             If the frame element does not exist in this frame
     */
    public RealizedFrameElement addRealizedFrameElement(final String fename,
	    final List<IToken> target) throws FrameElementNotFoundException {
	RealizedFrameElement rfe = null;
	if (frameElements.containsKey(fename)) {
	    rfe = frameElements.get(fename);
	    for (IToken token : target) {
		rfe.addTarget(token);
	    }
	} else {
	    rfe =
		    new RealizedFrameElement(this,
			    frame.getFrameElement(fename), "",
			    target.toArray(new IToken[target.size()]));
	    frameElements.put(fename, rfe);

	}
	return rfe;
    }

    @Override
    public String toString() {
	StringBuffer s =
		new StringBuffer(target_ + ": \"" + frame.getName() + "\" (");
	for (String fename : frameElements.keySet()) {
	    s.append(frameElements.get(fename));
	    s.append(", ");
	}
	s.append(")");
	return s.toString();
    }

    /**
     * Returns a sorted set of realized frame elements that have been set for
     * this frame The realized frame elements are sorted according to the
     * starting character position of the target. If none has been specified,
     * the ordering is undefined.
     * 
     * @return A sorted set of RealizedFrameElements
     */
    @Override
    public SortedSet<RealizedFrameElement> frameElements() {
	SortedSet<RealizedFrameElement> ret =
		new TreeSet<RealizedFrameElement>();
	ret.addAll(frameElements.values());
	return ret;
    }

    /**
     * This method returns a sorted set of realized frame elements that are
     * overt, i.e., not null instantiated. The ordering works as in
     * {@link RealizedFrame#frameElements()}.
     * 
     * @return A sorted set
     * @since 0.4.1
     */
    @Override
    public SortedSet<RealizedFrameElement> overtFrameElements() {
	SortedSet<RealizedFrameElement> ret =
		new TreeSet<RealizedFrameElement>();
	for (RealizedFrameElement rfe : frameElements()) {
	    if (!rfe.isNullInstantiated()) {
		ret.add(rfe);
	    }
	}
	return ret;

    }

    /**
     * Generalizes all content of this frame. Returns all possible
     * generalizations
     * 
     * @return A collection of realized frames, but more general than this one
     */
    public Collection<RealizedFrame> generalizeAll() {
	Collection<RealizedFrame> rfs = new HashSet<RealizedFrame>();
	for (Frame f : frame.inheritsFrom()) {
	    if (generalizable(f)) {
		RealizedFrame gen = generalize(f);
		if (gen.generalizable()) {
		    rfs.addAll(gen.generalizeAll());
		} else {
		    rfs.add(gen);
		}
	    }
	}
	return rfs;
    }

    /**
     * Returns true if this is generalizable to the given frame.
     * 
     * @param frame
     *            The frame to which one wants to generalize
     * @return true or false
     */
    public boolean generalizable(final Frame frame) {
	return (generalize(frame) != this);
    }

    /**
     * This method returns true if there is a frame to which this frame can be
     * generalized.
     * 
     * @return true or false
     */
    public boolean generalizable() {
	for (Frame frame : this.frame.inheritsFrom()) {
	    if (generalizable(frame)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Tries to generalize this frame to the level of given frame, i.e., tries
     * to realize the given frame with the targets of this frame.
     * 
     * @param frame
     *            The frame to which one wants to generalize
     * @return The generalized realized frame or this, if no generalization is
     *         possible
     */
    public RealizedFrame generalize(final Frame frame) {
	// TODO (fixed): Generalization of realized frames does not work
	// completly ...
	// some frame elements are broken
	// Fixed in rev. 48

	RealizedFrame gFrame = frame.realize(this.getTarget(), id);
	if (getSentence() != null) {
	    gFrame.setSentence(getSentence());
	}
	if (getTarget() != null) {
	    gFrame.setTarget(getTarget());
	}
	if (getFrame().inheritsFrom().isEmpty()
		|| !getFrame().inheritsFrom().contains(frame)) {
	    return this;
	}

	for (RealizedFrameElement rfe : frameElements()) {
	    // System.err.println(rfe);
	    FrameElement gFrameElement = rfe.frameElement.inheritsFrom(frame);
	    if (gFrameElement == null) {
		return this;
	    }
	    RealizedFrameElement gfe =
		    gFrameElement.realize(gFrame, rfe.getTarget(),
			    rfe.getIdString());
	    if (rfe.getTarget() != null) {
		gfe.setTarget(getTarget());
	    }
	    gfe.setIType(rfe.getIType());
	}
	return gFrame;

    }

    /**
     * Returns the frame realized by this RealizedFrame.
     * 
     * @return the frame
     */
    @Override
    public Frame getFrame() {
	return frame;
    }

    /**
     * Returns the realized frame elements in this realized frame. Note, that
     * this method only returns the frame elements that are actually used.
     * 
     * @return the frameElements
     */
    @Override
    public Map<String, RealizedFrameElement> getFrameElements() {
	return frameElements;
    }

    /**
     * This method returns realized frame elements that are overt, i.e., not
     * null instantiated.
     * 
     * @return The overt realized frame elements
     * @since 0.4.1
     */
    @Override
    public Map<String, RealizedFrameElement> getOvertFrameElements() {
	Map<String, RealizedFrameElement> map =
		new HashMap<String, RealizedFrameElement>();

	for (RealizedFrameElement rfe : frameElements.values()) {
	    if (!rfe.isNullInstantiated()) {
		map.put(rfe.getFrameElement().getName(), rfe);
	    }
	}

	return map;
    }

    /**
     * @return the xmlid
     */
    @Override
    public String getIdString() {
	return id;
    }

    /**
     * Returns the sentence in which the target occurs.
     * 
     * @return the sentence
     */
    @Override
    public ISentence getSentence() {
	return sentence;
    }

    /**
     * Sets the sentence in which this realized frame element occurrs.
     * 
     * @param sentence
     *            the sentence to set
     */
    public void setSentence(final ISentence sentence) {
	this.sentence = sentence;
    }

    public void setSentence(final Sentence sentence) {
	this.sentence = sentence;
    }

    /**
     * Returns the end position of the target of the realized frame.
     * 
     * @return An integer giving the last position
     */
    public int getEnd() {
	return this.getTarget().getCharacterRange().getElement2();
    }

    /**
     * Returns the start position of the target of the realized frame.
     * 
     * @return An integer giving the starting position
     */
    public int getStart() {
	return this.getTarget().getCharacterRange().getElement1();
    }

    @Override
    public boolean isNullInstantiated() {
	return false;
    }

    /**
     * This method returns the lemma as a pair.
     * 
     * @return A pair consisting of the lemma and the part of speech
     */
    public BasicPair<String, FN> getLemma() {
	if (this.getTargetList().size() == 1) {
	    IToken target = this.getTarget(0);
	    return new BasicPair<String, FN>(target.getProperty("LEMMA"),
		    target.getPartOfSpeech().asFN());
	} else if (this.getTargetList().size() == 0) {
	    return null;
	} else {
	    StringBuffer buf = new StringBuffer();
	    IPartOfSpeech pos = null;
	    for (IToken token : this.getTargetList()) {
		buf.append(token.getProperty("LEMMA"));
		buf.append(' ');
		if (pos == null) {
		    pos = token.getPartOfSpeech();
		}
	    }
	    return new BasicPair<String, FN>(buf.toString().trim(), pos.asFN());

	}
    }

}
