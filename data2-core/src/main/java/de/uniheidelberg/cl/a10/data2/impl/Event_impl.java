package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;

public class Event_impl extends AnnotationObjectInDocument_impl implements
		Event {

	AnnotationObjectInDocument anchor;

	Map<String, List<? extends AnnotationObjectInDocument>> arguments = new HashMap<String, List<? extends AnnotationObjectInDocument>>();;

	String eventClass;

	public Event_impl(String id, AnnotationObjectInDocument anc) {
		super(id);
		anchor = anc;
	}

	@Override
	public int indexOf() {
		return anchor.indexOf();
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		return null;
	}

	@Override
	public Map<String, List<? extends AnnotationObjectInDocument>> getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(
			Map<String, List<? extends AnnotationObjectInDocument>> arguments) {
		this.arguments = arguments;
	}

	@Override
	public void putArgument(String key,
			List<? extends AnnotationObjectInDocument> args) {
		this.arguments.put(key, args);
	}

	@Override
	public AnnotationObjectInDocument getAnchor() {
		return anchor;
	}

	@Override
	public String getEventClass() {
		return eventClass;
	}

	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}
}