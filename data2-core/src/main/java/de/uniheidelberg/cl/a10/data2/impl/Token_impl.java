package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Sense;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * This class models a token.
 * 
 * @author hartmann
 * 
 */
public class Token_impl extends AnnotationObjectInDocument_impl implements
		Token {

	/**
	 * The token surface
	 */
	final char[] surface;

	/**
	 * The sense of the token
	 */
	Sense_impl sense = null;

	/**
	 * The dependency relation to the governor
	 */
	String dependencyRelation = null;

	/**
	 * The part of speech, as a string
	 */
	String partOfSpeech;

	/**
	 * The lemma
	 */
	String lemma;

	/**
	 * The governor of the token, as in dependency syntax
	 */
	Token_impl governor;

	String oldId;

	/**
	 * A temporary storage for a dependency-induced frame. See
	 * {@link DependencyFrame_impl}.
	 */
	DependencyFrame_impl dependencyFrame = null;

	/**
	 * The sentence in which this token appears
	 */
	Sentence_impl sentence = null;

	/**
	 * The frames evoked by this token
	 */
	List<Frame_impl> frames = new LinkedList<Frame_impl>();

	/**
	 * The mentions in which this token appears.
	 */
	Collection<Mention_impl> mentions = new HashSet<Mention_impl>();;

	int begin;
	int end;

	/**
	 * The frame elements this token represents
	 */
	Collection<FrameElm_impl> frameElms = new LinkedList<FrameElm_impl>();;

	public Token_impl(final String id, final String surface) {
		super(id);
		this.surface = surface.toCharArray();
		this.init();
	}

	private void init() {
	}

	/**
	 * Creates a new Token instance with the specified properties.
	 * 
	 * @param id
	 *            The unique id of this token.
	 * @param posTag
	 *            The pos tag of this token.
	 * @param surface
	 *            The surface of this token.
	 * @param sense
	 * 
	 * @param deprel
	 * 
	 * @param lemma
	 * 
	 * @param governor
	 * 
	 * @param tokenPosition
	 *            position of this token in the document
	 * 
	 * @param startCharPosition
	 *            position of first token character
	 * 
	 * @param endCharPosition
	 *            position of last token character
	 */
	public Token_impl(final String surface, final String id,
			final String posTag, final Sense_impl sense, final String deprel,
			final String lemma, final Token_impl governor,
			final int tokenPosition, final int startCharPos,
			final int endCharPos) {
		super(id);
		this.begin = startCharPos;
		this.end = endCharPos;
		this.surface = surface.toCharArray();
		this.partOfSpeech = posTag;
		this.sense = sense;
		this.dependencyRelation = deprel;
		this.lemma = lemma;
		this.governor = governor;
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getSense()
	 */

	@Override
	public Sense getSense() {
		return this.sense;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getLemma()
	 */
	@Override
	public String getLemma() {
		return this.lemma;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getGovernor()
	 */
	@Override
	public Token_impl getGovernor() {
		return this.governor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getFrames()
	 */
	@Override
	public List<Frame_impl> getFrames() {
		return this.frames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getFrameElms()
	 */
	@Override
	public Collection<FrameElm_impl> getFrameElms() {
		return this.frameElms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getSentence()
	 */
	@Override
	public Sentence_impl getSentence() {
		return this.sentence;
	}

	public void addFrame(final Frame_impl frame) {
		this.frames.add(frame);

	}

	public void addFrameElm(final FrameElm_impl frameElm) {
		this.frameElms.add(frameElm);
	}

	@Override
	public String toString() {
		String out = this.getSurface() + " (" + this.getPartOfSpeech() + ")";
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getDependencyRelation()
	 */
	@Override
	public String getDependencyRelation() {
		return dependencyRelation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getDependents()
	 */
	@Override
	public Collection<Token> getDependents() {
		Collection<Token> deps = new HashSet<Token>();
		if (this.getSentence() != null) {
			for (Token token : this.getSentence()) {
				if (token.getGovernor() == this) {
					deps.add(token);
				}
			}
		}
		return deps;
	}

	/**
	 * @param dependencyRelation
	 *            the dependencyRelation to set
	 */
	public void setDependencyRelation(final String dependencyRelation) {
		this.dependencyRelation = dependencyRelation.intern();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getPartOfSpeech()
	 */
	@Override
	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	/**
	 * @param partOfSpeech
	 *            the partOfSpeech to set
	 */
	public void setPartOfSpeech(final String partOfSpeech) {
		this.partOfSpeech = partOfSpeech.intern();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getSurface()
	 */
	@Override
	public String getSurface() {
		return new String(surface);
	}

	/**
	 * @param sense
	 *            the sense to set
	 */
	public void setSense(final Sense_impl sense) {
		this.sense = sense;
	}

	/**
	 * @param lemma
	 *            the lemma to set
	 */
	public void setLemma(final String lemma) {
		this.lemma = lemma;
	}

	/**
	 * @param governor
	 *            the governor to set
	 */
	public void setGovernor(final Token_impl governor) {
		this.governor = governor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getDependencyFrame()
	 */
	@Override
	public DependencyFrame_impl getDependencyFrame() {
		if (this.dependencyFrame == null) {
			this.dependencyFrame = new DependencyFrame_impl(this);
			this.dependencyFrame.setFrameName(this.getLemma());
			this.dependencyFrame.setRitualDocument(this.getRitualDocument());
			for (Token tok : this.getDependents()) {
				if (!tok.getDependencyRelation().equalsIgnoreCase("PUNCT")) {
					DependencyFrameElement_impl dfe = new DependencyFrameElement_impl(
							this.dependencyFrame, tok);
					this.dependencyFrame.addFrameElm(dfe);
				}
			}
		}
		return this.dependencyFrame;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(final Sentence_impl sentence) {
		this.sentence = sentence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getBegin()
	 */
	@Override
	public int getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(final int begin) {
		this.begin = begin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getEnd()
	 */
	@Override
	public int getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(final int end) {
		this.end = end;
	}

	@Override
	public int compareTo(final Token tok) {
		if (this.getEnd() < tok.getEnd()) {
			return -1;
		} else if (this.getEnd() > tok.getEnd()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean addMention(final Mention_impl arg0) {
		return mentions.add(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getMentions()
	 */
	@Override
	public Collection<Mention_impl> getMentions() {
		return mentions;
	}

	@Override
	public int indexOf() {
		return this.getBegin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.impl.Token#getOldId()
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
	public Set<AnnotationObjectInDocument> getRelated() {
		Set<AnnotationObjectInDocument> s = new HashSet<AnnotationObjectInDocument>();
		s.addAll(getFrames());
		s.addAll(getDependents());
		s.addAll(getMentions());
		s.addAll(getFrameElms());
		if (getGovernor() != null)
			s.add(getGovernor());
		// s.add(getDependencyFrame());
		s.add(getSentence());
		return s;
	}

	@Override
	public boolean isWord() {
		return this.getPartOfSpeech().matches("\\p{Alpha}+");
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}
}
