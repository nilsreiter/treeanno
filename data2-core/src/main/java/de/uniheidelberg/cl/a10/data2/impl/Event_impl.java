package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Token;

public class Event_impl extends HasTokens_impl implements Event {

	@Deprecated
	HasTokens anchor;

	int indexOf;

	Map<String, List<? extends HasTokens>> arguments =
			new HashMap<String, List<? extends HasTokens>>();;

	String eventClass;

			Token target;

	@Deprecated
	public Event_impl(String id, HasTokens anc) {
		super(id);
		anchor = anc;
	}

	public Event_impl(String id) {
		super(id);
	}

	public Event_impl(String id, Token... tok) {
		super(id);
		for (Token token : tok) {
			this.add(token);
		}
		this.target = this.firstToken();
	}

			@Override
	public int indexOf() {
				return indexOf;
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		HashSet<AnnotationObjectInDocument> related =
				new HashSet<AnnotationObjectInDocument>();
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

	public void setIndexOf(int indexOf) {
		this.indexOf = indexOf;
	}

	@Override
	public Token getTarget() {
		return target;
	}

	public void setTarget(Token target) {
		this.target = target;
	}

}