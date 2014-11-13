package de.uniheidelberg.cl.a10.patterns.models.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.PreciseMarkovModel_impl;

public class TestPreciseMarkovModel extends TestMarkovModel {
	PreciseMarkovModel_impl<Object> preciseMarkovModel;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		preciseMarkovModel = new PreciseMarkovModel_impl<Object>();
		preciseMarkovModel.learn(Arrays.asList(a, b, c, a));
		preciseMarkovModel.learn(Arrays.asList(b, b, c, f));
	}

	@Test
	public void testGetPaths() {
		assertTrue(preciseMarkovModel.getShortestPathsWithProbabilities()
				.keySet().contains(Arrays.asList(b, c, a)));
		assertEquals(0.5, preciseMarkovModel.p(a).getProbability(), 1e-5);
		assertEquals(0.5, preciseMarkovModel
				.getShortestPathsWithProbabilities().getHighestProbability()
				.getProbability(), 0.0);
	}

	@Test
	public void testLearn() {
		MarkovModel_impl<Object> mm = new PreciseMarkovModel_impl<Object>();
		mm.learn(Arrays.asList(a, b, c, a, c, f));
		assertEquals(0.5, mm.p(a, b).getProbability(), 0.0);
		assertEquals(0.0, mm.p(b, c).getProbability(), 0.0);
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);

		mm.learn(Arrays.asList(a, b));
		assertEquals(0.66666666, mm.getTransitionProbabilities().get(a, b)
				.getProbability(), 0.00000001);
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);
		mm.learn(Arrays.asList(a, b));
		assertEquals(0.75, mm.getTransitionProbabilities().get(a, b)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);

		mm = new MarkovModel_impl<Object>();
		mm.learn(Arrays.asList(a, b, c));
		mm.learn(Arrays.asList(f, b, d));
		assertEquals(1.0, mm.getStartingProbabilities().sum(), 0.0);
	}

	@Test
	public void testPreciseMerge() {
		MarkovModel_impl<Object> mm = new PreciseMarkovModel_impl<Object>();
		mm.learn(Arrays.asList(a, b, c, a));
		mm.learn(Arrays.asList(a, d, f, a));
		testMerge(mm);
	}

	@Test
	public void testSimulatedMerge() {
		MarkovModel_impl<Object> mm = new PreciseMarkovModel_impl<Object>();
		mm.learn(Arrays.asList(a, b, c, a));
		mm.learn(Arrays.asList(a, d, f, a));
		assertEquals(0.5, mm.p(Arrays.asList(a, b, c, a)).getProbability(), 0.0);
		assertEquals(0.5, mm.getTransitionProbabilities().get(a, b)
				.getProbability(), 0.0);
		assertEquals(0.5, mm.getConditionalProbability(b, a, b, c)
				.getProbability(), 0.0);
		assertEquals(0.5, mm.getConditionalProbability(b, a, c, b)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getConditionalProbability(d, f, b, c)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getConditionalProbability(c, a, c, f)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getConditionalProbability(c, a, f, c)
				.getProbability(), 0.0);
		assertEquals(0.0, mm.getConditionalProbability(a, c, c, f)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getConditionalProbability(a, b, b, d)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getConditionalProbability(a, b, d, b)
				.getProbability(), 0.0);
		assertEquals(1.0, mm.getConditionalProbability(d, f, c, f)
				.getProbability(), 0.0);
	}

}
