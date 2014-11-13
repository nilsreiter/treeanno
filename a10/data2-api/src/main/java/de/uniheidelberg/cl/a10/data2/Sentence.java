package de.uniheidelberg.cl.a10.data2;

import java.util.Collection;

public interface Sentence extends AnnotationObjectInDocument, HasTokens {

	Section getSection();

	/**
	 * @return the chunks
	 */
	Collection<Chunk> getChunks();

	Token getRoot();

}