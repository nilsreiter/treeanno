package de.nilsreiter.alignment.algorithm;

import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface BayesianModelMerging<T> extends AlignmentAlgorithm<T> {
	public static final String CONFIG_THRESHOLD = "Threshold";
	public static final String CONFIG_THREADED = "Threaded";

	SimilarityFunction<T> getSimilarityFunction();

}
