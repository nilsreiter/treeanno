package de.nilsreiter.alignment.algorithm.impl;

import de.nilsreiter.alignment.algorithm.AlignmentAlgorithm;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public abstract class AbstractAlignmentAlgorithm_impl<D> implements
AlignmentAlgorithm<D> {
	SimilarityFunction<D> function;

	public SimilarityFunction<D> getSimilarityFunction() {
		return function;
	}
}
