package de.uniheidelberg.cl.a10.data2;

import java.util.Collection;

public interface Mention extends AnnotationObjectInDocument, HasTokens {

	Collection<? extends FrameElement> getFrameElms();

	Entity getEntity();

	String getSurface();

	/**
	 * @return the confidence
	 */
	double getConfidence();

	String getMentionString();

}