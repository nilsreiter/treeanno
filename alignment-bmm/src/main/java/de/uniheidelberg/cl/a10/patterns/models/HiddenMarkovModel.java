package de.uniheidelberg.cl.a10.patterns.models;

import java.util.Collection;
import java.util.List;

import de.uniheidelberg.cl.a10.patterns.Model;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * An interface for hidden markov models
 * 
 * @author reiter
 * 
 * @param <T>
 *            The type representing events / observations
 */
public interface HiddenMarkovModel<T> extends Model<List<T>> {

	@Override
	Probability p(final List<T> sequence);

	/**
	 * Returns a probability for the sequence
	 * 
	 * @param seq
	 * @return
	 */
	Probability p(T... seq);

	/**
	 * Returns the markov model for the states
	 * 
	 * @return
	 */
	MarkovModel<Integer> getMM();

	/**
	 * Returns the emission probability for the state <code>state</code> to emit
	 * event <code>event</code>
	 * 
	 * @param state
	 *            The state from which we emit
	 * @param event
	 *            The event we want to emit
	 * @return
	 */
	Probability getProbability(final Integer state, final T event);

	/**
	 * Returns a collection of observations in this HMM
	 * 
	 * @return
	 */
	Collection<T> getEvents();

	void setEventSimilarity(final SimilarityFunction<T> eventSimilarity);

	SimilarityFunction<T> getEventSimilarity();
}
