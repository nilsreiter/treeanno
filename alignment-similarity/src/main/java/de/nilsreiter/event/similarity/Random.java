package de.nilsreiter.event.similarity;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class Random implements EventSimilarityFunction {

	java.util.Random random = null;

	public Random() {
		random = new java.util.Random();
	}

	@Override
	public Probability sim(final Event arg0, final Event arg1) {
		return Probability.fromProbability(random.nextDouble());
	}

	@Override
	public void readConfiguration(Object tc) {}

}
