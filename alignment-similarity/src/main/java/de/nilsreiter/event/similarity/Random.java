package de.nilsreiter.event.similarity;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class Random<T extends Event> implements SimilarityFunction<T> {

	java.util.Random random = null;

	public Random() {
		random = new java.util.Random();
	}

	@Override
	public Probability sim(final T arg0, final T arg1)
			throws IncompatibleException {
		return Probability.fromProbability(random.nextDouble());
	}

	@Override
	public void readConfiguration(SimilarityConfiguration tc) {
	}

}
