package de.nilsreiter.alignment.algorithm.impl;

import de.nilsreiter.alignment.algorithm.SameSurface;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.HasTokens;

public class SameSurface_impl<T extends HasTokens & HasDocument> extends
		SameProperty_impl<T> implements SameSurface<T> {

	@Override
	public String getProperty(final T t) {
		return t.firstToken().getSurface();
	}

}
