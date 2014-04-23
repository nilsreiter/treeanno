package de.uniheidelberg.cl.a10.patterns.models.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.IntegerMapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.IntegerMatrix;
import de.uniheidelberg.cl.reiter.util.BasicPair;

public class PreciseMarkovModel_impl<T> extends MarkovModel_impl<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This matrix stores the absolute numbers of transitions from one state to
	 * another. It is used for recomputing probabilities after/while merging
	 * states.
	 */
	IntegerMatrix<T, T> history = null;

	public PreciseMarkovModel_impl() {
		super();
		history = new IntegerMapMatrix<T, T>();

	}

	public PreciseMarkovModel_impl(final PreciseMarkovModel_impl<T> m) {
		super(m);
		history = new IntegerMapMatrix<T, T>(m.history);
	}

	private void recalculateProbabilities(final T state) {
		for (T target : this.getStates()) {
			Probability prob = Probability.fromProbability((double) history
					.get(state, target) / (double) this.degree.get(state));
			this.transitionProbabilities.put(state, target, prob);
		}
	}

	@Override
	protected void addEdge(final T from, final T to, final Probability prob) {
		throw new UnsupportedOperationException();
	}

	protected void addEdges(final T from, final T to, final int number) {
		this.degree.add(from, number);
		this.history.put(from, to, history.get(from, to) + number);

		this.recalculateProbabilities(from);
	}

	@Override
	public void removeEdge(final T from, final T to) {
		this.degree.subtract(from);
		this.history.put(from, to, 0);
		this.recalculateProbabilities(from);
		transitionProbabilities.put(from, to, Probability.NULL);

	}

	@Override
	protected void addEdge(final T from, final T to) {
		this.states.add(to);
		this.states.add(from);
		this.degree.add(from);
		this.history.put(from, to, history.get(from, to) + 1);

		this.recalculateProbabilities(from);
	}

	@Override
	public void mergeStates(final T s1, final T s2) {
		Iterator<T> predIter = this.getPredecessors(s2).iterator();
		while (predIter.hasNext()) {
			T s = predIter.next();
			int degree = this.degree.get(s);
			this.addEdges(s, s1, history.get(s, s2));
			this.removeEdge(s, s2);
			this.degree.put(s, degree);
			this.recalculateProbabilities(s);
		}
		// this.degree.add(s1, this.degree.get(s2));
		Set<T> succIter = getSuccessors(s2);
		Set<BasicPair<T, T>> toRemove = new HashSet<BasicPair<T, T>>();
		for (T s : succIter) {
			this.addEdges(s1, s, history.get(s2, s));
			toRemove.add(new BasicPair<T, T>(s2, s));
		}

		for (BasicPair<T, T> p : toRemove) {
			this.removeEdge(p.getElement1(), p.getElement2());
		}
		this.recalculateProbabilities(s1);
		startingProbabilities.put(
				s1,
				PMath.add(startingProbabilities.get(s1),
						startingProbabilities.get(s2)));
		startingProbabilities.remove(s2);
		this.degree.remove(s2);
		this.states.remove(s2);
		if (this.finalStates.contains(s2)) {
			this.finalStates.add(s1);
		}
		this.finalStates.remove(s2);
	}

	@Override
	public void removeState(final T state) {
		for (T target : this.getSuccessors(state)) {
			this.removeEdge(state, target);
		}

		for (T from : this.getPredecessors(state)) {
			this.removeEdge(from, state);
		}
		this.degree.remove(state);
		this.finalStates.remove(state);
		n--;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(super.toString());
		b.append("History: ").append(history.toString()).append("\n");
		return b.toString();
	}
}
