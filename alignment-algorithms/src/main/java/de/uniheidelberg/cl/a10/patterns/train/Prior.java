package de.uniheidelberg.cl.a10.patterns.train;

import java.util.List;

import de.uniheidelberg.cl.a10.patterns.ModelPrior;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;

/**
 * This interface represents various possible implementations of priors for
 * models.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface Prior<T> extends ModelPrior<HiddenMarkovModel_impl<T>> {
	/**
	 * 
	 * @param hmm
	 * @return A log probability
	 */

	@Override
	Probability getModelProbability(final HiddenMarkovModel_impl<T> hmm);

	Probability getModelProbability(final HiddenMarkovModel_impl<T> hmm,
			final Integer s1, final Integer s2);

	Probability getModelProbabilityBasedOnSize(int n);

	/**
	 * This method decides whether the merging of states s1 and s2 should be
	 * considered at all. If the method returns false, merging the states will
	 * not be considered.
	 * 
	 * @param hmm
	 *            The HMM
	 * @param s1
	 * @param s2
	 * @return
	 */
	boolean isCandidate(final HiddenMarkovModel_impl<T> hmm, final Integer s1,
			final Integer s2);

	/**
	 * This method is called after each iteration of merges, i.e., if a decision
	 * has been made on merging two states.
	 */
	void reset();

	void precompute(List<T> l1, List<T> l2);
}
