package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Chunk;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Section;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * This class models a RitualDocument. It stores Tokens, Mentions, Entities,
 * Senses and FrameElements in an unordered manner. Sentences, Sections and
 * Frames are stored in an ordered manner. Frames are stored both in their
 * textual and temporal order.
 * 
 * @author hartmann
 * 
 */
public class Document_impl extends AnnotationObject_impl implements
		Comparable<Document_impl>, Document {

	/**
	 * Map storing all tokens in this document, mapping from tokenId to Token.
	 */
	private final Map<String, Token_impl> mapOfTokens;

	/**
	 * Map storing all mentions in this document, mapping from mentionId to
	 * Mention.
	 */
	private final Map<String, Mention_impl> mapOfMentions;

	/**
	 * Map storing all entities in this document, mapping from entityId to
	 * Entity.
	 */
	private final Map<String, Entity_impl> mapOfEntities;

	/**
	 * Map storing all chunks in this document, mapping from chunkId to Chunk.
	 */
	private final Map<String, Chunk> mapOfChunks;

	private final Map<String, Sense_impl> mapOfSenses;
	private final Map<String, FrameElm_impl> mapOfFrameElements;
	private final Map<String, Sentence_impl> mapOfSentences;
	private final Map<String, Frame_impl> mapOfFrames;
	private final Map<String, Section_impl> mapOfSections;
	private final Map<String, Mantra_impl> mapOfMantras;

	/**
	 * List of all frames in this document stored in their textual ordering (how
	 * they occur in the text).
	 */
	transient List<Frame_impl> listOfFrames;

	/**
	 * List of all frames in this document stored in their temporal ordering.
	 */
	@Deprecated
	List<Frame_impl> frames_temporalOrdering;

	/**
	 * List of all sections in this document.
	 */
	transient List<Section_impl> listOfSections = null;

	/**
	 * List of all mantras in this document.
	 */
	transient List<Mantra_impl> listOfMantras = null;

	/**
	 * List of all sentences in this document.
	 */
	transient List<Sentence_impl> listOfSentences = null;

	/**
	 * List of all tokens in this document
	 */
	transient List<Token> listOfTokens = null;

	transient List<Event> listOfEvents = null;

	private char[] originalText;

	private String documentTitle = null;

	String corpusName;

	public Document_impl(final String id) {
		super(id);
		this.mapOfSenses = new HashMap<String, Sense_impl>();
		this.mapOfTokens = new HashMap<String, Token_impl>();
		this.mapOfFrames = new HashMap<String, Frame_impl>();
		this.mapOfChunks = new HashMap<String, Chunk>();
		this.mapOfMantras = new HashMap<String, Mantra_impl>();
		this.mapOfMentions = new HashMap<String, Mention_impl>();
		this.mapOfEntities = new HashMap<String, Entity_impl>();
		this.mapOfSections = new HashMap<String, Section_impl>();
		this.mapOfSentences = new HashMap<String, Sentence_impl>();
		this.mapOfFrameElements = new HashMap<String, FrameElm_impl>();

		this.frames_temporalOrdering = new LinkedList<Frame_impl>();

		this.listOfEvents = new LinkedList<Event>();
	}

	public void resetLists() {
		this.listOfTokens = null;
		this.listOfFrames = null;
		this.listOfMantras = null;
		this.listOfSections = null;
		this.listOfSentences = null;
	}

	protected void fixSentences() {
		Sentence_impl[] sentence_array =
				mapOfSentences.values().toArray(new Sentence_impl[0]);
		Arrays.sort(sentence_array);
		this.listOfSentences = Arrays.asList(sentence_array);
	}

	protected void fixSections() {
		Section_impl[] section_array =
				mapOfSections.values().toArray(new Section_impl[0]);
		Arrays.sort(section_array);
		this.listOfSections = Arrays.asList(section_array);
	}

	protected void fixMantras() {
		Mantra_impl[] mantra_array =
				mapOfMantras.values().toArray(new Mantra_impl[0]);
		Arrays.sort(mantra_array);
		this.listOfMantras = Arrays.asList(mantra_array);
	}

	protected void fixTokens() {
		Token[] array = mapOfTokens.values().toArray(new Token_impl[0]);
		Arrays.sort(array);
		this.listOfTokens = Arrays.asList(array);
	}

	protected void fixFrames() {
		Frame_impl[] array = mapOfFrames.values().toArray(new Frame_impl[0]);
		Arrays.sort(array);
		this.listOfFrames = Arrays.asList(array);
	}

	/**
	 * adds a token to the document
	 * 
	 * @param tok
	 *            Token to be added
	 */
	public void addToken(final Token_impl tok) {
		this.mapOfTokens.put(tok.getId(), tok);
	}

	public void addMantra(final Mantra_impl mantra) {
		this.mapOfMantras.put(mantra.getId(), mantra);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getTokens()
	 */
	@Override
	public List<Token> getTokens() {
		if (this.listOfTokens == null) this.fixTokens();
		return listOfTokens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getTokenById(java.lang.String
	 * )
	 */
	@Override
	public Token_impl getTokenById(final String id) {
		return this.mapOfTokens.get(id);
	}

	public void addMention(final Mention_impl men) {
		this.mapOfMentions.put(men.getId(), men);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getMentions()
	 */
	@Override
	public Collection<Mention_impl> getMentions() {
		return this.mapOfMentions.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getMentionById(java.lang
	 * .String)
	 */
	@Override
	public Mention_impl getMentionById(final String id) {
		return this.mapOfMentions.get(id);
	}

	public void addEntity(final Entity_impl ent) {
		this.mapOfEntities.put(ent.getId(), ent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getEntities()
	 */
	@Override
	public Collection<Entity_impl> getEntities() {
		return this.mapOfEntities.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getEntityById(java.lang.
	 * String)
	 */
	@Override
	public Entity getEntityById(final String id) {
		return this.mapOfEntities.get(id);
	}

	public void addSense(final Sense_impl sense) {
		this.mapOfSenses.put(sense.getId(), sense);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getSenses()
	 */
	@Override
	public Collection<Sense_impl> getSenses() {
		return this.mapOfSenses.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getSenseById(java.lang.String
	 * )
	 */
	@Override
	public Sense_impl getSenseById(final String id) {
		return this.mapOfSenses.get(id);
	}

	public void addFrameElm(final FrameElm_impl fe) {
		this.mapOfFrameElements.put(fe.getId(), fe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getFrameElms()
	 */
	@Override
	public Collection<? extends FrameElement> getFrameElms() {
		return this.mapOfFrameElements.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getFrameElmById(java.lang
	 * .String)
	 */
	@Override
	public FrameElm_impl getFrameElmById(final String id) {
		return this.mapOfFrameElements.get(id);
	}

	public void addSentence(final Sentence_impl sent) {
		this.mapOfSentences.put(sent.getId(), sent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getSentences()
	 */
	@Override
	public List<Sentence_impl> getSentences() {
		if (this.listOfSentences == null) this.fixSentences();
		return this.listOfSentences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getSentenceById(java.lang
	 * .String)
	 */
	@Override
	public Sentence_impl getSentenceById(final String id) {
		return this.mapOfSentences.get(id);
	}

	public void addChunk(final Chunk chunk) {
		this.mapOfChunks.put(chunk.getId(), chunk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getChunks()
	 */
	@Override
	public Collection<Chunk> getChunks() {
		return this.mapOfChunks.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getChunkById(java.lang.String
	 * )
	 */
	@Override
	public Chunk getChunkById(final String id) {
		return this.mapOfChunks.get(id);
	}

	public void addSection(final Section_impl sec) {
		this.mapOfSections.put(sec.getId(), sec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getSections()
	 */
	@Override
	public List<Section_impl> getSections() {
		if (this.listOfSections == null) this.fixSections();
		return this.listOfSections;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getSectionById(java.lang
	 * .String)
	 */
	@Override
	public Section getSectionById(final String id) {
		return this.mapOfSections.get(id);
	}

	/**
	 * Adds a Frame to the list of textual ordered Frame elements.
	 * 
	 * @param frame
	 *            frame to be added.
	 */
	public synchronized void addFrame(final Frame_impl frame) {
		this.mapOfFrames.put(frame.getId(), frame);
		frame.setRitualDocument(this);
	}

	public void setTemporalOrderingOfFrames(final List<Frame_impl> frameList) {
		for (Frame_impl f : frameList) {
			this.frames_temporalOrdering.add(f);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getFrames()
	 */
	@Override
	public List<Frame_impl> getFrames() {
		if (this.listOfFrames == null) this.fixFrames();
		return this.listOfFrames;
	}

	/**
	 * Inserts a Frame into list of Frames in their temporal ordering at
	 * specified index.
	 * 
	 * @param frame
	 *            Frame to be added.
	 * @param index
	 *            Index
	 */
	public void addFrameToTemporalOrdering(final Frame_impl frame,
			final int index) {
		this.frames_temporalOrdering.add(index, frame);
		this.mapOfFrames.put(frame.getId(), frame);
	}

	/**
	 * Inserts a Frame into list of Frames in their temporal ordering at the
	 * end.
	 * 
	 * @param frame
	 *            Frame to be added.
	 */
	public void addFrameToTemporalOrdering(final Frame_impl frame) {
		this.frames_temporalOrdering.add(frame);
		this.mapOfFrames.put(frame.getId(), frame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getFramesInTemporalOrdering
	 * ()
	 */
	@Override
	@Deprecated
	public List<Frame_impl> getFramesInTemporalOrdering() {
		return this.frames_temporalOrdering;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getFrameById(java.lang.String
	 * )
	 */
	@Override
	public Frame_impl getFrameById(final String id) {
		return this.mapOfFrames.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getOriginalText()
	 */
	@Override
	public String getOriginalText() {
		return new String(originalText);
	}

	/**
	 * @param originalText
	 *            the originalText to set
	 */
	public void setOriginalText(final String originalText) {
		this.originalText = originalText.toCharArray();
	}

	public void addAllEntities(final Collection<Entity_impl> entities) {
		for (Entity_impl e : entities) {
			this.mapOfEntities.put(e.getId(), e);
		}

	}

	public void addAllSenses(final Collection<Sense_impl> senses) {
		for (Sense_impl s : senses) {
			this.mapOfSenses.put(s.getId(), s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.RitualDocument#getMantras()
	 */
	@Override
	public List<Mantra_impl> getMantras() {
		if (this.listOfMantras == null) this.fixMantras();
		return this.listOfMantras;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.RitualDocument#getById(java.lang.String)
	 */
	@Override
	public HasId getById(final String id) {
		HasId r = null;
		r = this.getFrameById(id);
		if (r == null) r = this.getTokenById(id);
		if (r == null) r = this.getEventById(id);

		return r;
	}

	@Override
	public Event getEventById(String id) {
		for (Event ev : this.getEvents()) {
			if (ev.getId().equalsIgnoreCase(id)) return ev;
		}
		return null;
	}

	@Override
	public int compareTo(final Document_impl arg0) {
		return this.getId().compareTo(arg0.getId());
	}

	@Override
	public String getTitle() {
		return this.documentTitle;
	}

	@Override
	public void setTitle(final String title) {
		this.documentTitle = title;
	}

	@Override
	public List<? extends Event> getEvents() {
		return this.listOfEvents;
	}

	@Override
	public void addEvent(Event ev) {
		this.listOfEvents.add(ev);
	}

	/**
	 * TODO: This needs to be extended to cover other types
	 */
	@Override
	public List<AnnotationObjectInDocument> getAnnotations(Class<?> clazz) {
		List<AnnotationObjectInDocument> ret =
				new LinkedList<AnnotationObjectInDocument>();
		if (clazz.equals(Token.class)) ret.addAll(this.getTokens());
		if (clazz.equals(Frame.class)) ret.addAll(this.getFrames());
		return ret;
	}

	@Override
	public String getCorpusName() {
		return corpusName;
	}

	@Override
	public void setCorpusName(String corpusName) {
		this.corpusName = corpusName;
	}
}
