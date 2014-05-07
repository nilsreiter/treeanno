package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.cluster.WordOverlap;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class WordOverlap_impl implements WordOverlap {

	@Override
	public Probability getSimilarity(final Document rd1, final Document rd2) {
		Set<String> lemmas1 = new HashSet<String>();
		Set<String> lemmas2 = new HashSet<String>();

		for (Token token : rd1.getTokens()) {
			lemmas1.add(token.getLemma());
		}

		for (Token token : rd2.getTokens()) {
			lemmas2.add(token.getLemma());
		}

		Set<String> overlap = new HashSet<String>();
		overlap.addAll(lemmas1);
		overlap.retainAll(lemmas2);

		return Probability.fromProbability(2 * overlap.size()
				/ ((double) lemmas1.size() + (double) lemmas2.size()));
	}

}
