package de.uniheidelberg.cl.a10.patterns.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;

public class ForwardViterbi<T> implements Viterbi<T> {
	private static class TNode {
		public Probability prob;
		public LinkedList<Integer> v_path;
		public Probability v_prob;

		public TNode(final Probability prob, final List<Integer> v_path,
				final Probability v_prob) {
			this.prob = prob;
			this.v_path = new LinkedList<Integer>(v_path);
			this.v_prob = v_prob;
		}

		@Override
		public String toString() {
			return String.valueOf(prob) + " " + v_path + " "
					+ String.valueOf(v_prob);
		}
	}

	@Override
	public Pair<Probability, List<Integer>> viterbi(
			final HiddenMarkovModel<T> hmm, final List<T> y) {
		ProbabilityDistribution<Integer> sp = new ProbabilityDistribution<Integer>(
				hmm.getMM().getStartingProbabilities());
		Collection<Integer> X = hmm.getMM().getStates();

		Map<Integer, TNode> T = new HashMap<Integer, TNode>();
		for (Integer state : X) {

			T.put(state,
					new TNode(sp.get(state), Arrays.asList(state), sp
							.get(state)));
		}

		for (int outputIndex = 0; outputIndex < y.size() - 1; outputIndex++) {
			T output = y.get(outputIndex);
			Map<Integer, TNode> U = new HashMap<Integer, TNode>();
			for (Integer next_state : X) {
				Probability total = Probability.NULL;

				List<Integer> argmax = new LinkedList<Integer>();
				Probability valmax = Probability.NULL;
				for (Integer state : X) {
					Probability prob = T.get(state).prob;
					LinkedList<Integer> v_path = new LinkedList<Integer>(
							T.get(state).v_path);
					Probability v_prob = T.get(state).v_prob;
					Probability p = PMath.multiply(hmm.getProbability(state,
							output),
							hmm.getMM().getProbability(state, next_state));
					prob = PMath.multiply(prob, p);
					v_prob = PMath.multiply(v_prob, p);
					total = PMath.add(total, prob);
					if (v_prob.getProbability() > valmax.getProbability()) {
						argmax = copy(v_path);
						argmax.add(next_state);
						valmax = v_prob;
					}
				}
				U.put(next_state, new TNode(total, argmax, valmax));
			}
			T = U;
		}
		// apply sum/max to the final states:
		Probability total = Probability.NULL;
		List<Integer> argmax = new LinkedList<Integer>();
		Probability valmax = Probability.NULL;
		for (Integer state : X) {
			Probability prob = T.get(state).prob;
			List<Integer> v_path = copy(T.get(state).v_path);
			Probability v_prob = T.get(state).v_prob;
			total = PMath.add(total, prob);
			if (v_prob.getProbability() > valmax.getProbability()) {
				argmax = copy(v_path);
				valmax = v_prob;
			}
		}

		return new Pair<Probability, List<Integer>>(valmax, argmax);
	}

	public static <T> List<T> copy(final List<T> l) {
		if (l instanceof ArrayList) {
			return (List<T>) ((ArrayList<T>) l).clone();
		}
		if (l instanceof LinkedList) {
			return (List<T>) ((LinkedList<T>) l).clone();
		}
		LinkedList<T> r = new LinkedList<T>();
		for (T e : l) {
			r.add(e);
		}
		return r;
	}
}
