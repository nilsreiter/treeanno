package de.nilsreiter.alignment.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.test.TestUtil;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public class TestAlgorithmFactory {
	AlgorithmFactory factory;
	Document[] documents;

	@Before
	public void setUp() {
		factory = new AlgorithmFactory();

		documents = new Document[2];
		documents[0] = TestUtil.getDocumentWithEvents("r0003", 10);
		documents[1] = TestUtil.getDocumentWithEvents("r0009", 10);
	}

	@Test
	public void testNeedlemanWunsch() {
		Configuration configuration = new BaseConfiguration();
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.NeedlemanWunsch");
		configuration.setProperty("database.url",
				"jdbc:mysql://localhost:3306/reiter");
		configuration.setProperty("database.username", "reiterns");
		configuration.setProperty("database.password", "bybNoaKni");
		configuration.setProperty(NeedlemanWunsch.PARAM_THRESHOLD, "0.5");
		configuration.setProperty(
				"similarity.de.nilsreiter.event.similarity.WordNet", "1.0");

		NeedlemanWunsch<Event> aa =
				(NeedlemanWunsch<Event>) factory.getAlgorithm(configuration);
		assertEquals(
				de.nilsreiter.alignment.algorithm.impl.NeedlemanWunsch_impl.class,
				aa.getClass());
		assertEquals("1.0*WordNet", aa.getSimilarityFunction().toString());
		assertNotNull(aa.align(documents[0].getEvents(),
				documents[1].getEvents()));
	}

	@Test
	public void testBayesianModelMerging() {
		Configuration configuration = new BaseConfiguration();
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.BayesianModelMerging");
		configuration.setProperty("database.url",
				"jdbc:mysql://localhost:3306/reiter");
		configuration.setProperty("database.username", "reiterns");
		configuration.setProperty("database.password", "bybNoaKni");
		configuration.setProperty(BayesianModelMerging.CONFIG_THRESHOLD, "0.5");
		configuration.setProperty(BayesianModelMerging.CONFIG_THREADED, false);
		configuration.setProperty(
				"similarity.de.nilsreiter.event.similarity.WordNet", "1.0");

		BayesianModelMerging<Event> aa =
				(BayesianModelMerging<Event>) factory
						.getAlgorithm(configuration);
		assertEquals(
				de.nilsreiter.alignment.algorithm.impl.BayesianModelMerging_impl.class,
				aa.getClass());
		assertEquals("1.0*WordNet", aa.getSimilarityFunction().toString());
		Alignment<Event> alignment =
				aa.align(documents[0].getEvents(), documents[1].getEvents());
		assertNotNull(alignment);
		assertEquals(20, alignment.getAlignments().size());
	}

	@Test
	public void testMRSystem() {
		Configuration configuration = new BaseConfiguration();
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.MRSystem");
		configuration.setProperty("database.url",
				"jdbc:mysql://localhost:3306/reiter");
		configuration.setProperty("database.username", "reiterns");
		configuration.setProperty("database.password", "bybNoaKni");
		configuration.setProperty(MRSystem.CONFIG_THRESHOLD, "0.5");
		configuration.setProperty(
				"similarity.de.nilsreiter.event.similarity.WordNet", "1.0");

		MRSystem<Event> aa =
				(MRSystem<Event>) factory.getAlgorithm(configuration);
		assertEquals("1.0*WordNet", aa.getSimilarityFunction().toString());
		assertEquals(
				de.nilsreiter.alignment.algorithm.impl.MRSystem_impl.class,
				aa.getClass());
		Alignment<Event> alignment =
				aa.align(documents[0].getEvents(), documents[1].getEvents());
		assertNotNull(alignment);
		assertEquals(11, alignment.getAlignments().size());
	}
}
