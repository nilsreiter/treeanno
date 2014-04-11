package de.uniheidelberg.cl.a10.patterns.models.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;

/**
 * This class implements a hidden markov model (HMM) that ensures that each
 * sequence begins and ends in the same state. To this end, each sequence is
 * enhanced by a {@link #startSymbol} and {@link #endSymbol}. Internally, the
 * hidden state sequence begins and ends at specific states which emit the start
 * resp. end symbol with a probability of 1.0.
 * 
 * @author reiter
 * 
 * @param <T>
 *            The class of the observations.
 */
public class SEHiddenMarkovModel_impl<T> extends HiddenMarkovModel_impl<T> {
	private static final long serialVersionUID = 1L;
	public static int START = 0;
	public static int END = -1;

	T startSymbol = null;
	T endSymbol = null;

	public SEHiddenMarkovModel_impl() {
		super();
		state = 1;
	}

	public SEHiddenMarkovModel_impl(final SEHiddenMarkovModel_impl<T> hmm) {
		super(hmm);
		this.state = hmm.state;
		this.startSymbol = hmm.startSymbol;
		this.endSymbol = hmm.endSymbol;
	}

	@Override
	public void init(final List<T> seq) {

		List<T> sequence = this.ensureStartSymbolEndSymbol(new LinkedList<T>(
				seq));

		List<Integer> intSequence = new LinkedList<Integer>();
		intSequence.add(START);

		for (int i = 1; i < sequence.size() - 1; i++) {
			T next = sequence.get(i);

			this.emissionProbabilities.put(state, next, Probability.ONE);
			intSequence.add(state);
			state++;
		}
		intSequence.add(END);

		this.mm.learn(intSequence);
	}

	public LinkedList<T> ensureStartSymbolEndSymbol(final List<T> seq) {
		LinkedList<T> sequence = new LinkedList<T>(seq);
		if (sequence.getFirst() != getStartSymbol()) {
			sequence.add(0, getStartSymbol());
		}
		if (sequence.getLast() != getEndSymbol()) {
			sequence.add(getEndSymbol());
		}
		return sequence;
	}

	@Override
	public Probability p(final List<T> sequence) {
		// return this.parallelViterbi(sequence).getElement1();
		return super.trellis(this.ensureStartSymbolEndSymbol(sequence));
	}

	/**
	 * @return the startSymbol
	 */
	public T getStartSymbol() {
		return startSymbol;
	}

	/**
	 * @param startSymbol
	 *            the startSymbol to set
	 */
	public void setStartSymbol(final T startSymbol) {
		this.startSymbol = startSymbol;
	}

	/**
	 * @return the endSymbol
	 */
	public T getEndSymbol() {
		return endSymbol;
	}

	/**
	 * @param endSymbol
	 *            the endSymbol to set
	 */
	public void setEndSymbol(final T endSymbol) {
		this.endSymbol = endSymbol;
	}

	public Pair<Probability, List<Integer>> viterbi(final List<T> seq) {
		List<T> sequence = this.ensureStartSymbolEndSymbol(seq);
		Probability alpha = PMath.multiply(mm.getStartingProbabilities()
				.getHighestProbability(), this.getProbability(0,
				sequence.get(0)));
		LinkedList<Integer> path = new LinkedList<Integer>();
		path.add(START);
		// we assume to have a single starting state
		for (int i = 1; i < sequence.size(); i++) {
			T t = sequence.get(i);
			ProbabilityDistribution<Integer> p = new ProbabilityDistribution<Integer>();
			for (Integer state : mm.getSuccessors(path.getLast())) {
				Probability d = Probability.NULL;
				Integer prevState = path.getLast();
				Probability d0 = alpha;
				Probability d1 = mm.getTransitionProbabilities().get(prevState,
						state);
				Probability d2 = this.getProbability(state, t);
				d = PMath.add(d, PMath.multiply(d0, d1, d2));

				p.put(state, d);

			}
			alpha = p.getHighestProbability();
			Pair<Probability, Set<Integer>> max = p.getMax();
			try {
				Integer s = max.getSecond().iterator().next();
				path.add(s);
			} catch (NoSuchElementException e) {
				return new Pair<Probability, List<Integer>>(Probability.NULL,
						null);
			}

		}

		Probability d = Probability.NULL;

		if (mm.getFinalStates().contains(path.getLast())) {
			d = alpha;
		}
		path.remove(0);
		path.remove(path.size() - 1);
		return new Pair<Probability, List<Integer>>(d, path);

	}

	/**
	 * @return
	 * @see de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl#getFinalStates()
	 */
	@Override
	public Set<Integer> getFinalStates() {
		Set<Integer> r = new HashSet<Integer>();
		r.add(END);
		return r;
	}

	/**
	 * Returns the (hidden) start state.
	 * 
	 * @return
	 */
	public Integer getStartState() {
		return START;
	}

	@Override
	public Probability getProbability(final Integer state, final T event) {
		if (state == START && event == null)
			return Probability.ONE;
		if (state == END && event == null)
			return Probability.ONE;
		return super.getProbability(state, event);
	}

	@Override
	public Probability p(final List<Integer> stateSequence,
			List<T> eventSequence, final Integer state1, final Integer state2) {

		eventSequence = this.ensureStartSymbolEndSymbol(eventSequence);
		return super.p(stateSequence, eventSequence, state1, state2);
	}

	@Override
	public Probability p(final List<Integer> stateSequence,
			List<T> eventSequence) {

		eventSequence = this.ensureStartSymbolEndSymbol(eventSequence);
		// stateSequence = this.ensureStartEnd(stateSequence);
		return super.p(stateSequence, eventSequence);
	}

}
