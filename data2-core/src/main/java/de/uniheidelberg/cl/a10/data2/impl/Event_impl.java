package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Token;

public class Event_impl extends AnnotationObjectInDocument_impl implements
		Event {

	HasTokens anchor;

	Map<String, List<? extends HasTokens>> arguments = new HashMap<String, List<? extends HasTokens>>();;

	String eventClass;

	public Event_impl(String id, HasTokens anc) {
		super(id);
		anchor = anc;
	}

	@Override
	public int indexOf() {
		return anchor.indexOf();
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		HashSet<AnnotationObjectInDocument> related = new HashSet<AnnotationObjectInDocument>();
		related.add(getAnchor());
		for (List<? extends HasTokens> ht : arguments.values()) {
			related.add((AnnotationObjectInDocument) ht);
		}
		return related;
	}

	@Override
	public Map<String, List<? extends HasTokens>> getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(Map<String, List<? extends HasTokens>> arguments) {
		this.arguments = arguments;
	}

	@Override
	public void putArgument(String key, List<? extends HasTokens> args) {
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

	@Override
	public List<Token> getTokens() {
		return anchor.getTokens();
	}

	@Override
	public Token lastToken() {
		return anchor.lastToken();
	}

	@Override
	public Token firstToken() {
		return anchor.firstToken();
	}

	@Override
	public List<Token> getTokensBetween(int end, int begin) {
		return anchor.getTokensBetween(end, begin);
	}

	@Override
	public int numberOfTokens() {
		return anchor.numberOfTokens();
	}

	@Override
	public Iterator<Token> iterator() {
		return anchor.iterator();
	}

}