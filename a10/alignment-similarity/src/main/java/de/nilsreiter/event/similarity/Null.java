package de.nilsreiter.event.similarity;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class Null implements EventSimilarityFunction {

	@Override
	public Probability sim(Event arg0, Event arg1) {
		return Probability.NULL;
	}

	@Override
	public void readConfiguration(Object tc) {
		// intentionally empty
	}
}