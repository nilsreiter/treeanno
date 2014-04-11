package de.uniheidelberg.cl.a10.patterns.train;

import java.io.FileNotFoundException;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;

/**
 * This class is used to create a trainer for Bayesian Model Merging.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class BMMFactory<T extends HasDocument> {

	Prior<T> prior = null;

	SimilarityFunction<T> similarityFunction = null;

	SimilarityFunctionFactory<T> sfFactory = null;

	public BMMFactory() {
		this.sfFactory = new SimilarityFunctionFactory<T>();

	}

	public BMMFactory(final SimilarityFunctionFactory<T> factory) {
		this.sfFactory = factory;
	}

	/**
	 * Most important method. Returns the trainer object.
	 * 
	 * @param bmmc
	 *            A configuration object
	 * @return
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 *             If a similarity function is specified that does not exist.
	 */
	public BayesianModelMerging<T> getTrainer(final BMMConfiguration bmmc)
			throws FileNotFoundException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		this.getPrior(bmmc);
		BayesianModelMerging<T> bmm;
		if (bmmc.isThreaded()) {
			bmm = new ThreadedBayesianModelMerging<T>(bmmc, prior);
		} else {
			bmm = new BayesianModelMerging<T>(bmmc, prior);
		}
		return bmm;

	}

	public Prior<T> getPrior(final BMMConfiguration bmmc)
			throws FileNotFoundException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (bmmc.getThreshold() != Double.MIN_VALUE) {
			prior = new GeometricDistributionWithThreshold<T>(
					this.getSimilarityFunction(bmmc), bmmc.prior,
					bmmc.getThreshold());
		} else {
			prior = new GeometricDistribution<T>(
					this.getSimilarityFunction(bmmc), bmmc.prior);
		}
		return prior;
	}

	public SimilarityFunction<T> getSimilarityFunction(
			final SimilarityConfiguration bmmc) throws FileNotFoundException,
			SecurityException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return sfFactory.getSimilarityFunction(bmmc);
	}

	public Prior<T> getLastPrior() {
		return prior;
	}
}
