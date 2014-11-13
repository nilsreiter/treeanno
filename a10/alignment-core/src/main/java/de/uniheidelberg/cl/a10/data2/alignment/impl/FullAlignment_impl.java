package de.uniheidelberg.cl.a10.data2.alignment.impl;

import java.util.Collection;
import java.util.HashSet;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.FullAlignment;

public class FullAlignment_impl<T extends HasDocument> extends
		Alignment_impl<T> implements FullAlignment<T> {

	public FullAlignment_impl(final String id) {
		super(id);
	}

	@Override
	public void addSingleton(final String id, final T object) {
		if (!graph.containsVertex(object)) {
			HashSet<T> set = new HashSet<T>();
			set.add(object);
			this.addAlignment(id, set);
		}
	}

	@Override
	public void addSingletons(final AlignmentIdProvider idp,
			final Collection<T> objects) {
		for (T obj : objects) {
			this.addSingleton(idp.getNextAlignmentId(), obj);
		}
	}

}
