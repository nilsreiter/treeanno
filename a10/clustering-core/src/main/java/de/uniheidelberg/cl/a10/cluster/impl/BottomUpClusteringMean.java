package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.similarity.Operation;

public class BottomUpClusteringMean<D> extends BottomUpClusteringLinkage<D> {

	Operation combinationOperation = Operation.AVG;

	@Override
	protected Probability clusterSimilarity(
			final Matrix<D, D, Probability> similarityMatrix,
			final Collection<D> cluster1, final Collection<D> cluster2) {
		List<Probability> list = new LinkedList<Probability>();
		for (D element1 : cluster1) {
			for (D element2 : cluster2) {
				if (element1 != element2) {
					list.add(similarityMatrix.get(element1, element2));
				}
			}
		}
		switch (combinationOperation) {
		case GEO:
			return PMath.geometricMean(list);
		case HARM:
			return PMath.harmonicMean(list);
		default:
			return PMath.arithmeticMean(list);
		}
	}

	/**
	 * @return the combinationOperation
	 */
	public Operation getCombinationOperation() {
		return combinationOperation;
	}

	/**
	 * @param combinationOperation
	 *            the combinationOperation to set
	 */
	public void setCombinationOperation(final Operation combinationOperation) {
		this.combinationOperation = combinationOperation;
	}
}
