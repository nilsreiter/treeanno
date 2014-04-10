package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Mantra;
import de.uniheidelberg.cl.a10.data2.Token;

public class Mantra_impl extends HasTokens_impl implements Mantra {

	public Mantra_impl(final String id, final Token token) {
		super(id);
		this.tokens.add(token);
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		return new HashSet<AnnotationObjectInDocument>();
	}

}
