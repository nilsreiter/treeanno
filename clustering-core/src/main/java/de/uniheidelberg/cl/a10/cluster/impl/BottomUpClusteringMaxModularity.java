package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniheidelberg.cl.a10.cluster.ECNumberOfClusters;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.Modularity;

public class BottomUpClusteringMaxModularity<D> extends BottomUpClustering<D> {

	@Override
	public List<? extends IFullPartition<D>> internalCluster(
			final Collection<D> documents) {

		int steps = 0;
		List<Partition> history = new ArrayList<Partition>();
		history.add(0, new Partition(documents));

		Modularity<D> m = new Modularity<D>(function);
		ECNumberOfClusters<D> ecm = new ECNumberOfClusters<D>(1);
		while (!this.exitCondition.matches(history) && !ecm.matches(history)
				&& steps < 5) {

			double modularity = 0.0;
			Partition bestPartition = null;

			for (D doc1 : documents) {
				for (D doc2 : documents) {
					Partition part = new Partition(history.get(0));
					if (doc1 != doc2 && !part.together(doc1, doc2)) {
						part.merge(doc1, doc2);
						double mod = m.getModularity(part);
						// System.err.println("  " + mod + ": " + part);
						if (mod > modularity) {
							modularity = mod;
							bestPartition = part;
						}
					}
				}
			}
			if (bestPartition != null) {
				// System.err.println(modularity + ": " + bestPartition);
				history.add(0, bestPartition);
			}
			steps++;
		}

		return history;
	}
}
