package de.uniheidelberg.cl.a10.data2;

import java.util.Collection;
import java.util.List;

import de.uniheidelberg.cl.a10.HasGlobalId;

public interface Token extends AnnotationObjectInDocument, Comparable<Token>,
		HasOldId, HasGlobalId {

	/**
	 * The sense
	 * 
	 * @return
	 */
	Sense getSense();

	String getLemma();

	Token getGovernor();

	List<? extends Frame> getFrames();

	Collection<? extends FrameElement> getFrameElms();

	Sentence getSentence();

	/**
	 * @return the dependencyRelation
	 */
	String getDependencyRelation();

	Collection<? extends Token> getDependents();

	/**
	 * @return the partOfSpeech
	 */
	String getPartOfSpeech();

	/**
	 * @return the surface
	 */
	String getSurface();

	Frame getDependencyFrame();

	/**
	 * @return the begin
	 */
	int getBegin();

	/**
	 * @return the end
	 */
	int getEnd();

	Collection<? extends Mention> getMentions();

	/**
	 * Returns true, if the token represents a word, i.e., not a punctuation
	 * symbol.
	 * 
	 * @return
	 */
	boolean isWord();

	void setId(String id);

}