package de.uniheidelberg.cl.a10.cluster.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.cluster.IDocumentSimilarityFunction;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.Modularity;
import de.uniheidelberg.cl.a10.cluster.impl.Partition_impl;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class TestModularity {
	IPartition<String> clustering;
	IDocumentSimilarityFunction<String, Probability> sim;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		clustering = new Partition_impl<String>(Arrays.asList(
				Arrays.asList("a", "b", "c"), Arrays.asList("d", "e")));
		sim = mock(IDocumentSimilarityFunction.class);
		when(sim.getSimilarity(anyString(), anyString())).thenReturn(
				Probability.NULL);
		when(sim.getSimilarity("a", "b")).thenReturn(
				Probability.fromProbability(0.1));
		when(sim.getSimilarity("a", "d")).thenReturn(
				Probability.fromProbability(0.1));
		when(sim.getSimilarity("a", "c")).thenReturn(
				Probability.fromProbability(0.1));
	}

	@Test
	public void testGetModularity() {
		Modularity<String> m = new Modularity<String>(sim);
		assertEquals(0.1, m.getModularity(clustering), 0.0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testModularity() {
		Modularity<String> m = new Modularity<String>(
				new IDocumentSimilarityFunction<String, Probability>() {
					@Override
					public Probability getSimilarity(final String rd1,
							final String rd2) {
						if (rd1 == "C" || rd2 == "C")
							return Probability.NULL;
						return Probability.ONE;
					}
				});
		double r;

		clustering = new Partition_impl<String>(Arrays.asList(Arrays.asList(
				"A", "B", "C")));
		r = m.getModularityBrandes(clustering);
		assertEquals(0.0, r, 0.0);

		clustering = new Partition_impl<String>(Arrays.asList(
				Arrays.asList("A", "B"), Arrays.asList("C")));
		r = m.getModularityBrandes(clustering);
		assertEquals(0.0, r, 0.0);

		clustering = new Partition_impl<String>(Arrays.asList(
				Arrays.asList("A", "B"), Arrays.asList("C", "D")));
		r = m.getModularityBrandes(clustering);
		assertEquals(0.0, r, 0.0);

	}

}
