package de.uniheidelberg.cl.a10.data2;

import java.util.Set;

import de.uniheidelberg.cl.a10.HasGlobalId;

public interface AnnotationObjectInDocument extends AnnotationObject,
		HasGlobalId, HasDocument {

	/**
	 * The document in which this object appears
	 * 
	 * @return
	 */
	@Override
	Document getRitualDocument();

	Set<AnnotationObjectInDocument> getRelated();

	@Override
	int indexOf();

	Set<AnnotationObjectInDocument> getClosure();

}