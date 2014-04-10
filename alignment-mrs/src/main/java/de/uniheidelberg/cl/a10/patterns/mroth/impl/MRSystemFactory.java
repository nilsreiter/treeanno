package de.uniheidelberg.cl.a10.patterns.mroth.impl;

import java.io.FileNotFoundException;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.mroth.MRSystem;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;

public class MRSystemFactory {

	public static <T extends Event> MRSystem<T> getInstance(
			final SimilarityConfiguration sConf) throws FileNotFoundException,
			SecurityException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		MRSystem_impl<T> mrs = new MRSystem_impl<T>();
		SimilarityFunctionFactory<T> sfunfact = new SimilarityFunctionFactory<T>();
		mrs.setSimilarityFunction(sfunfact.getSimilarityFunction(sConf));
		mrs.setThreshold(Probability.fromProbability(sConf.getThreshold()));
		return mrs;
	}

	public static <T extends Event> MRSystem<T> getInstance(
			final SimilarityFunction<T> fun, final double threshold) {
		MRSystem_impl<T> mrs = new MRSystem_impl<T>();
		mrs.setSimilarityFunction(fun);
		mrs.setThreshold(Probability.fromProbability(threshold));
		return mrs;
	}
}
