package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * This similarity measure always returns zero, used for debugging and testing.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class Null<T> implements SimilarityFunction<T> {

	@Override
	public Probability sim(final T arg0, final T arg1) {
		return Probability.NULL;
	}

	@Override
	public void readConfiguration(final Object tc) {}

}
