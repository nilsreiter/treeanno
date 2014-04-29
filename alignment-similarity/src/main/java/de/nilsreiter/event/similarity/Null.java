package de.nilsreiter.event.similarity;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class Null implements SimilarityFunction<Event> {

	@Override
	public Probability sim(Event arg0, Event arg1) throws IncompatibleException {
		return Probability.NULL;
	}

	@Override
	public void readConfiguration(SimilarityConfiguration tc) {
		// intentionally empty
	}
}