package de.uniheidelberg.cl.a10.cluster.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.cluster.IDocumentSimilarityFunction;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.impl.MinimumCut;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class TestMinimumCut {
	HasId[] documents;
	IDocumentSimilarityFunction<HasId, Probability> simFun;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		documents = new HasId[] { mock(HasId.class), mock(HasId.class),
				mock(HasId.class) };
		simFun = mock(IDocumentSimilarityFunction.class);
		when(simFun.getSimilarity(documents[0], documents[1])).thenReturn(
				Probability.fromProbability(0.8));
		when(simFun.getSimilarity(documents[1], documents[2])).thenReturn(
				Probability.fromProbability(0.1));
		when(simFun.getSimilarity(documents[0], documents[2])).thenReturn(
				Probability.fromProbability(0.2));
		when(simFun.getSimilarity(documents[1], documents[0])).thenReturn(
				Probability.fromProbability(0.8));
		when(simFun.getSimilarity(documents[2], documents[1])).thenReturn(
				Probability.fromProbability(0.05));
		when(simFun.getSimilarity(documents[2], documents[0])).thenReturn(
				Probability.fromProbability(0.2));
		when(simFun.getSimilarity(documents[0], documents[0])).thenReturn(
				Probability.ONE);
		when(simFun.getSimilarity(documents[1], documents[1])).thenReturn(
				Probability.ONE);
		when(simFun.getSimilarity(documents[2], documents[2])).thenReturn(
				Probability.ONE);
		for (int i = 0; i < documents.length; i++) {
			when(documents[i].getId()).thenReturn("d" + i);
			when(documents[i].toString()).thenReturn("d" + i);
		}
	}

	@Test
	public void testClustering() throws IOException {
		MinimumCut<HasId> mc = new MinimumCut<HasId>();
		mc.setDocumentSimilarityFunction(simFun);
		IPartition<HasId> partition = mc.cluster(Arrays.asList(documents));
		assertTrue(!partition.together(documents[2], documents[1]));
		assertTrue(partition.together(documents[1], documents[0]));
		assertEquals(2, partition.size());
		partition = mc.cluster(Arrays.asList(documents));
		assertTrue(!partition.together(documents[2], documents[1]));
		assertTrue(partition.together(documents[1], documents[0]));
		assertEquals(2, partition.size());
		// System.out.println(partition);
	}
}
