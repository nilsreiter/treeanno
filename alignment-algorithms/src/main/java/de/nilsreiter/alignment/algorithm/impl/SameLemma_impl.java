package de.nilsreiter.alignment.algorithm.impl;

import de.nilsreiter.alignment.algorithm.SameLemma;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.HasTokens;

public class SameLemma_impl<T extends HasTokens & HasDocument> extends
SameProperty_impl<T> implements SameLemma<T> {

	@Override
	public String getProperty(final T t) {
		return t.firstToken().getLemma();
	}

}
