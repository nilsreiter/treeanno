package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.Chunk;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;

/**
 * This class models a chunk.
 * 
 * @author hartmann
 * 
 */
public class Chunk_impl extends HasTokens_impl implements Chunk {

	Sentence_impl sentence = null;

	String category = null;

	public Chunk_impl(final String id) {
		super(id);

	}

	@Override
	public String toString() {
		return this.getId();
		/*
		 * StringBuffer sb = new StringBuffer(); sb.append(this.getId() + ": ");
		 * for (Token tok : this.getTokens()) { sb.append(tok.toString() +
		 * " -- "); } sb.append("[" + this.getCategory() + "]"); return
		 * sb.toString();
		 */
	}

	/**
	 * @return the category
	 */
	@Override
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(final String category) {
		this.category = category;
	}

	@Override
	public Sentence_impl getSentence() {
		return sentence;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(final Sentence_impl sentence) {
		this.sentence = sentence;
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		s.add(getSentence());
		return s;
	}

}
