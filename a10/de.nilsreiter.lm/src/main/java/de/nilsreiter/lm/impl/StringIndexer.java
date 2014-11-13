package de.nilsreiter.lm.impl;

import de.nilsreiter.lm.Indexer;

public class StringIndexer implements Indexer<String> {

	@Override
	public String getDummySymbol() {
		return null;
	}

}
