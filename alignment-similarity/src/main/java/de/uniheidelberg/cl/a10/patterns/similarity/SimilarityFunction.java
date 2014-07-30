package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

public interface SimilarityFunction<T> {
	Probability sim(T arg0, T arg1) throws SimilarityCalculationException;

	void readConfiguration(Object tc);

}
