package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.models.ForwardAlgorithm;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class TestForwardAlgorithm {
	List<Object> seq1;
	List<Object> seq2;
	List<Object> seq3;

	Object a, b, c;

	HiddenMarkovModel_impl<Object> hmm;
	SEHiddenMarkovModel_impl<Object> sehmm;

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
		seq3 = Arrays.asList(c, b);

		hmm = new HiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);
		hmm.init(seq3);
	}

	@Test
	public void testForwardAlgorithmWithHMM() {
		ForwardAlgorithm<Object> tr = new ForwardAlgorithm<Object>();

		assertEquals(0.33333, tr.p(hmm, seq1).getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(hmm, seq2).getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(hmm, seq3).getProbability(), 0.0001);
		assertEquals(0.0, tr.p(hmm, Arrays.asList(c, a)).getProbability(),
				0.0001);
		hmm.merge(0, 4);
		hmm.merge(1, 5);
		assertEquals(0.66666, tr.p(hmm, seq1).getProbability(), 0.0001);
		assertEquals(0.66666, tr.p(hmm, seq2).getProbability(), 0.0001);
		assertEquals(0.33333, tr.p(hmm, seq3).getProbability(), 0.0001);
		assertEquals(0.0, tr.p(hmm, Arrays.asList(c, a)).getProbability(),
				0.0001);
	}

}
