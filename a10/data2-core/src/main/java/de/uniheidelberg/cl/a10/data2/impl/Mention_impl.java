package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * This class models a mention.
 * 
 * @author hartmann
 * 
 */
public class Mention_impl extends HasTokens_impl implements Mention {

	/**
	 * The entity this mention belongs to.
	 */
	Entity_impl entity = null;

	/**
	 * The frame elements this mention represents.
	 */
	Collection<FrameElm_impl> frameElms;

	double confidence = Double.NaN;

	public Mention_impl(final String id) {
		super(id);
		this.init();
	}

	private void init() {
		this.frameElms = new LinkedList<FrameElm_impl>();
	}

	public void addFrameElm(final FrameElm_impl frameElm) {
		this.frameElms.add(frameElm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Mention#getFrameElms()
	 */
	@Override
	public Collection<? extends FrameElement> getFrameElms() {
		return this.frameElms;
	}

	public void setEntity(final Entity_impl ent) {
		this.entity = ent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Mention#getEntity()
	 */
	@Override
	public Entity_impl getEntity() {
		return this.entity;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(this.getId() + ": ");
		for (Token t : this.tokens) {
			s.append(t.getSurface() + " ");
		}
		return s.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Mention#getSurface()
	 */
	@Override
	public String getSurface() {
		StringBuffer s = new StringBuffer();
		for (Token t : this.tokens) {
			s.append(t.getSurface() + " ");
		}
		return s.toString().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Mention#getConfidence()
	 */
	@Override
	public double getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence
	 *            the confidence to set
	 */
	public void setConfidence(final double confidence) {
		this.confidence = confidence;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(final FrameElm_impl e) {
		return frameElms.add(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Mention#getMentionString()
	 */
	@Override
	public String getMentionString() {
		StringBuffer sb = new StringBuffer();
		for (Token tok : this.tokens) {
			sb.append(tok.getSurface() + "_");
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		// s.add(getEntity());
		s.addAll(getTokens());
		s.addAll(getFrameElms());
		return s;
	}

}
