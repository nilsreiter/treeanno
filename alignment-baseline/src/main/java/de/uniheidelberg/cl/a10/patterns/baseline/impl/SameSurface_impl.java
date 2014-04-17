package de.uniheidelberg.cl.a10.patterns.baseline.impl;

import de.uniheidelberg.cl.a10.HasTarget;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.baseline.SameSurface;

public class SameSurface_impl<T extends HasTarget & HasDocument> extends
		SameProperty_impl<T> implements SameSurface<T> {

	@Override
	public String getProperty(final T t) {
		return t.getTarget().getSurface();
	}

}
