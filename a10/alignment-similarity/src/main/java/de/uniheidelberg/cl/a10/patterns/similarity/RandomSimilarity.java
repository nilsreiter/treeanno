package de.uniheidelberg.cl.a10.patterns.similarity;

import java.util.Random;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class RandomSimilarity<T> extends AbstractSimilarityFunction<T> {

	Random random = null;

	public RandomSimilarity() {
		random = new Random();
	}

	@Override
	public Probability sim(final T arg0, final T arg1) {

		return Probability.fromProbability(random.nextDouble());
	}

}
