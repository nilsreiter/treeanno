package de.uniheidelberg.cl.a10.patterns.models;

import java.util.Collection;
import java.util.List;

import de.uniheidelberg.cl.a10.patterns.Model;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;

/**
 * An interface for markov models.
 * 
 * @author reiter
 * 
 * @param <T>
 *            The type representing the states
 */
public interface MarkovModel<T> extends Model<List<T>> {

	/**
	 * Returns the starting probabilities
	 * 
	 * @return
	 */
	ProbabilityDistribution<T> getStartingProbabilities();

	/**
	 * Returns the states used in this model
	 * 
	 * @return
	 */
	Collection<T> getStates();

	/**
	 * Returns the transition probability of the model, if transitioning from
	 * state <code>from</code> to state <code>to</code>
	 * 
	 * @param from
	 *            the source state
	 * @param to
	 *            The target state
	 * @return
	 */
	Probability getProbability(T from, T to);

	/**
	 * Returns a collection of final states
	 * 
	 * @return
	 */
	Collection<T> getFinalStates();

	@Override
	Probability p(final List<T> path);
}
