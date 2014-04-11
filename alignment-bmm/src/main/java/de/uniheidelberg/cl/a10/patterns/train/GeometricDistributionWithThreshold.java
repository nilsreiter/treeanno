package de.uniheidelberg.cl.a10.patterns.train;

import java.io.Serializable;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * This class implements the geometric distribution described in this paper:
 * 
 * <pre>
 * @article{Finlayson:2009aa,
 * 	Author = {Finlayson, Mark},
 * 	Journal = {New Frontiers in Analogy Research},
 * 	Pages = {127--136},
 * 	Title = {{Deriving Narrative Morphologies via Analogical Story Merging}},
 * 	Year = {2009}}
 * </pre>
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class GeometricDistributionWithThreshold<T> extends
		GeometricDistribution<T> implements Prior<T>,
		Serializable {
	private static final long serialVersionUID = 1L;

	double threshold = Double.MIN_VALUE;

	public GeometricDistributionWithThreshold(
			final SimilarityFunction<T> simFun, final double prior,
			final double threshold) {
		super(simFun, prior);
		this.threshold = threshold;
	}

	@Override
	protected Probability k(final HiddenMarkovModel_impl<T> hmm,
			final Integer state1, final Integer state2) {

		for (T event1 : hmm.getEventsForState(state1)) {
			for (T event2 : hmm.getEventsForState(state2)) {
				if (this.sim(event1, event2).getProbability() < this.threshold)
					return Probability.NULL;
			}
		}
		return Probability.ONE;
	}

	@Override
	public boolean isCandidate(final HiddenMarkovModel_impl<T> hmm,
			final Integer s1, final Integer s2) {
		if (s1 == s2)
			return true;
		Probability p = Probability.ONE;
		for (T event1 : hmm.getEventsForState(s1)) {
			for (T event2 : hmm.getEventsForState(s2)) {
				PMath.multiply(p, this.sim(event1, event2));
			}
		}
		return (p.getProbability() > threshold);
	}

}
