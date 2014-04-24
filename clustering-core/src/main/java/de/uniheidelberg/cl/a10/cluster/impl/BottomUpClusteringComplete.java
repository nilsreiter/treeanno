package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Collection;

import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class BottomUpClusteringComplete<D> extends BottomUpClusteringLinkage<D> {

	@Override
	protected Probability clusterSimilarity(
			final Matrix<D, D, Probability> similarityMatrix,
			final Collection<D> cluster1, final Collection<D> cluster2) {
		Probability minsim = Probability.ONE;
		for (D d1 : cluster1) {
			for (D d2 : cluster2) {
				Probability p = similarityMatrix.get(d1, d2);
				if (p.compareTo(minsim) < 0) {
					minsim = p;
				}
			}
		}
		return minsim;
	}

}
