package de.nilsreiter.alignment.algorithm.impl;

import de.nilsreiter.alignment.algorithm.AlgorithmConfiguration;
import de.nilsreiter.alignment.algorithm.NeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class NeedlemanWunschConfiguration extends SimilarityConfiguration
		implements AlgorithmConfiguration {

	@Override
	public Class<?> getAlgorithmClass() {
		return NeedlemanWunsch.class;
	}

}
