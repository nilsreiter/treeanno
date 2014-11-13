package de.uniheidelberg.cl.a10.cluster;

import de.uniheidelberg.cl.a10.cluster.impl.BottomUpClusteringComplete;
import de.uniheidelberg.cl.a10.cluster.impl.BottomUpClusteringMean;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class ClusteringAlgorithmFactory<D> {

	public ClusteringAlgorithm<D, Probability> getClusteringAlgorithm(
			final EAlgorithm algo) {
		switch (algo) {
		case BottomUpComplete:
			return new BottomUpClusteringComplete<D>();
		case BottomUpMean:
			return new BottomUpClusteringMean<D>();
		default:
			return null;
		}
	}
}
