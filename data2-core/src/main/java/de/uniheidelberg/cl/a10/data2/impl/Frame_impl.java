package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * This class models a frame.
 * 
 * @author hartmann
 * 
 */
public class Frame_impl extends HasTokens_impl implements Frame {

	String frameName;

	Set<FrameElm_impl> frameElms;

	/**
	 * This is the id used in the SalTo-annotations
	 */
	String oldId;

	public Frame_impl(final String id) {
		super(id);
		this.init();
	}

	private void init() {
		this.frameElms = new HashSet<FrameElm_impl>();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.impl.Frame#addFrameElm(de.uniheidelberg
	 * .cl.a10.data2.impl.FrameElm_impl)
	 */

	public void addFrameElm(final FrameElm_impl frameElm) {
		this.frameElms.add(frameElm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Frame#getFrameElms()
	 */
	@Override
	public Set<FrameElm_impl> getFrameElms() {
		return this.frameElms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.impl.Frame#getFrameElm(java.lang.String)
	 */
	@Override
	public FrameElm_impl getFrameElm(final String feName) {
		for (FrameElm_impl fe : this.getFrameElms()) {
			if (fe.getName().equals(feName))
				return fe;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Frame#toExtendedString()
	 */
	@Override
	public String toExtendedString() {
		StringBuffer s = new StringBuffer();
		s.append(this.getId() + ": ");
		s.append(this.frameName + " (");
		s.append(this.getTokens().get(0).getSurface());
		s.append(")");
		/*
		 * for (FrameElm fe : this.frameElms) { s.append(fe.toString() +
		 * " -- "); } s.append("[" + this.getFrameName() + "]");
		 */return s.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Frame#getFrameName()
	 */
	@Override
	public String getFrameName() {
		return frameName;
	}

	/**
	 * @param frameName
	 *            the frameName to set
	 */
	public void setFrameName(final String frameName) {
		this.frameName = frameName;
	}

	/**
	 * @return the oldId
	 */
	@Override
	public String getOldId() {
		return oldId;
	}

	/**
	 * @param oldId
	 *            the oldId to set
	 */
	public void setOldId(final String oldId) {
		this.oldId = oldId;
	}

	@Override
	public Token getTarget() {
		return this.getTokens().get(0);
	}

	@Override
	public String toString() {
		return this.getRitualDocument().getId() + " " + this.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Frame#getById(java.lang.String)
	 */
	@Override
	public FrameElement getById(final String id) {
		for (FrameElm_impl fe : frameElms) {
			if (fe.getId().equals(id))
				return fe;
		}
		return null;
	}

	@Deprecated
	public Collection<FrameElm_impl> getCollection() {
		return frameElms;
	}

	@Override
	public int indexOf() {
		return this.getRitualDocument().getFrames().indexOf(this);
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		s.addAll(getFrameElms());
		s.addAll(getTokens());
		s.add(getTarget());
		return s;
	}

}
