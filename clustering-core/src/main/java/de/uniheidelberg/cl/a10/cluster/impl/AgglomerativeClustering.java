package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniheidelberg.cl.a10.ObjectFactory;
import de.uniheidelberg.cl.a10.cluster.IExitCondition;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.Modularity;

@Deprecated
public class AgglomerativeClustering<D> extends AbstractClustering<D> {

	public static String OPTION_SELECTOR = "selector";
	public static String OPTION_BASEALGORITHM = "base";

	public AgglomerativeClustering() {
		super();
		options.put(OPTION_SELECTOR, "Modularity");
		options.put(OPTION_BASEALGORITHM,
				"de.uniheidelberg.cl.a10.cluster.BottomUpClusteringMean");
	}

	protected AbstractClustering<D> initClustering()
			throws ClassNotFoundException {

		if (!options.get(OPTION_BASEALGORITHM).startsWith("de.uniheidelberg.")) {
			options.put(
					OPTION_BASEALGORITHM,
					"de.uniheidelberg.cl.a10.cluster."
							+ options.get(OPTION_BASEALGORITHM));
		}

		ObjectFactory<AbstractClustering<D>> of = new ObjectFactory<AbstractClustering<D>>();

		AbstractClustering<D> baseClustering = of.getNewObject(options
				.get(OPTION_BASEALGORITHM));
		baseClustering
				.setDocumentSimilarityFunction(getDocumentSimilarityFunction());
		if (baseClustering instanceof AbstractClusteringWithExitCondition) {
			((AbstractClusteringWithExitCondition<D>) baseClustering)
					.setExitCondition(new IExitCondition<D>() {

						@Override
						public boolean matches(
								final List<? extends IPartition<D>> history) {
							return false;
						}
					});
		}
		return baseClustering;

	}

	@Override
	public IFullPartition<D> cluster(final Collection<D> documents) {
		IFullPartition<D> bestPartition = null;

		if (!options.get(OPTION_BASEALGORITHM).startsWith("de.uniheidelberg.")) {
			options.put(
					OPTION_BASEALGORITHM,
					"de.uniheidelberg.cl.a10.cluster."
							+ options.get(OPTION_BASEALGORITHM));
		}

		try {
			AbstractClustering<D> baseClustering = this.initClustering();
			List<? extends IFullPartition<D>> result = baseClustering
					.internalCluster(documents);
			double highestModularity = 0.0;
			Modularity<D> modularity = new Modularity<D>(
					this.getDocumentSimilarityFunction());
			for (IFullPartition<D> partition : result) {
				double mod = modularity.getModularity(partition);
				// System.err.println(mod + " " + partition);
				if (mod > highestModularity) {
					bestPartition = partition;
					highestModularity = mod;
				}
			}
		} catch (ClassNotFoundException e) {
			// This should not happen
			e.printStackTrace();
		}

		return bestPartition;
	}

	@Override
	public void parseOptionMap(final Map<String, String> map) {
		options.putAll(map);
	}

	@Override
	public List<? extends IFullPartition<D>> internalCluster(
			final Collection<D> documents) {
		try {
			return this.initClustering().internalCluster(documents);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<? extends IPartition<D>> getPartitionHistory() {
		// TODO Auto-generated method stub
		return null;
	}

}
