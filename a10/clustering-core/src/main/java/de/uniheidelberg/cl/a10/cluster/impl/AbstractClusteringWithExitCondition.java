package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Map;

import de.uniheidelberg.cl.a10.cluster.ECModularityNotImproving;
import de.uniheidelberg.cl.a10.cluster.ECNumberOfClusters;
import de.uniheidelberg.cl.a10.cluster.IExitCondition;
import de.uniheidelberg.cl.a10.cluster.Modularity;

public abstract class AbstractClusteringWithExitCondition<D> extends
		AbstractClustering<D> {

	public static String OPTION_EXIT = "exit";
	public static String OPTION_NUMBEROFCLUSTERS = "number";

	IExitCondition<D> exitCondition = null;

	public AbstractClusteringWithExitCondition() {
		super();
		options.put(OPTION_EXIT, "NumberOfClusters");
		options.put(OPTION_NUMBEROFCLUSTERS, "1");
	}

	@Override
	public void parseOptionMap(final Map<String, String> map) {
		options.putAll(map);
		if (options.get(OPTION_EXIT).equalsIgnoreCase("NumberOfClusters")) {
			this.exitCondition = new ECNumberOfClusters<D>(
					Integer.parseInt(options.get(OPTION_NUMBEROFCLUSTERS)));
		} else if (options.get(OPTION_EXIT).equalsIgnoreCase("Modularity")) {
			this.exitCondition = new ECModularityNotImproving<D>(
					new Modularity<D>(getDocumentSimilarityFunction()));
		}
	}

	/**
	 * @return the exitCondition
	 */
	public IExitCondition<D> getExitCondition() {
		return exitCondition;
	}

	/**
	 * @param exitCondition
	 *            the exitCondition to set
	 */
	public void setExitCondition(final IExitCondition<D> exitCondition) {
		this.exitCondition = exitCondition;
	};

}
