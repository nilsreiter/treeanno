package de.uniheidelberg.cl.a10.patterns.models.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniheidelberg.cl.a10.patterns.AbstractModel;
import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.models.ForwardAlgorithm;
import de.uniheidelberg.cl.a10.patterns.models.HiddenMarkovModel;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class HiddenMarkovModel_impl<T> extends AbstractModel<List<T>> implements
		Serializable, HiddenMarkovModel<T> {
	private static final long serialVersionUID = 1L;

	PreciseMarkovModel_impl<Integer> mm = null;

	MapMatrix<Integer, T, Probability> emissionProbabilities = null;

	int state = 0;

	transient SimilarityFunction<T> eventSimilarity = null;

	MapMatrix<Integer, Integer, ProbabilityDistribution<T>> history = null;

	public HiddenMarkovModel_impl() {
		this.mm = new PreciseMarkovModel_impl<Integer>();
		this.emissionProbabilities = new MapMatrix<Integer, T, Probability>();
		this.emissionProbabilities.setDefaultValue(Probability.NULL);
		this.history = new MapMatrix<Integer, Integer, ProbabilityDistribution<T>>();
		this.history.setDefaultValue(null);
		this.state = 0;
	}

	public HiddenMarkovModel_impl(final HiddenMarkovModel_impl<T> hmm) {
		this.mm = new PreciseMarkovModel_impl<Integer>(hmm.mm);
		this.emissionProbabilities = new MapMatrix<Integer, T, Probability>(
				hmm.emissionProbabilities);
		this.state = hmm.state;
		this.history = new MapMatrix<Integer, Integer, ProbabilityDistribution<T>>(
				hmm.history);
		this.history.setDefaultValue(null);
	}

	public void init(final List<T> sequence) {
		Iterator<T> iterator = sequence.iterator();
		List<Integer> intSequence = new LinkedList<Integer>();

		while (iterator.hasNext()) {
			T next = iterator.next();
			this.emissionProbabilities.put(state, next, Probability.ONE);
			intSequence.add(state);
			state++;
		}
		this.mm.learn(intSequence);
	}

	public Collection<Integer> getStates() {
		return mm.getStates();
	}

	@Override
	public Set<T> getEvents() {
		return this.emissionProbabilities.getColumns();
	}

	public Set<T> getEventsForState(final Integer state) {
		Map<T, Probability> row = this.emissionProbabilities.getRow(state);
		if (row != null)
			return row.keySet();
		return new HashSet<T>();
	}

	@Override
	public Probability getProbability(final Integer state, final T event) {
		if (this.getEventSimilarity() != null) {
			Probability p = Probability.NULL;
			for (T ev : this.emissionProbabilities.getRow(state).keySet()) {
				if (ev != null) {
					Probability p2 = Probability.NULL;
					try {
						p2 = PMath.multiply(this.emissionProbabilities.get(
								state, ev),
								this.getEventSimilarity().sim(event, ev));
					} catch (IncompatibleException e) {
					}

					p = PMath.add(p, p2);
				}
			}
			return p;
		}
		return this.emissionProbabilities.get(state, event);
	}

	public boolean emissionsEqual(final Integer state1, final Integer state2) {
		return this.emissionProbabilities.getRow(state1).equals(
				emissionProbabilities.getRow(state2));
	}

	public void merge(final Integer state1, final Integer state2) {
		this.mm.mergeStates(state1, state2);

		for (T evt : this.emissionProbabilities.getColumns()) {

			this.emissionProbabilities.put(state1, evt,
					this.mergeEmissionProbabilities(state1, state2, evt));
			this.emissionProbabilities.put(state2, evt, Probability.NULL);
		}

	}

	// TODO: We calculate the emission probabilities as average. Is this
	// correct? Should this be weighted somehow?
	protected Probability mergeEmissionProbabilities(final Integer state1,
			final Integer state2, final T output) {
		return Probability.fromProbability((this.emissionProbabilities.get(
				state1, output).getProbability() + this.emissionProbabilities
				.get(state2, output).getProbability()) / 2.0);
	}

	/**
	 * 
	 * @param sequence
	 * @return log probability
	 */
	public Probability trellis(final List<T> sequence) {
		return new ForwardAlgorithm<T>().p(this, sequence);
	}

	/**
	 * This method behaves like {@link #p(Sequence, Sequence)}, but allows to
	 * provide two states as arguments. The probability is then computed under
	 * the assumption that state1 and state2 would have been merged. The method
	 * assumes that state1 and state2 are neither the first nor the last state
	 * in the state sequence.
	 * 
	 * 
	 * @param stateSequence
	 *            The state sequence
	 * @param eventSequence
	 *            The event sequence
	 * @param state1
	 *            The first state to be merged
	 * @param state2
	 *            The second state to be merged
	 * @return A probability
	 */
	public Probability p(final List<Integer> stateSequence,
			final List<T> eventSequence, final Integer state1,
			final Integer state2) {
		Probability p = this.p(stateSequence, eventSequence);
		if (stateSequence.contains(state1) && stateSequence.contains(state2)) {
			int state1Index = stateSequence.indexOf(state1);
			int state2Index = stateSequence.indexOf(state2);

			Probability[] c = new Probability[] {
					this.mergeEmissionProbabilities(state1, state2,
							eventSequence.get(state1Index)),
					this.mm.getConditionalProbability(state1,
							stateSequence.get(state1Index + 1), state1, state2),
					this.mm.getConditionalProbability(
							stateSequence.get(state1Index - 1), state1, state1,
							state2),
					this.mergeEmissionProbabilities(state1, state2,
							eventSequence.get(state2Index)),
					this.mm.getConditionalProbability(state1,
							stateSequence.get(state2Index + 1), state1, state2),
					this.mm.getConditionalProbability(
							stateSequence.get(state2Index - 1), state1, state1,
							state2) };

			Probability[] d = new Probability[] {
					this.getProbability(state1, eventSequence.get(state1Index)),
					mm.getProbability(state1,
							stateSequence.get(state1Index + 1)),
					mm.getProbability(stateSequence.get(state1Index - 1),
							state1),
					this.getProbability(state2, eventSequence.get(state2Index)),
					mm.getProbability(state2,
							stateSequence.get(state2Index + 1)),
					mm.getProbability(stateSequence.get(state2Index - 1),
							state2) };
			p = PMath.multiply(p, c[0], c[1], c[2], c[3], c[4], c[5]);
			return PMath.divide(p, PMath.multiply(d));

		} else if (stateSequence.contains(state1)
				&& !stateSequence.contains(state2)) {
			Integer s = state1;
			int stateIndex = stateSequence.indexOf(s);
			Probability e = this.mergeEmissionProbabilities(state1, state2,
					eventSequence.get(stateIndex));
			Probability t1 = this.mm.getConditionalProbability(s,
					stateSequence.get(stateIndex + 1), state1, state2);
			Probability t2 = this.mm.getConditionalProbability(
					stateSequence.get(stateIndex - 1), s, state1, state2);
			p = PMath.multiply(p, e, t1, t2);

			Probability q1 = this.getProbability(s,
					eventSequence.get(stateIndex));
			Probability q2 = mm.getProbability(s,
					stateSequence.get(stateIndex + 1));
			Probability q3 = mm.getProbability(
					stateSequence.get(stateIndex - 1), s);
			Probability q = PMath.multiply(q1, q2, q3);

			return PMath.divide(p, q);

		} else if (!stateSequence.contains(state1)
				&& stateSequence.contains(state2)) {
			Integer s = state2;
			int stateIndex = stateSequence.indexOf(s);
			Probability e = this.mergeEmissionProbabilities(state1, state2,
					eventSequence.get(stateIndex));
			Probability t1 = this.mm.getConditionalProbability(s,
					stateSequence.get(stateIndex + 1), state1, state2);
			Probability t2 = this.mm.getConditionalProbability(
					stateSequence.get(stateIndex - 1), s, state1, state2);
			p = PMath.multiply(p, e, t1, t2);

			Probability q1 = this.getProbability(s,
					eventSequence.get(stateIndex));
			Probability q2 = mm.getProbability(s,
					stateSequence.get(stateIndex + 1));
			Probability q3 = mm.getProbability(
					stateSequence.get(stateIndex - 1), s);
			Probability q = PMath.multiply(q1, q2, q3);

			return PMath.divide(p, q);

		} else {
			return p;
		}
	}

	/**
	 * This method calculates the probability for a given event and state
	 * sequence.
	 * 
	 * @param stateSequence
	 *            The state sequence, internal in the HMM
	 * @param eventSequence
	 *            The event sequence
	 * @return A log probability
	 */
	public Probability p(final List<Integer> stateSequence,
			final List<T> eventSequence) {

		Probability d = PMath
				.multiply(
						this.mm.getStartingProbabilities().get(
								stateSequence.get(0)),
						this.getProbability(stateSequence.get(0),
								eventSequence.get(0)));
		for (int i = 1; i < stateSequence.size(); i++) {
			if (d == Probability.NULL)
				return d;
			d = PMath.multiply(
					d,
					mm.getTransitionProbabilities().get(
							stateSequence.get(i - 1), stateSequence.get(i)),
					this.getProbability(stateSequence.get(i),
							eventSequence.get(i)));
		}
		if (this.mm.getFinalStates().contains(
				stateSequence.get(stateSequence.size() - 1)))
			return d;
		return Probability.NULL;
	}

	/**
	 * 
	 * @param sequence
	 * @return A log probability
	 */
	@Override
	public Probability p(final List<T> sequence) {
		return this.trellis(sequence);
	}

	@Override
	public Probability p(final T... seq) {
		return this.trellis(Arrays.asList(seq));
	}

	public Probability p(final Collection<List<T>> sequences) {
		Probability p = Probability.ONE;
		for (List<T> s : sequences) {
			p = PMath.multiply(p, this.p(s));
		}
		return p;
	}

	public int getNumberOfStates() {
		return mm.getStates().size();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(mm.toString());
		b.append("Emission Probabilities: " + this.emissionProbabilities)
				.append('\n');
		b.append("History").append('\n');
		b.append(this.history).append('\n');
		return b.toString();
	}

	/**
	 * @return the emissionProbabilities
	 */

	public MapMatrix<Integer, T, Probability> getEmissionProbabilities() {
		return emissionProbabilities;
	}

	/**
	 * @param emissionProbabilities
	 *            the emissionProbabilities to set
	 */
	public void setEmissionProbabilities(
			final MapMatrix<Integer, T, Probability> emissionProbabilities) {
		this.emissionProbabilities = emissionProbabilities;
	}

	/**
	 * @return
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#getStartingProbabilities()
	 */
	public ProbabilityDistribution<Integer> getStartingProbabilities() {
		return mm.getStartingProbabilities();
	}

	/**
	 * @return
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#getTransitionProbabilities()
	 */
	public MapMatrix<Integer, Integer, Probability> getTransitionProbabilities() {
		return mm.getTransitionProbabilities();
	}

	/**
	 * @param transitionProbabilities
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#setTransitionProbabilities(de.uniheidelberg.cl.a10.patterns.data.matrix.DoubleMapMatrix)
	 */
	public void setTransitionProbabilities(
			final MapMatrix<Integer, Integer, Probability> transitionProbabilities) {
		mm.setTransitionProbabilities(transitionProbabilities);
	}

	/**
	 * @param startingProbabilities
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#setStartingProbabilities(de.uniheidelberg.cl.a10.patterns.data.DoubleDistribution)
	 */
	public void setStartingProbabilities(
			final ProbabilityDistribution<Integer> startingProbabilities) {
		mm.setStartingProbabilities(startingProbabilities);
	}

	/**
	 * @param states
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#setStates(java.util.Set)
	 */
	public void setStates(final Collection<Integer> states) {
		mm.setStates(states);
	}

	/**
	 * @return
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#getFinalStates()
	 */
	public Set<Integer> getFinalStates() {
		return mm.getFinalStates();
	}

	/**
	 * @param finalStates
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#setFinalStates(java.util.Set)
	 */
	public void setFinalStates(final Set<Integer> finalStates) {
		mm.setFinalStates(finalStates);
	}

	/**
	 * @return the eventSimilarity
	 */
	@Override
	public SimilarityFunction<T> getEventSimilarity() {
		return eventSimilarity;
	}

	/**
	 * @param eventSimilarity
	 *            the eventSimilarity to set
	 */
	@Override
	public void setEventSimilarity(final SimilarityFunction<T> eventSimilarity) {
		this.eventSimilarity = eventSimilarity;
	}

	/**
	 * @return the mm
	 */
	@Override
	public MarkovModel_impl<Integer> getMM() {
		return mm;
	}

}
