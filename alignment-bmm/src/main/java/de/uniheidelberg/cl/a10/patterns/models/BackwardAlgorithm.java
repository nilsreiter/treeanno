package de.uniheidelberg.cl.a10.patterns.models;

import java.util.List;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;

public class BackwardAlgorithm<T> implements Trellis<T> {

	@Override
	public Probability p(final HiddenMarkovModel<T> hmm, final List<T> sequence) {

		// Initialisation
		ProbabilityDistribution<Integer> beta = new ProbabilityDistribution<Integer>();
		for (Integer state : hmm.getMM().getStates()) {
			if (hmm.getMM().getFinalStates().contains(state))
				beta.put(state, Probability.ONE);
		}

		// Induction
		for (int i = sequence.size() - 1; i > 0; i--) {
			ProbabilityDistribution<Integer> next = beta;
			T event = sequence.get(i);
			ProbabilityDistribution<Integer> p = new ProbabilityDistribution<Integer>();
			for (Integer currentState : hmm.getMM().getStates()) {
				Probability d = Probability.NULL;

				for (Integer nextState : hmm.getMM().getStates()) {
					if (hmm.getMM().getProbability(currentState, nextState)
							.isPositive()) {
						Probability d0 = next.get(nextState);
						Probability d1 = hmm.getMM().getProbability(
								currentState, nextState);
						Probability d2 = hmm.getProbability(nextState, event);
						d = PMath.add(d, PMath.multiply(d0, d1, d2));
					}
				}
				p.put(currentState, d);
			}
			beta = p;
		}

		// Totalisation and last element
		Probability p = Probability.NULL;
		for (Integer state : hmm.getMM().getStates()) {
			p = PMath.add(p, PMath.multiply(hmm.getMM()
					.getStartingProbabilities().get(state), beta.get(state),
					hmm.getProbability(state, sequence.get(0))));
		}

		return p;
	}
}
