package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.models.BackwardAlgorithm;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class TestBackwardAlgorithm {
	HiddenMarkovModel_impl<Object> hmm;
	SEHiddenMarkovModel_impl<Object> sehmm;

	Object a, b, c;

	@Before
	public void setUp() throws Exception {

		a = mock(Object.class);
		when(a.toString()).thenReturn("a");
		b = mock(Object.class);
		when(b.toString()).thenReturn("b");
		c = mock(Object.class);
		when(c.toString()).thenReturn("c");

		List<Object> seq1 = Arrays.asList(a, b, a, b);
		List<Object> seq2 = Arrays.asList(a, b);
		List<Object> seq3 = Arrays.asList(c, b);

		hmm = new HiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);
		hmm.init(seq3);

		sehmm = new SEHiddenMarkovModel_impl<Object>();
		sehmm.init(seq1);
		sehmm.init(seq2);
		sehmm.init(seq3);

	}

	@Test
	public void testBackwardAlgorithmWithHMM() {
		BackwardAlgorithm<Object> tr = new BackwardAlgorithm<Object>();

		assertEquals(0.33333, tr.p(hmm, Arrays.asList(a, b, a, b))
				.getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(hmm, Arrays.asList(a, b)).getProbability(),
				0.0001);
		assertEquals(0.33333, tr.p(hmm, Arrays.asList(c, b)).getProbability(),
				0.0001);
		assertEquals(0.0, tr.p(hmm, Arrays.asList(c, a)).getProbability(),
				0.0001);
		hmm.merge(0, 4);
		hmm.merge(1, 5);
		assertEquals(0.66666, tr.p(hmm, Arrays.asList(a, b, a, b))
				.getProbability(), 0.0001);
		assertEquals(0.66666, tr.p(hmm, Arrays.asList(a, b)).getProbability(),
				0.0001);
		assertEquals(0.33333, tr.p(hmm, Arrays.asList(c, b)).getProbability(),
				0.0001);
		assertEquals(0.0, tr.p(hmm, Arrays.asList(c, a)).getProbability(),
				0.0001);
	}

	@Test
	public void testBackwardAlgorithmWithSEHMM() {
		BackwardAlgorithm<Object> tr = new BackwardAlgorithm<Object>();

		assertEquals(0.33333, tr
				.p(sehmm, Arrays.asList(null, a, b, a, b, null))
				.getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(sehmm, Arrays.asList(null, a, b, null))
				.getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(sehmm, Arrays.asList(null, c, b, null))
				.getProbability(), 0.0001);
		assertEquals(0.0, tr.p(sehmm, Arrays.asList(null, c, a, null))
				.getProbability(), 0.0001);
		// System.out.println(sehmm);
		sehmm.merge(1, 5);
		sehmm.merge(2, 6);
		assertEquals(0.33333, tr
				.p(sehmm, Arrays.asList(null, a, b, a, b, null))
				.getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(sehmm, Arrays.asList(null, a, b, null))
				.getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(sehmm, Arrays.asList(null, c, b, null))
				.getProbability(), 0.0001);
		assertEquals(0.0, tr.p(sehmm, Arrays.asList(null, c, a, null))
				.getProbability(), 0.0001);
	}

}
