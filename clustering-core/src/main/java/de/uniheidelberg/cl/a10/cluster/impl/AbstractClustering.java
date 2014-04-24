package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.uniheidelberg.cl.a10.cluster.ClusteringAlgorithm;
import de.uniheidelberg.cl.a10.cluster.IDocumentSimilarityFunction;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public abstract class AbstractClustering<D> implements
		ClusteringAlgorithm<D, Probability> {

	Logger logger = Logger.getAnonymousLogger();

	IDocumentSimilarityFunction<D, Probability> function = null;

	Map<String, String> options = new HashMap<String, String>();

	public AbstractClustering() {

	}

	public abstract List<? extends IFullPartition<D>> internalCluster(
			Collection<D> documents);

	@Override
	public IFullPartition<D> cluster(final Collection<D> documents) {
		return this.internalCluster(documents).get(0);
	}

	@Override
	public void setDocumentSimilarityFunction(
			final IDocumentSimilarityFunction<D, Probability> function) {
		this.function = function;
	}

	/**
	 * @return the function
	 */
	@Override
	public IDocumentSimilarityFunction<D, Probability> getDocumentSimilarityFunction() {
		return function;
	}

	@Override
	public abstract void parseOptionMap(Map<String, String> map);

}
