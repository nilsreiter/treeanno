package de.uniheidelberg.cl.a10.patterns.mroth;

import java.util.List;
import java.util.logging.Logger;

import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface MRSystem<T extends FrameTokenEvent> {

	/**
	 * Set sequence 1
	 * 
	 * @param sequence1
	 */
	void setSequence1(List<T> sequence1);

	/**
	 * Set sequence 1
	 * 
	 * @param sequence1
	 */
	void setSequence2(List<T> sequence2);

	/**
	 * Calculate the alignment
	 * 
	 * @param sequence1
	 * @throws IncompatibleException
	 */
	Alignment<T> getAlignment() throws IncompatibleException;

	SimilarityFunction<T> getSimilarityFunction();

	Probability getThreshold();

	void setLogger(Logger logger);

	Logger getLogger();

}
