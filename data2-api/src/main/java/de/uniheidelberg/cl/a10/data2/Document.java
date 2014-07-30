package de.uniheidelberg.cl.a10.data2;

import java.util.Collection;
import java.util.List;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.HasTitle;

public interface Document extends AnnotationObject, HasId, HasTitle {

	HasId getById(String id);

	Chunk getChunkById(String id);

	Collection<? extends Chunk> getChunks();

	Collection<? extends Entity> getEntities();

	Entity getEntityById(String id);

	Frame getFrameById(String id);

	FrameElement getFrameElmById(String id);

	Collection<? extends FrameElement> getFrameElms();

	/**
	 * 
	 * @return List of Frames in their textual ordering.
	 */
	List<? extends Frame> getFrames();

	/**
	 * 
	 * @return List of Frames in their temporal ordering.
	 */
	@Deprecated
	List<? extends Frame> getFramesInTemporalOrdering();

	List<? extends Mantra> getMantras();

	Mention getMentionById(String id);

	Collection<? extends Mention> getMentions();

	/**
	 * @return the originalText
	 */
	String getOriginalText();

	Section getSectionById(String id);

	List<? extends Section> getSections();

	Sense getSenseById(String id);

	Collection<? extends Sense> getSenses();

	Sentence getSentenceById(String id);

	List<? extends Sentence> getSentences();

	Token getTokenById(String id);

	List<Token> getTokens();

	String getTitle();

	void setTitle(String title);

	List<? extends Event> getEvents();

	List<AnnotationObjectInDocument> getAnnotations(Class<?> clazz);

	void addEvent(Event ev);

	Event getEventById(String id);

	String getCorpusName();

	void setCorpusName(String corpusName);

}