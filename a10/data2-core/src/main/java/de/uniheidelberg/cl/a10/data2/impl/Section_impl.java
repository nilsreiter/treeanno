package de.uniheidelberg.cl.a10.data2.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Section;
import de.uniheidelberg.cl.a10.data2.Sentence;

/**
 * This class models a section, which is a collection of sentences.
 * 
 * @author hartmann
 * 
 */
public class Section_impl extends AnnotationObjectInDocument_impl implements Section {

	private final ArrayList<Sentence> sents;
	private String ritualText;

	public Section_impl(final String id) {
		super(id);
		this.sents = new ArrayList<Sentence>();
	}

	public void addSentence(final Sentence_impl sent) {
		this.sents.add(sent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Section#getSentences()
	 */
	@Override
	public ArrayList<Sentence> getSentences() {
		return this.sents;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setRitualText(final String text) {
		this.ritualText = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Section#getRitualText()
	 */
	@Override
	public String getRitualText() {
		return this.ritualText;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId() + ": ");
		for (Sentence sent : this.getSentences()) {
			sb.append(sent.getId() + " -- ");
		}
		return sb.toString();
	}

	@Override
	public Iterator<Sentence> iterator() {
		return this.sents.iterator();
	}

	@Override
	public int indexOf() {
		return this.getRitualDocument().getSections().indexOf(this);
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		// TODO Auto-generated method stub
		return null;
	}
}
