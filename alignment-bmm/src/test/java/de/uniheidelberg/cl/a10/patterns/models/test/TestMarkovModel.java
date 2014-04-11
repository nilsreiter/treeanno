package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.models.OutOfVocabularyException;
import de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl;

public class TestMarkovModel {

	Object a, b, c, d, e, f;

	MarkovModel_impl<Object> markovModel;

	@Before
	public void setUp() throws Exception {
		a = mock(Object.class);
		when(a.toString()).thenReturn("a");
		b = mock(Object.class);
		when(b.toString()).thenReturn("b");
		c = mock(Object.class);
		when(c.toString()).thenReturn("c");
		d = mock(Object.class);
		when(d.toString()).thenReturn("d");
		e = mock(Object.class);
		when(e.toString()).thenReturn("e");
		f = mock(Object.class);
		when(f.toString()).thenReturn("f");

		markovModel = new MarkovModel_impl<Object>();
		markovModel.learn(Arrays.asList(a, b, c, a));
		markovModel.learn(Arrays.asList(b, b, c, f));

	}

	@Test
	public void testRemove() {
		assertEquals(0.33333333333, markovModel.getTransitionProbabilities()
				.get(b, b).getProbability(), 0.000001);
		assertEquals(0.66666666666, markovModel.getTransitionProbabilities()
				.get(b, c).getProbability(), 0.000001);
		markovModel.removeEdge(b, b);
		assertEquals(1.0, markovModel.getTransitionProbabilities().get(b, c)
				.getProbability(), 0.000001);
		assertEquals(0.0, markovModel.getTransitionProbabilities().get(b, b)
				.getProbability(), 0.000001);
	}

	public void testMerge(final MarkovModel_impl<Object> mm) {
		// System.out.println(mm);
		assertEquals(1.0, mm.p(a).getProbability(), 0.0);
		assertEquals(0.5, mm.p(a, b).getProbability(), 0.0);
		assertEquals(0.5, mm.p(a, d).getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(b, c)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(d, f)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(c, a)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(f, a)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);

		mm.mergeStates(b, d);
		// System.out.println(mm);
		assertEquals(1.0, mm.getTransitionProbabilities().get(a, b)
				.getProbability(), 0.0);
		assertEquals(0.0, mm.getTransitionProbabilities().get(a, d)
				.getProbability(), 0.0);
		assertEquals(0.5, mm.getTransitionProbabilities().get(b, c)
				.getProbability(), 0.0);
		assertEquals(0.5, mm.getTransitionProbabilities().get(b, f)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(c, a)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);

		mm.mergeStates(c, f);
		// System.out.println(mm);
		assertEquals(1.0, mm.getTransitionProbabilities().get(a, b)
				.getProbability(), 0.0);
		assertEquals(0.0, mm.getTransitionProbabilities().get(a, d)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(b, c)
				.getProbability(), 0.0);
		assertEquals(0.0, mm.getTransitionProbabilities().get(b, f)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(c, a)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);

		mm.mergeStates(a, b);
		// System.out.println(mm);
	}

	@Test
	public void testMerge() {
		MarkovModel_impl<Object> mm = new MarkovModel_impl<Object>();
		mm.learn(Arrays.asList(a, b, c, a));
		mm.learn(Arrays.asList(a, d, f, a));
		assertEquals(0.5, mm.getTransitionProbabilities().get(a, b)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getTransitionProbabilities().get(f, a)
				.getProbability(), 0.0);
		testMerge(mm);
	}

	@Test(expected = OutOfVocabularyException.class)
	public void testOutOfVocabularySingle() {
		markovModel.p(mock(Object.class));
	}

	@Test(expected = OutOfVocabularyException.class)
	public void testOutOfVocabularyAtEnd() {
		markovModel.p(a, mock(Object.class));
	}

	@Test(expected = OutOfVocabularyException.class)
	public void testOutOfVocabularyAtBegin() {
		markovModel.p(mock(Object.class), a);
	}

}
