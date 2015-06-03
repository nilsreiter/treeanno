package de.uniheidelberg.cl.a10.data2;

import java.util.List;
import java.util.Map;

import de.uniheidelberg.cl.a10.HasTarget;

public interface Event extends AnnotationObjectInDocument, HasTokens, HasTarget {
	Map<String, List<? extends HasTokens>> getArguments();

	void setArguments(Map<String, List<? extends HasTokens>> arguments);

	void putArgument(String key, List<? extends HasTokens> args);

	@Deprecated
	AnnotationObjectInDocument getAnchor();

	String getEventClass();
}