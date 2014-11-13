package de.nilsreiter.alignment.algorithm;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface WeightedBaseline<T extends HasDocument> extends Baseline<T> {
	void setSimilarityFunction(SimilarityFunction<T> sfun);
}
