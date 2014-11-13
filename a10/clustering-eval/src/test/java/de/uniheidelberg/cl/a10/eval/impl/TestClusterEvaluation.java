package de.uniheidelberg.cl.a10.eval.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.cluster.impl.Cluster_impl;
import de.uniheidelberg.cl.a10.cluster.impl.Partition_impl;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluation;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluationFactory;
import de.uniheidelberg.cl.a10.eval.ClusterEvaluationStyle;

public class TestClusterEvaluation {
	Partition_impl<Object> gold;
	Partition_impl<Object>[] silver;

	Object[] items;

	ClusterEvaluation<Object> eval;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		int numberOfItems = 10;
		gold = new Partition_impl<Object>();
		silver = new Partition_impl[3];

		items = new Object[numberOfItems];

		for (int i = 0; i < numberOfItems; i++) {
			items[i] = mock(Object.class);
		}

		gold.add(new Cluster_impl<Object>(items[0], items[1], items[2]));
		gold.add(new Cluster_impl<Object>(items[3], items[4], items[5]));

		int s = 0;
		silver[s] = new Partition_impl<Object>();
		silver[s].add(new Cluster_impl<Object>(items[0], items[1]));
		silver[s].add(new Cluster_impl<Object>(items[2], items[3]));
		silver[s].add(new Cluster_impl<Object>(items[4], items[5]));

		s = 1;
		silver[s] = new Partition_impl<Object>();
		silver[s].add(new Cluster_impl<Object>(items[0], items[1], items[2]));
		silver[s].add(new Cluster_impl<Object>(items[3], items[4], items[5]));

		s = 2;
		silver[s] = new Partition_impl<Object>();
		silver[s].add(new Cluster_impl<Object>(items[0], items[1], items[2],
				items[3], items[4], items[5]));
	}

	@Test
	public void testRandIndex() {
		eval = ClusterEvaluationFactory
				.getClusterEvaluation(ClusterEvaluationStyle.Rand);
		assertEquals(0.66666, eval.evaluate(gold, silver[0]), 1e-5);
		eval = ClusterEvaluationFactory
				.getClusterEvaluation(ClusterEvaluationStyle.Rand2);
		assertEquals(0.66666, eval.evaluate(gold, silver[0]), 1e-5);
	}

	@Test
	public void testAdjustedRandIndex() {
		eval = ClusterEvaluationFactory
				.getClusterEvaluation(ClusterEvaluationStyle.AdjustedRand2);
		assertEquals(0.242424, eval.evaluate(gold, silver[0]), 1e-5);

	}

	@Test
	public void testVI() {
		eval = ClusterEvaluationFactory
				.getClusterEvaluation(ClusterEvaluationStyle.VI);
		assertEquals(1.25163, eval.evaluate(gold, silver[0]), 1e-5);
		assertEquals(0.0, eval.evaluate(gold, silver[1]), 1e-5);
		assertEquals(1.0, eval.evaluate(gold, silver[2]), 1e-5);

	}

	@Test
	public void testNVI() {
		eval = ClusterEvaluationFactory
				.getClusterEvaluation(ClusterEvaluationStyle.NVI);

		assertEquals(1.25162, eval.evaluate(gold, silver[0]), 1e-5);
		assertEquals(0.0, eval.evaluate(gold, silver[1]), 1e-5);
		assertEquals(1.0, eval.evaluate(gold, silver[2]), 1e-5);

	}
}