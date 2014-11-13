package de.uniheidelberg.cl.a10.data2.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.Mention;

/**
 * This class models an entity.
 * 
 * @author hartmann
 * 
 */
public class Entity_impl extends AnnotationObjectInDocument_impl implements Entity {

	Sense_impl sense;
	List<Mention_impl> mentions;

	public Entity_impl(final String id) {
		super(id);
		this.init();
		this.sense = null;
	}

	public Entity_impl(final String id, final Sense_impl sense) {
		super(id);
		this.init();
		this.sense = sense;
	}

	private void init() {
		this.mentions = new ArrayList<Mention_impl>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Entity#getSense()
	 */
	@Override
	public Sense_impl getSense() {
		return this.sense;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Entity#getMentions()
	 */
	@Override
	public List<Mention_impl> getMentions() {
		return this.mentions;
	}

	public void addMention(final Mention_impl men) {
		this.mentions.add(men);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId() + ": ");
		for (Mention m : this.getMentions()) {
			sb.append(m.toString() + " -- ");
		}
		sb.append("[" + this.getSense() + "]");
		return sb.toString();
	}

	@Override
	public Iterator<Mention> iterator() {
		return new Iterator<Mention>() {
			Iterator<Mention_impl> iter = mentions.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Mention next() {
				return iter.next();
			}

			@Override
			public void remove() {
				iter.remove();
			}
		};
	}

	/**
	 * @param sense
	 *            the sense to set
	 */
	public void setSense(final Sense_impl sense) {
		this.sense = sense;
	}

	@Override
	public int indexOf() {
		return 0;
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		s.addAll(getMentions());
		return s;
	}

	public void remove(final AnnotationObjectInDocument obj) {
		this.mentions.remove(obj);
	}
}
