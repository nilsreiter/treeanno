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

import java.util.Properties;
import java.util.TreeSet;

/**
 * This class represents an annotated frame element.
 * 
 * @author Nils Reiter
 * 
 */
public class RealizedFrameElement extends AHasTarget implements
	IRealizedFrameElement, IHasID {
    /**
     * The frame element to be realized.
     */
    FrameElement frameElement;
    /**
     * The realized frame to which this frame element belongs.
     */
    RealizedFrame realizedFrame;

    /**
     * The XML identifier.
     */
    String id;

    /**
     * Stores, whether the frame element is instantiated or not. Default: false
     */
    boolean nullInstantiated = false;

    /**
     * Stores the instantiaton type, if the frame element is null instantiated.
     */
    String iType;

    /**
     * Stores additional data, such as grammatical function (which seems to be
     * frame-dependent).
     */
    Properties data = null;

    /**
     * The constructor of the realized frame element.
     * 
     * @param realizedFrame
     *            The realized frame to which this belongs
     * @param frameElement
     *            The frame element to be realized
     * @param target
     *            The target of the annotation
     * @param id
     *            An XML id
     */
    public RealizedFrameElement(final RealizedFrame realizedFrame,
	    final FrameElement frameElement, final IToken target,
	    final String id) {
	this.target_ = new TreeSet<IToken>(new TokenComparator());
	this.target_.add(target);
	this.id = id;
	this.frameElement = frameElement;
	this.realizedFrame = realizedFrame;
    }

    public RealizedFrameElement(final RealizedFrame realizedFrame,
	    final FrameElement frameElement, final String id,
	    final IToken... target) {
	this.target_ = new TreeSet<IToken>(new TokenComparator());
	for (IToken token : target) {
	    this.target_.add(token);
	}
	this.id = id;
	this.frameElement = frameElement;
	this.realizedFrame = realizedFrame;
    }

    /**
     * This constructor creates a new RealizedFrameElement based on the frame
     * and the the frame element. It is used to create frame elements that are
     * not instantiated (i.e., have no target in the sentence)
     * 
     * @param realizedFrame
     *            The realized frame to which this realized frame element
     *            belongs
     * @param frameElement
     *            The frame element
     */
    public RealizedFrameElement(final RealizedFrame realizedFrame,
	    final FrameElement frameElement) {
	this.frameElement = frameElement;
	this.realizedFrame = realizedFrame;
	nullInstantiated = true;
    }

    /**
     * Returns a String representation of this realized frame element.
     * 
     * @return Returns a string representation of this object
     */
    @Override
    public final String toString() {
	if (isNullInstantiated()) {
	    return getIType() + ": " + frameElement.name;
	}
	if (target_ == null) {
	    return "Not yet linked: " + frameElement.name;
	}
	return "\"" + this.getTargetString() + "\": " + frameElement.name;
    }

    /**
     * @return the id
     */
    @Override
    public String getIdString() {
	return id;
    }

    /**
     * Returns the {@link FrameElement} realized by this
     * 
     * @return the frameElement
     */
    @Override
    public FrameElement getFrameElement() {
	return frameElement;
    }

    /**
     * Returns the {@link RealizedFrame} in which this frame element has been
     * realized.
     * 
     * @return the realizedFrame
     */
    @Override
    public RealizedFrame getRealizedFrame() {
	return realizedFrame;
    }

    /**
     * Returns the end position of the target of this frame element. If the FE
     * is null instantiated, the method returns -1.
     * 
     * @return The end position of the target or -1
     */
    public int getEnd() {
	if (isNullInstantiated()) {
	    return -1;
	}
	return target_.last().getCharacterRange().getElement2();
    }

    /**
     * Returns the starting position of the target of this frame element. If the
     * FE is null instantiated, the method returns -1.
     * 
     * @return The starting position of the target or -1
     */
    public int getStart() {

	if (isNullInstantiated()) {
	    return -1;
	}
	return target_.first().getCharacterRange().getElement1();
    }

    @Override
    public boolean isNullInstantiated() {
	return nullInstantiated;
    }

    /**
     * A method to set this realized frame element to null instantiated.
     * 
     * @param nullInstantiated
     *            the new value
     */
    public void setNullInstantiated(final boolean nullInstantiated) {
	this.nullInstantiated = nullInstantiated;
    }

    /**
     * Returns a string that represents the instantiation type of this FE.
     * 
     * @return the instantiation type
     */
    @Override
    public String getIType() {
	return iType;
    }

    /**
     * Sets the instantiation type of this FE.
     * 
     * @param type
     *            The intantiation type of this FE (a string)
     */
    public void setIType(final String type) {
	iType = type;
    }

    /**
     * Sets the property named "key" to the value "value".
     * 
     * @param key
     *            The key of the property
     * @param value
     *            The value
     * @since 0.4
     */
    public void setProperty(final String key, final String value) {
	if (data == null) {
	    data = new Properties();
	}
	data.setProperty(key, value);
    }

    /**
     * Retrieves the property with key "key".
     * 
     * @param key
     * @return The value of the property
     * @since 0.4
     */
    public String getProperty(final String key) {
	return data.getProperty(key);
    }

    /**
     * Returns true, if the sentence, in which the target of the frame appears,
     * is the same sentence than the sentence, in which the target of this frame
     * element appears.
     * 
     * @return true or false
     */
    @Override
    public boolean isCrossingSentenceBoundaries() {
	return getRealizedFrame().getTarget().getSentence() == getTarget()
		.getSentence();
    }

    @Override
    public void addTarget(final IToken target) {
	if (this.target_ == null) {
	    this.target_ = new TreeSet<IToken>(new TokenComparator());
	}
	this.nullInstantiated = false;
	this.target_.add(target);
    }

    @Override
    public IToken getTarget() {
	throw new UnsupportedOperationException();
    }

    /**
     * TODO: This needs cleanup
     * 
     * @return
     * @Override public CharacterRange getTargetCharacterRange() {
     *           CharacterRange r = new CharacterRange(Integer.MAX_VALUE,
     *           Integer.MIN_VALUE);
     * 
     *           for (IToken token : this.getTargetList()) { TreeElement te =
     *           (TreeElement) token; if (r.getElement1() >
     *           te.getTokenRange().getElement1()) {
     *           r.setElement1(te.getTokenRange().getElement1()); } if
     *           (r.getElement2() < te.getTokenRange().getElement2()) {
     *           r.setElement2(te.getTokenRange().getElement2()); } } return r;
     *           }
     */
}
