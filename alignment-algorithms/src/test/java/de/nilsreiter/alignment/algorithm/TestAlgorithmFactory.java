package de.nilsreiter.alignment.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.test.TestUtil;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public class TestAlgorithmFactory {
	AlgorithmFactory factory;
	Document[] documents;
	Configuration configuration;

	@Before
	public void setUp() {
		factory = new AlgorithmFactory();

		documents = new Document[2];
		documents[0] = TestUtil.getDocumentWithEvents("r0003", 10);
		documents[1] = TestUtil.getDocumentWithEvents("r0009", 10);

		configuration = new BaseConfiguration();
		configuration.setProperty("database.url",
				"jdbc:mysql://localhost:3306/reiter");
		configuration.setProperty("database.username", "reiterns");
		configuration.setProperty("database.password", "bybNoaKni");
	}

	@Test
	public void testNeedlemanWunsch() {
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.NeedlemanWunsch");
		configuration.setProperty("NeedlemanWunsch.threshold", "0.5");
		configuration.addProperty("NeedlemanWunsch.similarityFunctions",
				"de.nilsreiter.event.similarity.WordNet");
		configuration.addProperty("NeedlemanWunsch.weight", "1.0");

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
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.BayesianModelMerging");
		configuration.setProperty("BayesianModelMerging.threshold", "0.5");
		configuration.setProperty("BayesianModelMerging.threaded", false);
		configuration.addProperty("BayesianModelMerging.similarityFunctions",
				"de.nilsreiter.event.similarity.WordNet");
		configuration.addProperty("BayesianModelMerging.weight", "1.0");

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
		assertEquals(19, alignment.getAlignments().size());
	}

	@Test
	public void testMRSystem() {
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.MRSystem");
		configuration.setProperty("MRSystem.threshold", "0.5");
		configuration.addProperty("MRSystem.similarityFunctions",
				"de.nilsreiter.event.similarity.WordNet");
		configuration.addProperty("MRSystem.weight", "1.0");

		MRSystem<Event> aa =
				(MRSystem<Event>) factory.getAlgorithm(configuration);
		assertEquals("1.0*WordNet", aa.getSimilarityFunction().toString());
		assertEquals(
				de.nilsreiter.alignment.algorithm.impl.MRSystem_impl.class,
				aa.getClass());
		Alignment<Event> alignment =
				aa.align(documents[0].getEvents(), documents[1].getEvents());
		assertNotNull(alignment);
		assertEquals(15, alignment.getAlignments().size());
	}

	@Test
	public void testConfigurationReading() throws ConfigurationException {
		Configuration configuration =
				new HierarchicalINIConfiguration(getClass().getClassLoader()
						.getResource("configuration-bayesianmodelmerging.ini"));
		BayesianModelMerging<Event> aa =
				(BayesianModelMerging<Event>) factory
				.getAlgorithm(configuration);
		assertNotNull(aa);
		assertEquals("1.0*WordNet+0.5*FrameNet", aa.getSimilarityFunction()
				.toString());

	}
}
