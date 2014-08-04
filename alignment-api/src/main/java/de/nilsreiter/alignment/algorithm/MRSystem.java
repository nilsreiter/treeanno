package de.nilsreiter.alignment.algorithm;

import java.util.logging.Logger;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityCalculationException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface MRSystem<T> extends AlignmentAlgorithm<T> {

	public static final String CONFIG_THRESHOLD = "Threshold";

	/**
	 * Calculate the alignment
	 * 
	 * @param sequence1
	 * @throws IncompatibleException
	 */
	Alignment<T> getAlignment() throws SimilarityCalculationException;

	SimilarityFunction<T> getSimilarityFunction();

	Probability getThreshold();

	void setLogger(Logger logger);

	Logger getLogger();

}
