package de.uniheidelberg.cl.a10.data2;


public interface Chunk extends HasTokens, AnnotationObjectInDocument {
	String getCategory();

	Sentence getSentence();
}
