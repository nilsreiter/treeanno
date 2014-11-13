package de.nilsreiter.lm.impl;

import de.nilsreiter.lm.Indexer;

public class CharIndexer implements Indexer<Character> {

	@Override
	public Character getDummySymbol() {
		return '@';
	}

}
