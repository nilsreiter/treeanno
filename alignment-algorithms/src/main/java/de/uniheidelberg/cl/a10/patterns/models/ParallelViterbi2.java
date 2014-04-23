package de.uniheidelberg.cl.a10.patterns.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;

public class ParallelViterbi2<T> implements Viterbi<T> {

	@Override
	public Pair<Probability, List<Integer>> viterbi(
			final HiddenMarkovModel<T> hmm, final List<T> y) {

		ProbabilityDistribution<Integer> delta = new ProbabilityDistribution<Integer>();
		hmm.getMM().getStartingProbabilities();
		Map<Integer, List<Integer>> path = new HashMap<Integer, List<Integer>>();

		// initialisation
		for (Integer state : hmm.getMM().getStates()) {
			delta.put(state, PMath.multiply(hmm.getMM()
					.getStartingProbabilities().get(state),
					hmm.getProbability(state, y.get(0))));
			List<Integer> p = new LinkedList<Integer>();
			p.add(state);
			path.put(state, p);
		}

		// iterating over the events of the sequence
		for (int t = 1; t < y.size(); t++) {
			ProbabilityDistribution<Integer> delta1 = new ProbabilityDistribution<Integer>();
			Map<Integer, List<Integer>> path1 = new HashMap<Integer, List<Integer>>();

			for (Integer currentState : hmm.getMM().getStates()) {
				ProbabilityDistribution<Integer> dist = new ProbabilityDistribution<Integer>();
				for (Integer previousState : hmm.getMM().getStates()) {
					Probability p0 = delta.get(previousState);
					Probability p1 = hmm.getMM().getProbability(previousState,
							currentState);
					Probability p2 = hmm.getProbability(currentState, y.get(t));
					Probability p = PMath.multiply(p0, p1, p2);
					dist.put(previousState, p);
				}
				Pair<Probability, Set<Integer>> bp = dist.getMax();
				if (bp.getFirst().isPositive()) {
					Integer bestState = bp.getSecond().iterator().next();
					if (path.containsKey(bestState)) {
						path1.put(currentState, new LinkedList<Integer>());
						path1.get(currentState).addAll(path.get(bestState));
						path1.get(currentState).add(currentState);
						delta1.put(currentState, bp.getFirst());
					}
				}
			}
			delta = delta1;
			path = path1;
		}

		// finalising
		Pair<Probability, Set<Integer>> finalPair = delta.getMax();
		Pair<Probability, List<Integer>> ret = new Pair<Probability, List<Integer>>(
				finalPair.getFirst(), new LinkedList<Integer>());
		for (Integer state : finalPair.getSecond()) {
			if (hmm.getMM().getFinalStates().contains(state)) {
				ret.getSecond().addAll(path.get(state));
			}
		}

		return ret;
	}
}
