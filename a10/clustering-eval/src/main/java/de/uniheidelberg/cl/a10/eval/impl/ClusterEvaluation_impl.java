package de.uniheidelberg.cl.a10.eval.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluation;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluationStyle;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import edu.cuny.qc.speech.ClusterEvaluator;
import edu.cuny.qc.speech.ContingencyTable;

public class ClusterEvaluation_impl<T> implements ClusterEvaluation<T> {

	ClusterEvaluationStyle style;

	public ClusterEvaluation_impl(ClusterEvaluationStyle style) {
		super();
		this.style = style;
	}

	protected ContingencyTable getContingencyTable(IPartition<T> gold,
			IPartition<T> silver) {
		Double[][] arr = new Double[gold.getClusters().size()][silver
				.getClusters().size()];
		List<ICluster<T>> goldClusters = new LinkedList<ICluster<T>>(
				gold.getClusters());
		List<ICluster<T>> silverClusters = new LinkedList<ICluster<T>>(
				silver.getClusters());
		for (int i = 0; i < goldClusters.size(); i++) {
			for (int j = 0; j < silverClusters.size(); j++) {
				Set<T> goldCl = goldClusters.get(i);
				Set<T> silverCl = silverClusters.get(j);
				arr[i][j] = cut(goldCl, silverCl);
			}
		}
		return new ContingencyTable(arr);
	}

	protected double cut(Set<?> s1, Set<?> s2) {
		double d = 0.0;
		for (Object o1 : s1) {
			if (s2.contains(o1))
				d++;
		}
		return d;
	}

	@Override
	public double evaluate(IPartition<T> gold, IPartition<T> silver) {
		ClusterEvaluator ce = new ClusterEvaluator();
		ce.setData(getContingencyTable(gold, silver));

		switch (style) {
		case AdjustedRand2:
			return ce.getAdjustedRandIndex();
		case NVI:
			return ce.getNVI();
		case VI:
			return ce.getVI();
		case Rand2:
		default:
			return ce.getRandIndex();

		}
	}

	@Override
	public SingleResult evaluate(IPartition<T> gold, IPartition<T> silver,
			Object name) {

		SingleResult sr = new SingleResult_impl();
		sr.setScore(style.toString(), this.evaluate(gold, silver));

		return sr;
	}

	@Override
	public void setRestriction(String id) {
		// TODO Auto-generated method stub

	}
}