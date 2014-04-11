package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class TestSEHiddenMarkovModel extends TestHiddenMarkovModel {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	@Test
	public void testHMM() {

		HiddenMarkovModel_impl<Object> hmm = new SEHiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);

		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(
				0.25,
				PMath.multiply(hmm.p(Arrays.asList(a, b)),
						hmm.p(Arrays.asList(a, b, a, b))).getProbability(), 0.0);

	}

	@Override
	@Test
	public void testMerging() {

		HiddenMarkovModel_impl<Object> hmm = new SEHiddenMarkovModel_impl<Object>();
		hmm.init(seq2);
		hmm.init(seq1);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		hmm.merge(2, 4);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		assertEquals(0.0, hmm.p(Arrays.asList(b, a)).getProbability(), 0.0);
	}

	@Test
	public void testCopy() {

		SEHiddenMarkovModel_impl<Object> hmmo = new SEHiddenMarkovModel_impl<Object>();
		hmmo.init(seq2);
		hmmo.init(seq1);
		HiddenMarkovModel_impl<Object> hmm = new SEHiddenMarkovModel_impl<Object>(
				hmmo);
		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(
				0.25,
				PMath.multiply(hmm.p(Arrays.asList(a, b)),
						hmm.p(Arrays.asList(a, b, a, b))).getProbability(), 0.0);
	}

	@Test
	public void testSimulatedMerge() {
		List<Integer> sseq1 = Arrays.asList(0, 1, 2, 3, 4, -1);
		List<Integer> sseq2 = Arrays.asList(0, 5, 6, -1);

		SEHiddenMarkovModel_impl<Object> hmm = new SEHiddenMarkovModel_impl<Object>();
		SEHiddenMarkovModel_impl<Object> hmmprime = new SEHiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);

		hmmprime.init(seq1);
		hmmprime.init(seq2);
		hmmprime.merge(2, 4);
		assertEquals(0.5, hmm.p(sseq1, seq1).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(sseq2, seq2).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(sseq2, seq2, 5, 1).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(sseq2, seq2, 1, 5).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(sseq2, seq2, 1, 3).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(sseq2, seq2, 3, 1).getProbability(), 0.0);

		assertEquals(0.125, hmm.p(sseq1, seq1, 1, 3).getProbability(), 0.0001);
		assertEquals(0.125, hmm.p(sseq1, seq1, 3, 1).getProbability(), 0.0001);

		assertEquals(0.125, hmmprime.p(Arrays.asList(0, 1, 2, 3, 2, -1), seq1)
				.getProbability(), 0.0001);
		assertEquals(0.5, hmmprime.p(Arrays.asList(0, 5, 6, -1), seq2)
				.getProbability(), 0.0001);

		assertEquals(0.125, hmm.p(sseq1, seq1, 2, 4).getProbability(), 0.0001);
		assertEquals(0.125, hmm.p(sseq1, seq1, 4, 2).getProbability(), 0.0001);
		assertEquals(0.25, hmm.p(sseq1, seq1, 2, 6).getProbability(), 0.0);
	}

	@Test
	public void testEventSimilarity() {
		List<Object> seq1 = Arrays.asList(a, c, a, c);
		SimilarityFunction<Object> sf = new SimilarityFunction<Object>() {
			@Override
			public Probability sim(final Object arg0, final Object arg1) {
				if (arg0 == null || arg1 == null)
					return Probability.NULL;
				if (arg0.equals(arg1))
					return Probability.ONE;
				if (arg0 == c && arg1 == b)
					return Probability.fromProbability(0.5);
				if (arg0 == b && arg1 == c)
					return Probability.fromProbability(0.5);
				return Probability.NULL;
			}

			@Override
			public String toString() {
				return "";
			}

			@Override
			public void readConfiguration(final SimilarityConfiguration tc) {
				// TODO Auto-generated method stub

			}

		};

		SEHiddenMarkovModel_impl<Object> hmm = new SEHiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);
		hmm.setEventSimilarity(sf);

		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(1.0, hmm.getProbability(2, c).getProbability(), 0.0);
		assertEquals(0.5, hmm.getProbability(2, b).getProbability(), 0.0);
		assertEquals(1.0, hmm.getProbability(6, b).getProbability(), 0.0);
		assertEquals(0.5, hmm.getProbability(6, c).getProbability(), 0.0);
		assertEquals(0.25, hmm.p(Arrays.asList(a, b, a, c)).getProbability(),
				0.0);
		assertEquals(0.25, hmm.p(Arrays.asList(a, c)).getProbability(), 0.0);

	}
}
