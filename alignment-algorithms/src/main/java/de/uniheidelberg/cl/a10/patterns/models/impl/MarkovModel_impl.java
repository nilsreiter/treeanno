package de.uniheidelberg.cl.a10.patterns.models.impl;

import java.io.Serializable;
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
import de.uniheidelberg.cl.a10.patterns.data.matrix.DiffMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.models.MarkovModel;
import de.uniheidelberg.cl.a10.patterns.models.OutOfVocabularyException;
import de.uniheidelberg.cl.reiter.util.BasicPair;
import de.uniheidelberg.cl.reiter.util.Counter;

/**
 * This class is a basic implementation of a markov model. In method
 * descriptions, we use the following nomenclature:
 * 
 * <ol>
 * <li>π(s): The starting probability of s</li>
 * <li>T(s1, s2): The transition probability from s1 to s2</li>
 * <li>F: The set of final states</li>
 * </ol>
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class MarkovModel_impl<T> extends AbstractModel<List<T>> implements
		MarkovModel<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The number of observations made in total.
	 */
	int n = 0;

	/**
	 * Stores the degree (number of outgoing edges) for each node.
	 */
	Counter<T> degree = null;

	/**
	 * Stores the set of final states
	 */
	Set<T> finalStates = null;

	/**
	 * Contains transition probabilities from one state to another.
	 */
	MapMatrix<T, T, Probability> transitionProbabilities = null;

	/**
	 * Contains starting probabilites.
	 */
	ProbabilityDistribution<T> startingProbabilities = null;

	/**
	 * Contains all states.
	 */
	Collection<T> states;

	/**
	 * Initilizes an empty markov model, i.e., without states and all
	 * probabilities zero.
	 */
	public MarkovModel_impl() {
		transitionProbabilities = new MapMatrix<T, T, Probability>();
		transitionProbabilities.setDefaultValue(Probability.NULL);
		degree = new Counter<T>();
		finalStates = new HashSet<T>();
		states = new HashSet<T>();
		startingProbabilities = new ProbabilityDistribution<T>();
	}

	/**
	 * Creates a new markov model as a copy of an old one. The matrices may be
	 * differential matrices, so it is not necessarily a deep copy.
	 * 
	 * @param model
	 */
	public MarkovModel_impl(final MarkovModel_impl<T> model) {
		transitionProbabilities =
				new DiffMatrix<T, T, Probability>(model.transitionProbabilities);
		degree = new Counter<T>(model.degree);
		finalStates = new HashSet<T>(model.finalStates);
		startingProbabilities =
				new ProbabilityDistribution<T>(model.startingProbabilities);
		states = new HashSet<T>(model.states);

	}

	/**
	 * "Learns" probabilities from a given sequence. This implementation
	 * recalculates the probabilities iteratively.
	 * 
	 * @param sequence
	 *            The sequence to learn from.
	 */
	public synchronized void learn(final List<T> sequence) {

		// Update starting probabilities
		double prob = 1.0 / ++n;
		for (T t : getStartingProbabilities().keySet()) {
			double oldProb = getStartingProbabilities().get(t).getProbability();
			startingProbabilities.put(t,
					Probability.fromProbability(oldProb - (oldProb * prob)));
		}

		Iterator<T> iterator = sequence.iterator();
		T last = iterator.next();

		startingProbabilities.put(last, startingProbabilities.get(last)
				.getProbability() + prob);
		states.add(last);
		while (iterator.hasNext()) {
			T next = iterator.next();
			states.add(next);
			addEdge(last, next);
			last = next;
		}
		finalStates.add(last);
	}

	/**
	 * Returns a set of states that are accessible from the given state with a
	 * probability > 0.0.
	 * 
	 * @param state
	 *            s1
	 * @return S = {s|P(s1, s) > 0.0}
	 */
	public Set<T> getSuccessors(final T state) {
		return transitionProbabilities.getRow(state).keySet();
	}

	/**
	 * The union of all sets of states that are reachable (p>0.0) from one of
	 * the states in the argument.
	 * 
	 * @param states
	 *            The states for which all successors are returned.
	 * @return
	 */
	public Set<T> getSuccessors(final Collection<T> states) {
		Set<T> r = new HashSet<T>();
		for (T t : states) {
			r.addAll(transitionProbabilities.getRow(t).keySet());
		}
		return r;
	}

	/**
	 * 
	 * @param state
	 *            s1
	 * @return S = {s|P(s, s1) > 0.0}
	 */
	public Set<T> getPredecessors(final T state) {
		Set<T> ret = new HashSet<T>();
		for (T from : this.getStates()) {
			if (transitionProbabilities.get(from, state).isPositive()) {
				ret.add(from);
			}
		}
		return ret;
	}

	/**
	 * This method merges the states <code>s1</code> and <code>s2</code>. In
	 * particular, this means the following (<code>s</code> is the new state):
	 * 
	 * <ul>
	 * <li>π(s) = π(s1) + π(s2)</li>
	 * <li>If s1 or s2 ∈ of F: F = F ∪ {s}</li>
	 * </ul>
	 * 
	 * @param s1
	 * @param s2
	 */
	public void mergeStates(final T s1, final T s2) {
		this.degree.add(s1, this.degree.get(s2));
		Iterator<T> predIter = this.getPredecessors(s2).iterator();
		while (predIter.hasNext()) {
			T s = predIter.next();
			int degree = this.degree.get(s);
			this.addEdge(s, s1, transitionProbabilities.get(s, s2));
			this.removeEdge(s, s2);
			this.degree.put(s, degree);
		}
		Set<T> succIter = getSuccessors(s2);
		Set<BasicPair<T, T>> toRemove = new HashSet<BasicPair<T, T>>();
		// while (succIter.hasNext()) {
		for (T s : succIter) {
			// T s = succIter.next();
			this.addEdge(
					s1,
					s,
					Probability.fromProbability(transitionProbabilities.get(s2,
							s).getProbability() / 2));
			toRemove.add(new BasicPair<T, T>(s2, s));
		}

		for (BasicPair<T, T> p : toRemove) {
			this.removeEdge(p.getElement1(), p.getElement2());
		}
		if (finalStates.contains(s2)) finalStates.add(s1);
		this.degree.remove(s2);
		this.states.remove(s2);
		this.finalStates.remove(s2);
	}

	public void removeEdge(final T from, final T to) {
		this.degree.subtract(from);
		Probability prob = Probability.NULL;
		if (this.getSuccessors(from).size() > 1) {
			double succ = this.getSuccessors(from).size() - 1;
			double d =
					this.transitionProbabilities.get(from, to).getProbability()
					/ succ;
			prob = Probability.fromProbability(d);
		}
		transitionProbabilities.put(from, to, Probability.NULL);
		for (T target : this.getSuccessors(from)) {
			if (to != target) {
				Probability newFactor =
						PMath.add(
								this.transitionProbabilities.get(from, target),
								prob);
				this.transitionProbabilities.put(from, target, newFactor);
			}
		}
	}

	public void removeState(final T state) {
		for (T target : this.getSuccessors(state)) {
			this.removeEdge(state, target);
		}

		for (T from : this.getPredecessors(state)) {
			this.removeEdge(from, state);
		}
		this.degree.remove(state);
		this.finalStates.remove(state);
		this.states.remove(state);
		n--;
	}

	protected void addEdge(final T from, final T to, final Probability prob) {

		for (T target : this.getSuccessors(from)) {
			Probability newFactor =
					PMath.subtract(this.transitionProbabilities.get(from,
							target), PMath.multiply(prob,
									this.transitionProbabilities.get(from, target)));
			this.transitionProbabilities.put(from, target, newFactor);
		}

		this.transitionProbabilities.put(from, to,
				PMath.add(this.transitionProbabilities.get(from, to), prob));
	}

	protected void addEdge(final T from, final T to) {
		this.degree.add(from);
		Probability newprob =
				Probability.fromProbability(1.0 / this.degree.get(from));

		for (T target : this.transitionProbabilities.getColumns()) {

			if (this.transitionProbabilities.get(from, target).isPositive()) {
				Probability newFactor =
						PMath.subtract(this.transitionProbabilities.get(from,
								target), PMath.multiply(newprob,
								this.transitionProbabilities.get(from, target)));
				this.transitionProbabilities.put(from, target, newFactor);
			}
		}

		this.transitionProbabilities.put(from, to,
				PMath.add(transitionProbabilities.get(from, to), newprob));

	}

	@Override
	public Probability p(final List<T> path) {
		if (!states.contains(path.get(0))) {
			throw new OutOfVocabularyException();
		}
		Probability r = this.startingProbabilities.get(path.get(0));
		for (int i = 0; i < path.size() - 1; i++) {
			if (!states.contains(path.get(i + 1))) {
				throw new OutOfVocabularyException();
			}
			r =
					PMath.multiply(
							r,
							getTransitionProbabilities().get(path.get(i),
									path.get(i + 1)));
		}
		return r;
	}

	public Probability p(final T... path) {
		if (!states.contains(path[0])) {
			throw new OutOfVocabularyException();
		}
		Probability r = this.startingProbabilities.get(path[0]);
		for (int i = 0; i < path.length - 1; i++) {
			if (!states.contains(path[i + 1])) {
				throw new OutOfVocabularyException();
			}
			r =
					PMath.multiply(
							r,
							this.getTransitionProbabilities().get(path[i],
									path[i + 1]));
		}
		return r;
	}

	/**
	 * Returns the transition probability under the assumption that m1 and m2
	 * are merged.
	 * 
	 * @param from
	 * @param to
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Probability getConditionalProbability(final T from, final T to,
			final T m1, final T m2) {
		if (m1 == from || m2 == from) {
			return Probability
					.fromProbability((this.transitionProbabilities.get(m1, to)
							.getProbability() + this.transitionProbabilities
							.get(m2, to).getProbability()) / 2);
		} else if (m1 == to || m2 == to) {
			return PMath.add(this.transitionProbabilities.get(from, m1),
					this.transitionProbabilities.get(from, m2));
		} else {
			return this.transitionProbabilities.get(from, to);
		}
	}

	/**
	 * @return the degree
	 */
	protected Map<T, Integer> getDegree() {
		return degree;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("States (").append(states.size()).append("): ").append(states)
				.append("\n");
		b.append("Degree: ").append(this.degree.toString()).append('\n');
		b.append("Transition Probabilities: ")
				.append(this.transitionProbabilities.toString()).append('\n');
		b.append("Starting Probabilities: ").append(this.startingProbabilities)
				.append('\n');
		return b.toString();
	}

	protected Collection<List<T>> getShortestPaths() {
		Collection<List<T>> r = new HashSet<List<T>>();
		for (T state : this.startingProbabilities.keySet()) {
			r.addAll(this.getPaths(state, new HashSet<T>()));
		}
		return r;

	}

	public ProbabilityDistribution<List<T>> getShortestPathsWithProbabilities() {
		ProbabilityDistribution<List<T>> r =
				new ProbabilityDistribution<List<T>>();
		for (List<T> s : this.getShortestPaths()) {
			r.put(s, this.p(s));
		}
		return r;
	}

	protected Collection<List<T>> getPaths(final T state, final Set<T> visited) {
		if (visited.contains(state)) {
			return new HashSet<List<T>>();
		}
		if (this.finalStates.contains(state)) {
			Set<List<T>> r = new HashSet<List<T>>();
			List<T> s = new LinkedList<T>();
			s.add(state);
			r.add(s);
			return r;
		} else {
			Collection<List<T>> r = new HashSet<List<T>>();
			for (T nextState : this.degree.keySet()) {
				if (this.transitionProbabilities.get(state, nextState)
						.isPositive()) {
					visited.add(state);
					r.addAll(this.getPaths(nextState, visited));
				}
			}
			for (List<T> s : r) {
				s.add(0, state);
			}
			return r;
		}
	}

	public T getMostProbableStartState() {
		return this.startingProbabilities.getMax().getSecond().iterator()
				.next();
	}

	@Override
	public Collection<T> getStates() {
		return this.states;
	}

	/**
	 * @return the transitionProbabilities
	 */

	public MapMatrix<T, T, Probability> getTransitionProbabilities() {
		return transitionProbabilities;
	}

	/**
	 * @param transitionProbabilities
	 *            the transitionProbabilities to set
	 */
	public void setTransitionProbabilities(
			final MapMatrix<T, T, Probability> transitionProbabilities) {
		this.transitionProbabilities = transitionProbabilities;
	}

	/**
	 * @param states
	 *            the states to set
	 */
	public void setStates(final Collection<T> states) {
		this.states = states;
	}

	/**
	 * @return the finalStates
	 */
	@Override
	public Set<T> getFinalStates() {
		return finalStates;
	}

	/**
	 * @param finalStates
	 *            the finalStates to set
	 */
	public void setFinalStates(final Set<T> finalStates) {
		this.finalStates = finalStates;
	}

	/**
	 * @return the startingProbabilities
	 */
	@Override
	public ProbabilityDistribution<T> getStartingProbabilities() {
		return startingProbabilities;
	}

	/**
	 * @param startingProbabilities
	 *            the startingProbabilities to set
	 */
	public void setStartingProbabilities(
			final ProbabilityDistribution<T> startingProbabilities) {
		this.startingProbabilities = startingProbabilities;
	}

	@Override
	public Probability getProbability(final T from, final T to) {
		return this.getTransitionProbabilities().get(from, to);
	}

}
