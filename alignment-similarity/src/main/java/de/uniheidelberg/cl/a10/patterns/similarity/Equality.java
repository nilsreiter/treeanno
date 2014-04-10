package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class Equality<T> extends AbstractSimilarityFunction<T> {

	@Override
	public Probability sim(final T arg0, final T arg1) {
		if (arg0 == null || arg1 == null)
			return Probability.NULL;
		if (arg0 == arg1)
			return Probability.ONE;
		if (arg0.equals(arg1))
			return Probability.ONE;
		return Probability.NULL;
	}

}
