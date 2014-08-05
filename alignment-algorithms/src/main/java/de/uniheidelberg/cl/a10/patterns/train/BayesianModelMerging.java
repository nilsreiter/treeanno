package de.uniheidelberg.cl.a10.patterns.train;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.TrainingConfiguration;
import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.ParallelViterbi2;
import de.uniheidelberg.cl.a10.patterns.models.Viterbi;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * This is an implementation of a method called "Bayesian Model Merging".
 * 
 * It is described in this paper:
 * 
 * <pre>
 * \@article{Stolcke:1993aa,
 * 	Author = {Stolcke, Andreas and Omohundro, Stephen},
 * 	Journal = {Advances in Neural Information Processing Systems},
 * 	Title = {Hidden Markov Model Induction by Bayesian Model Merging},
 * 	Year = {1993}}
 * </pre>
 * 
 * One of the requirements is that the underlying HMM needs a clearly defined
 * start and end state, so we're using SEHiddenMarkovModel internally.
 * 
 * <h2>Performance</h2> In order to reduce runtime, we're employing the
 * following optimisations:
 * <ol>
 * <li>Viterbi likelihood: Instead of calculating the exact probability of a
 * sequence, we're calculating the probability of the most likely path, leading
 * to the given sequence of observations.</li>
 * <li>Constant Viterbi paths: We assume that if a viterbi path passes through
 * state A and state A gets merged with state B, the viterbi path passes through
 * the new state. This way, we can reuse once calculated viterbi paths over and
 * over.</li>
 * <li>Precheck for similarity: Before actually merging the states, we ask the
 * prior if s1 and s2 are a good candidate pair. See also
 * {@link Prior#isCandidate(HiddenMarkovModel_impl, Integer, Integer)} .</li>
 * <li>Greedy: If we find merge candidates which increase the P(M|S), we merge
 * them.</li>
 * </ol>
 * 
 * <h2>Nomenclature</h2>
 * <table>
 * <tr>
 * <th>Symbol</th>
 * <th>Meaning</th>
 * </tr>
 * <tr>
 * <td>M</td>
 * <td>a HMM</td>
 * </tr>
 * <tr>
 * <td>M₀</td>
 * <td>the current HMM</td>
 * </tr>
 * <tr>
 * <td>S</td>
 * <td>a set of sequences</td>
 * </tr>
 * <tr>
 * <td>V(S)</td>
 * <td>A mapping of event sequences to their most likely (viterbi) state
 * sequence</td>
 * </tr>
 * </table>
 * 
 * 
 * @author reiter
 * 
 * @param <T>
 *            The class T represents the events.
 */
public class BayesianModelMerging<T extends HasDocument> extends
		AbstractTrainer<List<T>> {

	transient Prior<T> prior = null;

	Map<List<T>, List<Integer>> viterbiPaths = null;

	SimilarityFunction<T> eventSimilarity = null;

	List<Pair<Integer, Integer>> mergeHistory =
			new LinkedList<Pair<Integer, Integer>>();

	BMMConfiguration configuration = null;

	protected BayesianModelMerging(final BMMConfiguration bmmc,
			final Prior<T> prior) {
		this.configuration = bmmc;
		this.prior = prior;
	}

	/**
	 * Performs merges until the conditional probability of the model no longer
	 * increases.
	 * 
	 * @param sequences
	 */
	protected void performMerges(final SEHiddenMarkovModel_impl<T> hmm,
			final Collection<List<T>> sequences) {
		Map<List<T>, List<Integer>> viterbiPaths =
				new HashMap<List<T>, List<Integer>>();
		Viterbi<T> vi = new ParallelViterbi2<T>();
		for (List<T> seq : sequences) {
			List<T> sseq = hmm.ensureStartSymbolEndSymbol(seq);
			viterbiPaths.put(sseq, vi.viterbi(hmm, sseq).getSecond());
		}
		Probability prob;
		Probability newProb =
				this.getConditionalProbabilityOfModel(hmm, viterbiPaths);
		logger.info("P(M|S) = " + newProb);
		do {
			// System.gc();
			prob = newProb;
			Pair<Probability, Map<List<T>, List<Integer>>> r =
					this.performMerge(hmm, viterbiPaths);
			newProb = r.getFirst();
			logger.info("P(M|S) = " + newProb);
			viterbiPaths = r.getSecond();
			this.prior.reset();
			// System.err.print(".");
		} while (newProb.getLogProbability() > prob.getLogProbability());
		// System.err.println();
		this.viterbiPaths = viterbiPaths;
	}

	protected Pair<Probability, Map<List<T>, List<Integer>>> performMerge(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences, final Integer s1,
			final Integer s2) {
		logger.info("Merging states " + s1 + " and " + s2);
		this.mergeHistory.add(new Pair<Integer, Integer>(s1, s2));

		hmm.merge(s1, s2);

		for (Entry<List<T>, List<Integer>> entry : sequences.entrySet()) {
			replace(entry.getValue(), s2, s1);
		}
		this.prior.reset();
		return new Pair<Probability, Map<List<T>, List<Integer>>>(
				this.getConditionalProbabilityOfModel(hmm, sequences),
				sequences);
	}

	/**
	 * This method performs the single merge who gives the highest conditional
	 * probability of the model. Depending on the setting of {@link #greedy},
	 * the method searches the full merge space or stops with the first positive
	 * merge.
	 * 
	 * @param sequences
	 *            A map in which each observation sequence is mapped onto its
	 *            most likely state sequence.
	 * @return A pair of a probability and a new map, reflecting state merges.
	 */
	protected Pair<Probability, Map<List<T>, List<Integer>>> performMerge(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences) {
		Probability p = this.getConditionalProbabilityOfModel(hmm, sequences);
		Pair<Integer, Integer> maxPair = null;

		Iterator<Pair<Integer, Integer>> pairIterator =
				new PairIterator(hmm, new StateMergeFilter(hmm));
		while (pairIterator.hasNext()) {
			Pair<Integer, Integer> pair = pairIterator.next();
			logger.trace("Evaluating state merge pair {}", pair);
			Integer s1 = pair.getFirst();
			Integer s2 = pair.getSecond();

			Probability d =
					this.getConditionalProbabilityOfModel(hmm, sequences, s1,
							s2);// this.performTestMerge(sequences, s1,
			// s2);

			if (d.getLogProbability() > p.getLogProbability()) {

				p = d;
				if (maxPair == null) {
					maxPair = new Pair<Integer, Integer>(s1, s2);
				} else {

					maxPair = new Pair<Integer, Integer>(s1, s2);
				}

			}
		}

		if (maxPair != null) {
			return this.performMerge(hmm, sequences, maxPair.getFirst(),
					maxPair.getSecond());
		}
		return new Pair<Probability, Map<List<T>, List<Integer>>>(
				this.getConditionalProbabilityOfModel(hmm, sequences),
				sequences);
	}

	/**
	 * Returns the conditional probability <code>P(M|S) ≃ P(M) * P(S|M)</code>,
	 * with <code>M</code> being the model and <code>S</code> a collection of
	 * sequences of states and events (viterbi paths)
	 * 
	 * @param hmm
	 *            The hidden markov model
	 * @param sequences
	 *            A map, associating each list of events a list of states
	 * @return The probability
	 */
	protected Probability getConditionalProbabilityOfModel(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences) {
		Probability p_m = this.prior.getModelProbability(hmm);
		Probability p_s = this.getSequencesProbability(hmm, sequences);
		return PMath.multiply(p_m, p_s);

	}

	/**
	 * As
	 * {@link #getConditionalProbabilityOfModel(SEHiddenMarkovModel_impl, Map)},
	 * but under the assumption that states s1 and s2 are merged
	 * 
	 * @param hmm
	 *            The hidden markov model
	 * @param sequences
	 *            A map, associating each list of events a list of states
	 * @param s1
	 *            State
	 * @param s2
	 *            State
	 * @return The probability
	 */
	protected Probability getConditionalProbabilityOfModel(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences, final Integer s1,
			final Integer s2) {
		Probability p_m = this.prior.getModelProbability(hmm, s1, s2);
		Probability p_s = this.getSequencesProbability(hmm, sequences, s1, s2);
		return PMath.multiply(p_m, p_s);

	}

	/**
	 * Returns the conditional probability <code>P(M|S) ≃ P(M) * P(S|M)</code>,
	 * with <code>M</code> being the model and <code>S</code> a collection of
	 * sequences.
	 * 
	 * @param hmm
	 * @param sequences
	 * @return
	 */
	public Probability getConditionalProbabilityOfModel(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Collection<List<T>> sequences) {
		Probability p_m = this.prior.getModelProbability(hmm);
		Probability p_s = this.getSequencesProbability(hmm, sequences);
		return PMath.multiply(p_m, p_s);

	}

	/**
	 * Calculates <code>P(S|M)</code>.
	 * 
	 * @param hmm
	 *            The model <code>M</code>
	 * @param seqs
	 *            The set of sequences <code>S</code>
	 * @return
	 */
	public Probability getSequencesProbability(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Collection<List<T>> seqs) {
		Probability d = Probability.ONE;
		for (List<T> seq : seqs) {
			d = PMath.multiply(d, hmm.p(seq));
		}
		return d;
	}

	/**
	 * This method calculates the probability of the given event sequences using
	 * the given state sequences.
	 * 
	 * @param hmm
	 *            The hidden markov model, <code>M</code>
	 * @param sequences
	 *            Contains a mapping of event to state sequences,
	 *            <code>V(S)</code>.
	 * @return <code>P(V(S)|M)</code>
	 */
	protected Probability getSequencesProbability(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences) {
		Probability d = Probability.ONE;
		for (Entry<List<T>, List<Integer>> entry : sequences.entrySet()) {
			d = PMath.multiply(d, hmm.p(entry.getValue(), entry.getKey()));
		}
		return d;
	}

	/**
	 * As {@link #getSequencesProbability(SEHiddenMarkovModel_impl, Map)}, but
	 * with the assumption that z1 and z2 are merged
	 * 
	 * @param hmm
	 *            The hidden markov model
	 * @param sequences
	 *            A map of event sequences to state sequences
	 * @param z1
	 *            State
	 * @param z2
	 *            State
	 * @return The probability
	 */
	protected Probability getSequencesProbability(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences, final Integer z1,
			final Integer z2) {
		Probability d = Probability.ONE;
		for (Entry<List<T>, List<Integer>> entry : sequences.entrySet()) {
			d =
					PMath.multiply(d,
							hmm.p(entry.getValue(), entry.getKey(), z1, z2));
		}
		return d;
	}

	/**
	 * @return the viterbiPaths
	 */
	public Map<List<T>, List<Integer>> getViterbiPaths() {
		return viterbiPaths;
	}

	/**
	 * @return the prior
	 */
	public Prior<T> getPrior() {
		return prior;
	}

	/**
	 * @param prior
	 *            the prior to set
	 */
	public void setPrior(final Prior<T> prior) {
		this.prior = prior;
	}

	public SEHiddenMarkovModel_impl<T> train1(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Iterator<List<T>> trainingInstances) {
		Map<List<T>, List<Integer>> viterbiPaths =
				new HashMap<List<T>, List<Integer>>();
		Viterbi<T> vi = new ParallelViterbi2<T>();
		while (trainingInstances.hasNext()) {
			List<T> seq =
					hmm.ensureStartSymbolEndSymbol(trainingInstances.next());
			viterbiPaths.put(seq, vi.viterbi(hmm, seq).getSecond());
		}
		this.performMerge(hmm, viterbiPaths);
		return hmm;
	}

	@Override
	public SEHiddenMarkovModel_impl<T> init(
			final Iterator<List<T>> trainingInstances) {
		SEHiddenMarkovModel_impl<T> hmm = new SEHiddenMarkovModel_impl<T>();
		List<List<T>> sequences = new LinkedList<List<T>>();
		while (trainingInstances.hasNext()) {
			List<T> seq = trainingInstances.next();
			hmm.init(seq);
			sequences.add(seq);
		}

		return hmm;
	}

	@Override
	public SEHiddenMarkovModel_impl<T> train(
			final Iterator<List<T>> trainingInstances) {
		SEHiddenMarkovModel_impl<T> hmm = new SEHiddenMarkovModel_impl<T>();
		hmm.setProperty("Trainer", this.toString());
		long time = System.nanoTime();
		hmm.setEventSimilarity(getEventSimilarity());
		Collection<List<T>> seqs = new LinkedList<List<T>>();
		while (trainingInstances.hasNext()) {
			List<T> seq = new LinkedList<T>(trainingInstances.next());
			seqs.add(seq);
			hmm.init(seq);
		}
		this.performMerges(hmm, seqs);
		long now = System.nanoTime();
		long duration = now - time;
		hmm.setProperty("Training time", String.valueOf(duration));
		hmm.setProperty("Training time (s)", String.valueOf(duration * 1E-9));
		hmm.setProperty("Training date", new Date().toString());
		return hmm;
	}

	/**
	 * @return the eventSimilarity
	 */
	public SimilarityFunction<T> getEventSimilarity() {
		return eventSimilarity;
	}

	/**
	 * @param eventSimilarity
	 *            the eventSimilarity to set
	 */
	public void setEventSimilarity(final SimilarityFunction<T> eventSimilarity) {
		this.eventSimilarity = eventSimilarity;
	}

	@Override
	public TrainingConfiguration getTrainingConfiguration() {
		return this.configuration;
	};

	public static <T> T[] replace(final T[] arr, final T arg0, final T arg1) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(arg0)) {
				arr[i] = arg1;
			}
		}
		return arr;
	}

	public static <T> List<T>
	replace(final List<T> list, final T e1, final T e2) {
		while (list.indexOf(e1) >= 0) {
			list.set(list.indexOf(e1), e2);
		}
		return list;
	}

}
