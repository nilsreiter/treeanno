package de.nilsreiter.alignment.algorithm.impl;

import de.nilsreiter.alignment.algorithm.AlgorithmConfiguration;
import de.nilsreiter.alignment.algorithm.MRSystem;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class MRSystemConfiguration extends SimilarityConfiguration implements
AlgorithmConfiguration {

	@Override
	public Class<?> getAlgorithmClass() {
		return MRSystem.class;
	}

}
