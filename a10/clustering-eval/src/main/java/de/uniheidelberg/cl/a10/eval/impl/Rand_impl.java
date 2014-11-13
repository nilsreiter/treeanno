package de.uniheidelberg.cl.a10.eval.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.util.ArithmeticUtils;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.eval.Rand;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

@Deprecated
public class Rand_impl<T> implements Rand<T> {

	String rId = null;

	protected boolean isAligned(final IPartition<T> partition, final T e1,
			final T e2) {
		if (partition.getCluster(e1) == partition.getCluster(e2)
				&& partition.getCluster(e1) != null)
			return true;
		return false;
	}

	@Override
	public double evaluate(final IPartition<T> gold, final IPartition<T> silver) {
		Set<T> objects = new HashSet<T>();
		boolean includeGoldObjects = false;

		if (includeGoldObjects) {
			for (ICluster<T> fa : gold.getClusters()) {
				if (rId == null || fa.getId().equals(rId))
					objects.addAll(fa);
			}
		}

		for (ICluster<T> fa : silver.getClusters()) {
			if (rId == null)
				objects.addAll(fa);
		}
		Matrix<T, T, Boolean> visited = new MapMatrix<T, T, Boolean>(false);
		// System.err.println(objects.size() + " objects.");
		int agreements = 0;
		for (T element1 : objects) {
			for (T element2 : objects) {
				if (element1 != element2 && !visited.get(element1, element2)) {
					if (isAligned(gold, element1, element2) == isAligned(
							silver, element1, element2)) {
						agreements++;
					}
					visited.put(element1, element2, true);
					visited.put(element2, element1, true);
				}

			}
		}

		long pairs = ArithmeticUtils.binomialCoefficient(objects.size(), 2);
		// System.err.println(agreements + " / " + pairs);
		double rand = (double) agreements / (double) pairs;
		return rand;
	}

	@Override
	public SingleResult evaluate(final IPartition<T> gold,
			final IPartition<T> silver, final Object name) {

		double rand = this.evaluate(gold, silver);
		SingleResult_impl r = new SingleResult_impl(name);
		r.setPrecision(rand);
		return r;
	}

	@Override
	public void setRestriction(final String id) {
		this.rId = id;
	}

}