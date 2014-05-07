package de.uniheidelberg.cl.a10.cluster;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class VarianceRatioCriterion<D> implements PartitionScorer<D> {
	IDocumentSimilarityFunction<D, Probability> similarityFunction = null;

	public double getVRC(final IPartition<D> partition) {
		int n = partition.getObjects().size();
		int k = partition.getClusters().size();

		double bgss = 0.0, wgss = 0.0;

		for (D d1 : partition.getObjects()) {
			for (D d2 : partition.getObjects()) {
				double s = similarityFunction.getSimilarity(d1, d2)
						.getProbability();
				if (partition.together(d1, d2)) {
					wgss += Math.pow(s, 2.0);
				} else {
					bgss += Math.pow(s, 2.0);
				}
			}
		}

		return (bgss / (k - 1)) / (wgss / (n - k));
	}

	public IDocumentSimilarityFunction<D, Probability> getSimilarityFunction() {
		return similarityFunction;
	}

	public void setSimilarityFunction(
			final IDocumentSimilarityFunction<D, Probability> similarityFunction) {
		this.similarityFunction = similarityFunction;
	}

	@Override
	public double getScore(final IPartition<D> cluster) {
		return this.getVRC(cluster);
	}
}
