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

/**
 * This is not finalized
 * 
 * TODO: Finalize!
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class AdjustedRand_impl<T> extends Rand_impl<T> implements Rand<T> {

	Matrix<ICluster<T>, ICluster<T>, Integer> contingency = new MapMatrix<ICluster<T>, ICluster<T>, Integer>();

	protected int overlap(final ICluster<T> x, final ICluster<T> y) {
		if (contingency.get(x, y) == null) {
			Set<T> set = new HashSet<T>(x);
			set.retainAll(y);
			contingency.put(x, y, set.size());
		}
		return contingency.get(x, y);
	}

	protected double adjRand(final IPartition<T> partitionX,
			final IPartition<T> partitionY) {
		Set<T> objects = new HashSet<T>();
		boolean includeGoldObjects = false;

		if (includeGoldObjects) {
			for (ICluster<T> fa : partitionX.getClusters()) {
				if (rId == null || fa.getId().equals(rId))
					objects.addAll(fa);
			}
		}

		for (ICluster<T> fa : partitionY.getClusters()) {
			if (rId == null)
				objects.addAll(fa);
		}
		Matrix<T, T, Boolean> visited = new MapMatrix<T, T, Boolean>(false);
		System.err.println(objects.size() + " objects.");
		int a = 0, b = 0, c = 0, d = 0;
		for (T element1 : objects) {
			for (T element2 : objects) {
				if (element1 != element2 && !visited.get(element1, element2)) {
					if (partitionX.together(element1, element2)
							&& partitionY.together(element1, element2)) {
						a++;
					} else if (partitionX.together(element1, element2)
							&& !partitionY.together(element1, element2)) {
						b++;
					} else if (!partitionX.together(element1, element2)
							&& partitionY.together(element1, element2)) {
						c++;
					} else if (!partitionX.together(element1, element2)
							&& !partitionY.together(element1, element2)) {
						d++;
					}
				}
			}
		}
		int n = partitionY.getObjects().size();
		System.err.println("a = " + a);
		System.err.println("b = " + b);
		System.err.println("c = " + c);
		System.err.println("d = " + d);
		System.err.println("a+b+c+d = " + (a + b + c + d));

		double exp = ((a + b) * (a + c)) + ((c + d) * (b + d));
		double arand = (ArithmeticUtils.binomialCoefficient(n, 2) * (a + d) - exp)
				/ (Math.pow(ArithmeticUtils.binomialCoefficient(n, 2), 2) - exp);
		System.err.println("arand = " + arand);
		return arand;
	}

	@Override
	public SingleResult evaluate(final IPartition<T> gold,
			final IPartition<T> silver, final Object name) {
		double rand = this.adjRand(gold, silver);
		SingleResult_impl r = new SingleResult_impl(name);
		r.setPrecision(rand);
		return r;
	}

}
