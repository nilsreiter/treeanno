package de.uniheidelberg.cl.a10.data2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.Chunk;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * This class models a sentence.
 * 
 * @author hartmann
 * 
 */
public class Sentence_impl extends HasTokens_impl implements Sentence {

	Section_impl section;

	Collection<Chunk> chunks;

	Token root = null;

	public Sentence_impl(final String id) {
		super(id);
		this.init();
		this.section = null;
	}

	public Sentence_impl(final String id, final ArrayList<Token> tokens,
			final Section_impl section) {
		super(id);
		this.init();
		this.tokens = tokens;
		this.section = section;
	}

	private void init() {
		this.section = null;
		this.chunks = new HashSet<Chunk>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Sentence#getSection()
	 */
	@Override
	public Section_impl getSection() {
		return this.section;
	}

	public void setSection(final Section_impl sec) {
		this.section = sec;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId() + ": ");
		for (Token t : this.getTokens()) {
			sb.append(t.toString() + " ");
		}
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Sentence_impl))
			return false;
		Sentence_impl sent = (Sentence_impl) obj;
		if (this.tokens.size() != sent.getTokens().size())
			return false;
		for (int i = 0; i < this.tokens.size(); i++) {
			if (!this.tokens.get(i).equals(sent.getTokens().get(i)))
				return false;
		}
		return true;
	}

	public void addChunk(final Chunk chunk) {
		this.chunks.add(chunk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Sentence#getChunks()
	 */
	@Override
	public Collection<Chunk> getChunks() {
		return chunks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Sentence#getRoot()
	 */
	@Override
	public Token getRoot() {
		if (root == null) {
			for (Token tok : this) {
				if (tok.getGovernor() == null) {
					root = tok;
					return root;
				}
			}
		}
		return root;
	}

	@Override
	public int indexOf() {
		return this.getRitualDocument().getSentences().indexOf(this);
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		s.addAll(this.getChunks());
		s.addAll(this.getTokens());
		if (this.getSection() != null)
			s.add(this.getSection());
		return s;
	}

}
