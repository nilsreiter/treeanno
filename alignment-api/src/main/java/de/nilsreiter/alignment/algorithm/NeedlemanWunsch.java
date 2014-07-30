package de.nilsreiter.alignment.algorithm;

import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface NeedlemanWunsch<T> extends AlignmentAlgorithm<T> {
	public static final String PARAM_THRESHOLD = "Threshold";

	SimilarityFunction<T> getSimilarityFunction();
}
