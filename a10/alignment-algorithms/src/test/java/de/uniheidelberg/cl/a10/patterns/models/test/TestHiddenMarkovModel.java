package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.ProbabilityDistribution;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;

public class TestHiddenMarkovModel {

	List<Object> seq1;
	List<Object> seq2;

	Object a, b, c;

	@Before
	public void setUp() throws Exception {
		a = mock(Object.class);
		when(a.toString()).thenReturn("a");
		b = mock(Object.class);
		when(b.toString()).thenReturn("b");
		c = mock(Object.class);
		when(c.toString()).thenReturn("c");
		seq1 = Arrays.asList(a, b, a, b);
		seq2 = Arrays.asList(a, b);

	}

	@Test
	public void testHMM() {

		HiddenMarkovModel_impl<Object> hmm = new HiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);

		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(Arrays.asList(a, b)).getProbability(), 0.0);
		assertEquals(
				0.25,
				PMath.multiply(hmm.p(Arrays.asList(a, b)),
						hmm.p(Arrays.asList(a, b, a, b))).getProbability(), 0.0);

	}

	@Test
	public void testGetProbability() {

		HiddenMarkovModel_impl<Object> hmm = new HiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);
		assertEquals(0.5, hmm.p(Arrays.asList(4, 5), seq2).getProbability(),
				0.0);
		assertEquals(0.5, hmm.p(Arrays.asList(0, 1, 2, 3), seq1)
				.getProbability(), 0.0);
	}

	@Test
	public void testMerging() {

		HiddenMarkovModel_impl<Object> hmm = new HiddenMarkovModel_impl<Object>();
		hmm.init(seq2);
		hmm.init(seq1);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		hmm.merge(0, 2);
		assertEquals(0.5, hmm.p(seq2).getProbability(), 0.0);
		assertEquals(0.5, hmm.p(seq1).getProbability(), 0.0);
		assertEquals(0.0, hmm.p(Arrays.asList(b, a)).getProbability(), 0.0);
	}

	@Test
	public void testSoftDrink() {
		MapMatrix<Integer, Integer, Probability> tp = new MapMatrix<Integer, Integer, Probability>();
		tp.put(0, 1, Probability.fromProbability(0.3));
		tp.put(0, 0, Probability.fromProbability(0.7));
		tp.put(1, 1, Probability.fromProbability(0.5));
		tp.put(1, 0, Probability.fromProbability(0.5));

		MapMatrix<Integer, String, Probability> ep = new MapMatrix<Integer, String, Probability>();
		ep.put(0, "cola", Probability.fromProbability(0.6));
		ep.put(0, "ice_t", Probability.fromProbability(0.1));
		ep.put(0, "lem", Probability.fromProbability(0.3));
		ep.put(1, "cola", Probability.fromProbability(0.1));
		ep.put(1, "ice_t", Probability.fromProbability(0.7));
		ep.put(1, "lem", Probability.fromProbability(0.2));

		ProbabilityDistribution<Integer> sp = new ProbabilityDistribution<Integer>();
		sp.put(0, 1.0);

		HashSet<Integer> states = new HashSet<Integer>();
		states.add(0);
		states.add(1);

		HiddenMarkovModel_impl<String> hmm = new HiddenMarkovModel_impl<String>();
		hmm.setTransitionProbabilities(tp);
		hmm.setEmissionProbabilities(ep);
		hmm.setStartingProbabilities(sp);
		hmm.setStates(states);
		hmm.setFinalStates(states);

		assertEquals(0.084, hmm.p(Arrays.asList("lem", "ice_t"))
				.getProbability(), 0.0001);
		assertEquals(0.162, hmm.p(Arrays.asList("cola", "lem"))
				.getProbability(), 0.0001);

	}

}
