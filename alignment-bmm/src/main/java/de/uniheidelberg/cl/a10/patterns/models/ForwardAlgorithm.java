package de.uniheidelberg.cl.a10.patterns.models;

import java.util.List;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;

/**
 * A trellis implementation for hidden markov models
 * 
 * @author reiter
 * 
 * @param <T>
 *            The event of the hmm
 */
public class ForwardAlgorithm<T> implements Trellis<T> {
	@Override
	public Probability p(final HiddenMarkovModel<T> hmm, final List<T> sequence) {

		ProbabilityDistribution<Integer> alpha = new ProbabilityDistribution<Integer>(
				hmm.getMM().getStartingProbabilities());

		for (int i = 0; i < sequence.size() - 1; i++) {
			ProbabilityDistribution<Integer> last = alpha;
			T event = sequence.get(i);
			ProbabilityDistribution<Integer> p = new ProbabilityDistribution<Integer>();
			for (Integer currentState : hmm.getMM().getStates()) {
				Probability d = Probability.NULL;
				for (Integer previousState : hmm.getMM().getStates()) {
					if (hmm.getMM().getProbability(previousState, currentState)
							.isPositive()) {
						Probability d0 = last.get(previousState);
						Probability d1 = hmm.getMM().getProbability(
								previousState, currentState);
						Probability d2 = hmm.getProbability(previousState,
								event);
						d = PMath.add(d, PMath.multiply(d0, d1, d2));
					}

				}
				p.put(currentState, d);

			}
			alpha = p;
		}

		Probability d = Probability.NULL;
		for (Integer state : hmm.getMM().getStates()) {
			if (hmm.getMM().getFinalStates().contains(state)) {
				d = PMath.add(
						d,
						PMath.multiply(
								alpha.get(state),
								hmm.getProbability(state,
										sequence.get(sequence.size() - 1))));
			}
		}
		return d;

	}
}
