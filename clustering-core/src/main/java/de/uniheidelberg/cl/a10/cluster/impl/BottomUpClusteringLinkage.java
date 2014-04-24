package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.IterableMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public abstract class BottomUpClusteringLinkage<D> extends
		BottomUpClustering<D> {
	@Override
	public IFullPartition<D> cluster(final Collection<D> documents) {
		IterableMatrix<D, D, Probability> similarityMatrix = this
				.getSimilarityMatrix(documents);

		// int steps = 0;
		mergedPairs = new LinkedList<Pair<D, D>>();
		history = new ArrayList<Partition>();
		history.add(new Partition(documents));

		while (!this.exitCondition.matches(history)) {
			Probability maxsim = Probability.NULL;
			Pair<ICluster<D>, ICluster<D>> maxpair = null;
			Partition currentPartition = history.get(0);
			Partition newPartition = new Partition(currentPartition);
			for (ICluster<D> cl1 : newPartition.getClusters()) {
				for (ICluster<D> cl2 : newPartition.getClusters()) {
					if (cl1 != cl2) {
						Probability p = this.clusterSimilarity(
								similarityMatrix, cl1, cl2);
						if (p.compareTo(maxsim) >= 0) {
							maxsim = p;
							maxpair = new Pair<ICluster<D>, ICluster<D>>(cl1,
									cl2);
						}
					}
				}
			}
			if (maxpair != null) {
				newPartition.merge(maxpair.getKey(), maxpair.getValue());
				history.add(0, newPartition);
			}
		}

		return history.get(0);
	}

	protected abstract Probability clusterSimilarity(
			Matrix<D, D, Probability> similarityMatrix, Collection<D> cluster1,
			Collection<D> cluster2);

}
