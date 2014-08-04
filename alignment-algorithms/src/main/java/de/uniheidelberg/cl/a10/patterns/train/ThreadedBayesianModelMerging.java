package de.uniheidelberg.cl.a10.patterns.train;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class ThreadedBayesianModelMerging<T extends HasDocument> extends
BayesianModelMerging<T> {

	int maximalNumberOfThreads = Integer.MAX_VALUE;

	protected ThreadedBayesianModelMerging(final BMMConfiguration conf,
			final Prior<T> prior) {
		super(conf, prior);
		/*
		 * if (Main.host == Main.Host.GILLESPIE) { this.maximalNumberOfThreads =
		 * Integer.MAX_VALUE; }
		 */
	}

	@Override
	public Pair<Probability, Map<List<T>, List<Integer>>> performMerge(
			final SEHiddenMarkovModel_impl<T> hmm,
			final Map<List<T>, List<Integer>> sequences) {
		Probability p = this.getConditionalProbabilityOfModel(hmm, sequences);
		Pair<Integer, Integer> maxPair = null;
		List<TestMergeThread> threads = new ArrayList<TestMergeThread>();

		Iterator<org.apache.commons.math3.util.Pair<Integer, Integer>> iterator =
				new PairIterator(hmm, new StateMergeFilter(hmm));

		Semaphore semaphore = new Semaphore(this.maximalNumberOfThreads);
		while (iterator.hasNext()) {
			Pair<Integer, Integer> pair = iterator.next();
			logger.finer("Evaluating state merge pair " + pair);
			Integer s1 = pair.getFirst();
			Integer s2 = pair.getSecond();
			if (this.prior.isCandidate(hmm, s1, s2)) {
				if (hmm.getMM().getPredecessors(s1)
						.equals(hmm.getMM().getPredecessors(s2))
						&& hmm.emissionsEqual(s1, s2)) {
					return this.performMerge(hmm, sequences, s1, s2);
				}
				TestMergeThread tmr =
						new TestMergeThread(hmm, sequences, s1, s2, semaphore);
				threads.add(tmr);
				try {
					semaphore.acquire();
					tmr.start();
				} catch (InterruptedException e) {}

			}
		}
		int counter = 0;
		for (TestMergeThread tmr : threads) {
			try {
				tmr.join();
				Probability d = tmr.getReturnValue();
				logger.finest(tmr.s1 + " and " + tmr.s2 + " would give " + d);
				if (d.getLogProbability() > p.getLogProbability()) {
					p = d;
					if (maxPair == null) {
						maxPair = new Pair<Integer, Integer>(tmr.s1, tmr.s2);
					} else {
						maxPair = new Pair<Integer, Integer>(tmr.s1, tmr.s2);
					}
				}
				counter++;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info(counter + " threads processed.");

		if (maxPair != null) {
			return this.performMerge(hmm, sequences, maxPair.getFirst(),
					maxPair.getSecond());
		}
		return new Pair<Probability, Map<List<T>, List<Integer>>>(
				this.getConditionalProbabilityOfModel(hmm, sequences),
				sequences);
	}

	class TestMergeThread extends Thread {
		SEHiddenMarkovModel_impl<T> hmm;
		Map<List<T>, List<Integer>> sequences;
		Integer s1;
		Integer s2;
		Probability returnValue;
		Semaphore sem;

		boolean kill = false;

		public TestMergeThread(final SEHiddenMarkovModel_impl<T> hmm,
				final Map<List<T>, List<Integer>> sequences, final Integer s1,
				final Integer s2, final Semaphore semaphore) {
			super();
			this.hmm = hmm;
			this.sequences = sequences;
			this.s1 = s1;
			this.s2 = s2;
			this.sem = semaphore;

		}

		@Override
		public void run() {
			this.returnValue =
					getConditionalProbabilityOfModel(hmm, sequences, s1, s2);
			sem.release();
			return;

		}

		/**
		 * @return the returnValue
		 */
		public Probability getReturnValue() {
			return returnValue;
		}

		/**
		 * @return the kill
		 */
		public boolean isKill() {
			return kill;
		}

		/**
		 * @param kill
		 *            the kill to set
		 */
		public void kill() {
			this.kill = true;
		}
	}

	/**
	 * @return the maximalNumberOfThreads
	 */
	public int getMaximalNumberOfThreads() {
		return maximalNumberOfThreads;
	}

	/**
	 * @param maximalNumberOfThreads
	 *            the maximalNumberOfThreads to set
	 */
	public void setMaximalNumberOfThreads(final int maximalNumberOfThreads) {
		this.maximalNumberOfThreads = maximalNumberOfThreads;
	}
}
