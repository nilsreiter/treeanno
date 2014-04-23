package de.uniheidelberg.cl.a10.patterns.models;

import java.util.List;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * This interface must be implemented by all viterbi algorithms.
 * 
 * @author reiter
 * 
 * @param <T>
 *            The type representing observations in the HMM
 */
public interface Viterbi<T> {

	/**
	 * Applies the viterbi algorithm and returns a pair of the most probable
	 * path for the given sequence and its probability.
	 * 
	 * @param hmm
	 *            The hidden markov model
	 * @param y
	 *            The sequence
	 * @return
	 */
	Pair<Probability, List<Integer>> viterbi(final HiddenMarkovModel<T> hmm,
			final List<T> y);
}
