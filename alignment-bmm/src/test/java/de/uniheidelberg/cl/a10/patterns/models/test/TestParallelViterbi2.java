package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.models.ParallelViterbi2;
import de.uniheidelberg.cl.a10.patterns.models.Viterbi;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class TestParallelViterbi2 {
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

		hmm = new HiddenMarkovModel_impl<Object>();
		hmm.init(seq1);
		hmm.init(seq2);

		sehmm = new SEHiddenMarkovModel_impl<Object>();
		sehmm.init(seq1);
		sehmm.init(seq2);

	}

	@Test
	public void testParallelViterbiForHMM() {
		Viterbi<Object> vi = new ParallelViterbi2<Object>();

		assertEquals(0.5, vi.viterbi(hmm, Arrays.asList(a, b, a, b)).getFirst()
				.getProbability(), 0.0);
		assertEquals(0.5, vi.viterbi(hmm, Arrays.asList(a, b)).getFirst()
				.getProbability(), 0.0);
		assertEquals(Arrays.asList(0, 1, 2, 3),
				vi.viterbi(hmm, Arrays.asList(a, b, a, b)).getSecond());
	}

	@Test
	public void testParallelViterbiForSEHMM() {
		Viterbi<Object> vi = new ParallelViterbi2<Object>();

		assertEquals(0.5,
				vi.viterbi(sehmm, Arrays.asList(null, a, b, a, b, null))
						.getFirst().getProbability(), 0.0);
		assertEquals(0.5, vi.viterbi(sehmm, Arrays.asList(null, a, b, null))
				.getFirst().getProbability(), 0.0);
		assertEquals(Arrays.asList(0, 1, 2, 3, 4, -1),
				vi.viterbi(sehmm, Arrays.asList(null, a, b, a, b, null))
						.getSecond());
	}
}
