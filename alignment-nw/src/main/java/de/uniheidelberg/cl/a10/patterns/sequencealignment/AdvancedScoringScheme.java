package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import de.uniheidelberg.cl.a10.Util;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * This scoring scheme uses scaling of the similarity values as described <a
 * href="https://www.cl.uni-heidelberg.de/trac/rituals/wiki/Alignment">here</a>.
 * Basically, if two elements are similar above a certain threshold
 * <code>t</code>, the similarity value is scaled to [1,2]. If the value is
 * lower than <code>t</code>, it is scaled to [-1,0].
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class AdvancedScoringScheme<T> extends BasicScoringScheme<T> {

	SimilarityFunction<T> function;

	Probability threshold = null;

	public AdvancedScoringScheme(final Probability threshold,
			final SimilarityFunction<T> similarityFunction) {
		super(1, 1, -1);
		this.threshold = threshold;
		this.function = similarityFunction;
	}

	@Override
	public double scoreSubstitution(final T a, final T b) {
		if (a == null || b == null)
			return gap_cost;
		Probability p;
		try {
			p = function.sim(a, b);
			if (p.getProbability() > threshold.getProbability()) {
				return Util.scale(threshold.getProbability(), 1.0, 1.0, 2.0,
						p.getProbability());
			} else {
				return Util.scale(0.0, threshold.getProbability(), -1.0, 0.0,
						p.getProbability());
			}
		} catch (IncompatibleException e) {
			return Util.scale(0.0, threshold.getProbability(), -1.0, 0.0, 0.0);
		}

	}

	@Override
	public Probability sim(final T a, final T b) {
		try {
			return this.function.sim(a, b);
		} catch (IncompatibleException e) {
			e.printStackTrace();
		}
		return Probability.NULL;
	}

}
