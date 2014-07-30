package de.nilsreiter.alignment.algorithm;

import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Event;

public class TestAlgorithmFactory {
	AlgorithmFactory factory;

	@Before
	public void setUp() {
		factory = new AlgorithmFactory();
	}

	@Test
	public void test() {
		Configuration configuration = new BaseConfiguration();
		configuration.setProperty("alignment.algorithm",
				"de.nilsreiter.alignment.algorithm.NeedlemanWunsch");
		configuration.setProperty("database.url",
				"jdbc:mysql://localhost:3306/reiter");
		configuration.setProperty("database.username", "reiterns");
		configuration.setProperty("database.password", "bybNoaKni");
		configuration.setProperty(NeedlemanWunsch.PARAM_THRESHOLD, "0.5");
		configuration.setProperty(
				"similarity.de.nilsreiter.event.similarity.Null", "1.0");

		NeedlemanWunsch<Event> aa =
				(NeedlemanWunsch<Event>) factory.getAlgorithm(configuration);
		assertEquals(
				de.nilsreiter.alignment.algorithm.impl.NeedlemanWunsch_impl.class,
				aa.getClass());
		assertEquals("1.0*Null", aa.getSimilarityFunction().toString());

	}
}
