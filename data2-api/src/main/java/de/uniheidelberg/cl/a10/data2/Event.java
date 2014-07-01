package de.uniheidelberg.cl.a10.data2;

import java.util.List;
import java.util.Map;

public interface Event extends AnnotationObjectInDocument, HasTokens {
	Map<String, List<? extends AnnotationObjectInDocument>> getArguments();

	void setArguments(
			Map<String, List<? extends AnnotationObjectInDocument>> arguments);

	void putArgument(String key, List<? extends AnnotationObjectInDocument> args);

	AnnotationObjectInDocument getAnchor();

	String getEventClass();
}