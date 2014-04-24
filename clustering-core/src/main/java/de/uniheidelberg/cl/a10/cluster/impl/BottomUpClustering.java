package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.CountingMapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.IterableMatrix;

/**
 * This algorithm naively puts in each step the two documents in a cluster that
 * have the highest similarity.
 * 
 * @author reiter
 * 
 * @param <D>
 */
public class BottomUpClustering<D> extends
		AbstractClusteringWithExitCondition<D> {

	class Partition implements IFullPartition<D> {

		Collection<D> objects = new HashSet<D>();

		final ArrayList<ICluster<D>> set = new ArrayList<ICluster<D>>();

		public Partition(final Partition partition) {
			objects.addAll(partition.objects);
			for (ICluster<D> cl : partition.set) {
				set.add(new Cluster_impl<D>(cl));
			}
		}

		public Partition(final Collection<D> items) {
			objects.addAll(items);
			this.createSubsets(items);
		}

		private void createSubsets(final Collection<D> items) {
			for (D item : items) {
				Cluster_impl<D> subset = new Cluster_impl<D>();
				subset.add(item);
				set.add(subset);
			}
		}

		public void merge(final int setA, final int setB) {
			if (setA != setB) {
				set.get(setA).addAll(set.get(setB));
				set.remove(setB);
			}
		}

		public void merge(final ICluster<D> setA, final ICluster<D> setB) {
			if (setA != setB) {
				setA.addAll(setB);
				set.remove(setB);
			}
		}

		public int find(final D searchfor) {
			for (int i = 0; i < set.size(); i++) {
				if (set.get(i).contains(searchfor)) {
					return i;
				}
			}
			return -1;
		}

		@Override
		public Collection<D> getObjects() {
			return objects;
		}

		@Override
		public Collection<ICluster<D>> getClusters() {
			return set;
		}

		public synchronized void merge(final D setA, final D setB) {
			merge(find(setA), find(setB));
		}

		@Override
		public int size() {
			return set.size();
		}

		@Override
		public String toString() {
			return set.toString();
		}

		@Override
		public ICluster<D> getCluster(final D object) {
			return set.get(find(object));
		}

		@Override
		public boolean together(final D o1, final D o2) {
			return (find(o1) == find(o2) && find(o1) != -1);
		}
	}

	Probability threshold = Probability.NULL;

	List<Pair<D, D>> mergedPairs = null;
	List<Partition> history = null;

	@Override
	public List<? extends IFullPartition<D>> internalCluster(
			final Collection<D> documents) {
		IterableMatrix<D, D, Probability> similarityMatrix = this
				.getSimilarityMatrix(documents);

		Iterator<Pair<D, D>> iterator = similarityMatrix.getNavigableSet()
				.descendingIterator();
		mergedPairs = new LinkedList<Pair<D, D>>();
		// int steps = 0;
		history = new ArrayList<Partition>();
		history.add(new Partition(documents));
		while (iterator.hasNext() && !this.exitCondition.matches(history)) {
			Pair<D, D> pair = iterator.next();

			if (similarityMatrix.get(pair.getKey(), pair.getValue()).compareTo(
					threshold) > 0) {
				Partition p = new Partition(history.get(0));
				p.merge(pair.getKey(), pair.getValue());
				history.add(0, p);
				mergedPairs.add(pair);
			}

			// steps++;

		}

		return history;
	}

	@Override
	public IFullPartition<D> cluster(final Collection<D> documents) {
		return this.internalCluster(documents).get(0);
	}

	/**
	 * @return the threshold
	 */
	public Probability getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(final Probability threshold) {
		this.threshold = threshold;
	}

	protected IterableMatrix<D, D, Probability> getSimilarityMatrix(
			final Collection<D> documents) {
		IterableMatrix<D, D, Probability> similarityMatrix = new CountingMapMatrix<D, D, Probability>(
				Probability.NULL);

		for (D rd0 : documents) {
			for (D rd1 : documents) {
				if (!rd0.equals(rd1)) {
					if (this.getDocumentSimilarityFunction() == null
							|| this.getDocumentSimilarityFunction()
									.getSimilarity(rd0, rd1) == null)
						similarityMatrix.put(rd0, rd1, Probability.NULL);
					else
						similarityMatrix.put(rd0, rd1,
								this.getDocumentSimilarityFunction()
										.getSimilarity(rd0, rd1));
				}
			}
		}
		return similarityMatrix;
	}

	@Override
	public List<? extends IPartition<D>> getPartitionHistory() {
		return history;
	}
}
