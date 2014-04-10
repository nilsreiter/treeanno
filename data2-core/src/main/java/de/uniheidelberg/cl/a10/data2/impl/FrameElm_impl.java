package de.uniheidelberg.cl.a10.data2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * This class models a frame element.
 * 
 * @author hartmann
 * 
 */
public class FrameElm_impl extends HasTokensAndHead_impl implements
		FrameElement {

	Collection<Mention_impl> mentions = null;
	Frame_impl frame = null;
	String name = null;

	/**
	 * true if this frame element's corresponding mentions belong all to the
	 * same entity, false otherwise
	 */
	boolean hasOneToOneMapping = false;
	List<Entity_impl> inLinkingEntities;

	public FrameElm_impl(final String id, final ArrayList<Token> tokens,
			final Collection<Mention_impl> mentions) {
		super(id);
		this.init();
		this.tokens = tokens;
		this.mentions = mentions;
	}

	public FrameElm_impl(final String id) {
		super(id);
		this.init();
	}

	private void init() {
		this.mentions = new HashSet<Mention_impl>();
		this.inLinkingEntities = new ArrayList<Entity_impl>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.FrameElement#getMentions()
	 */
	@Override
	public Collection<Mention_impl> getMentions() {
		return this.mentions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.impl.FrameElement#getCoreferringMentions()
	 */
	@Override
	public Collection<Mention> getCoreferringMentions() {
		Set<Mention> mentions = new HashSet<Mention>();
		for (Mention mention : this.getMentions()) {
			mentions.addAll(mention.getEntity().getMentions());
		}
		return mentions;
	}

	public void setFrame(final Frame_impl frame) {
		this.frame = frame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.FrameElement#getFrame()
	 */
	@Override
	public Frame_impl getFrame() {
		return this.frame;
	}

	public String getDocId() {
		if (this.getRitualDocument() != null)
			return this.getRitualDocument().getId();
		return null;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(this.getId() + ": ");
		for (Token t : this.tokens) {
			s.append(t.getSurface() + " ");
		}
		s.append("[" + this.getName() + "]");
		return s.toString();
	}

	public String toString(final boolean coref) {
		if (coref) {
			StringBuilder b = new StringBuilder();
			b.append('{');
			b.append('[');
			for (Token t : this.tokens) {
				b.append(t.getSurface() + " ");
			}
			b.append(']');
			Set<String> mentionStrings = new HashSet<String>();
			for (Mention m : this.getCoreferringMentions()) {
				mentionStrings.add(m.getMentionString());
			}
			b.append(mentionStrings.toString());
			b.append('}');
			return b.toString();
		} else {
			return toString();
		}
	}

	public String toFormattedString(final boolean coref) {
		if (coref) {
			StringBuilder b = new StringBuilder();
			b.append('{');
			b.append('[');
			int c = 0;
			for (Token t : this.tokens) {
				b.append(t.getSurface() + " ");
				if (c > 2) {
					b.append("\\n");
					c = 0;
				}
				c++;
			}
			b.append(']');
			b.append("\\n");
			Set<String> mentionStrings = new HashSet<String>();
			/*
			 * for (Mention m : this.getCoreferringMentions()) {
			 * mentionStrings.add(m.getMentionString() + "\\n"); }
			 */
			b.append(mentionStrings.toString());
			b.append('}');
			return b.toString();
		} else {
			return toString();
		}
	}

	public void setOneToOneMapping(final boolean bool) {
		this.hasOneToOneMapping = bool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.FrameElement#hasOneToOneMapping()
	 */
	@Override
	public boolean hasOneToOneMapping() {
		return this.hasOneToOneMapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.FrameElement#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public void addInLinkingEntity(final Entity_impl e) {
		this.inLinkingEntities.add(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.impl.FrameElement#getInLinkingEntities()
	 */
	@Override
	public List<Entity_impl> getInLinkingEntities() {
		return this.inLinkingEntities;
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(final Mention_impl arg0) {
		return mentions.add(arg0);
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		s.add(getFrame());
		s.addAll(getMentions());
		s.addAll(getTokens());
		return s;
	}

}
