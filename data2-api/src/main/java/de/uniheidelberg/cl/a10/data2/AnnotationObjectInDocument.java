package de.uniheidelberg.cl.a10.data2;

import java.util.Set;

import de.uniheidelberg.cl.a10.HasGlobalId;

public interface AnnotationObjectInDocument extends AnnotationObject, HasGlobalId {

	/**
	 * The document in which this object appears
	 * 
	 * @return
	 */
	Document getRitualDocument();

	Set<AnnotationObjectInDocument> getRelated();

	int indexOf();

	Set<AnnotationObjectInDocument> getClosure();
}