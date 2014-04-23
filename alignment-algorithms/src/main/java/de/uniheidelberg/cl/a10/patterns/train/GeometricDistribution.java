package de.uniheidelberg.cl.a10.patterns.train;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
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
public class GeometricDistribution<T> implements Prior<T>, Serializable {
	private static final long serialVersionUID = 1L;

	Probability p;

	SimilarityFunction<T> sim;

	Map<Integer, Probability> history;

	public GeometricDistribution(final SimilarityFunction<T> simFun,
			final double prior) {
		this.p = Probability.fromProbability(prior);
		this.sim = simFun;
		this.history = new HashMap<Integer, Probability>();
	}

	/**
	 * This method implements an algorithm to calculate the model probability
	 * without the need to create a new HMM with merged states. It follows the
	 * formula:
	 * 
	 * <pre>
	 * P(M', S₂, S₂) = ( P(M') * K(S₁, S₂) ) / ( K(S₁) * K(S₂) * (1-p) )
	 * </pre>
	 * 
	 * 
	 * @param hmm
	 * @param state1
	 * @param state2
	 * @return
	 */
	@Override
	public Probability getModelProbability(final HiddenMarkovModel_impl<T> hmm,
			final Integer state1, final Integer state2) {
		Probability k1 = k(hmm, state1);
		Probability k2 = k(hmm, state2);
		if (k1 == Probability.NULL || k2 == Probability.NULL)
			return Probability.NULL;
		return PMath.divide(
				PMath.multiply(this.getModelProbability(hmm),
						this.k(hmm, state1, state2)),
				PMath.multiply(k1, k2, p.complement()));

	}

	@Override
	public synchronized Probability getModelProbability(
			final HiddenMarkovModel_impl<T> hmm) {

		if (history.containsKey(hmm.hashCode())) {
			return history.get(hmm.hashCode());
		}

		Probability prod = Probability.ONE;
		for (Integer state : hmm.getMM().getStates()) {
			Probability k = k(hmm, state);
			if (k == Probability.NULL)
				return Probability.NULL;
			prod = PMath.multiply(prod, k);
		}
		Probability p_inv = p.complement();
		// System.err.println("p_inv = " + p_inv);
		int n = (hmm.getNumberOfStates() - 1);
		Probability d_opp = Probability.fromLogProbability(p_inv
				.getLogProbability() * n);
		// System.err.println("d_opp = " + d_opp);
		Probability d = PMath.multiply(prod, p, d_opp);
		this.history.put(hmm.hashCode(), d);
		return d;
	}

	protected Probability k(final HiddenMarkovModel_impl<T> hmm,
			final Integer state) {
		return k(hmm, state, state);
	}

	protected Probability k(final HiddenMarkovModel_impl<T> hmm,
			final Integer state1, final Integer state2) {
		Probability p = Probability.ONE;
		for (T event1 : hmm.getEventsForState(state1)) {
			for (T event2 : hmm.getEventsForState(state2)) {
				p = PMath.multiply(p, this.sim(event1, event2));
			}
		}
		return p;
	}

	protected Probability sim(final T state1, final T state2) {
		if (state1 == null || state2 == null)
			return Probability.NULL;
		Probability p;
		try {
			p = this.sim.sim(state1, state2);
		} catch (IncompatibleException e) {
			return Probability.NULL;
		}
		return p;
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
		return p.isPositive();
	}

	@Override
	public void reset() {
		this.history.clear();
	}

	/**
	 * @return the p
	 */
	public Probability getP() {
		return p;
	}

	@Override
	public void precompute(final List<T> l1, final List<T> l2) {
		for (T e1 : l1) {
			for (T e2 : l2) {
				this.sim(e1, e2);
			}
		}
	}

	@Override
	public Probability getModelProbabilityBasedOnSize(final int n) {
		Probability p_inv = p.complement();

		Probability d_opp = Probability.fromLogProbability(p_inv
				.getLogProbability() * n);
		return PMath.multiply(p, d_opp);
	}
}
