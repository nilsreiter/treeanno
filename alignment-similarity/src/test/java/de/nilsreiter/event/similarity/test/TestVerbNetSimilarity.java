package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.similarity.VerbNet;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityCalculationException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class TestVerbNetSimilarity extends TestBasics {
	File nbPath;
	File slPath;

	@Before
	public void setUp() throws Exception {
		nbPath = new File("/home/users0/reiterns/local/nombank-1.0");
		slPath = new File("/home/users0/reiterns/local/semlink1.1");

		super.init();

	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testVerbNetSimilarity() throws SimilarityCalculationException {
		assertNotNull(rds[2]);

		Event f1 = rds[2].getEvents().get(0);
		Event f2 = rds[2].getEvents().get(1);
		Event f3 = rds[2].getEvents().get(2);
		Event f4 = rds[2].getEvents().get(3);

		SimilarityFunction<Event> sf = new VerbNet(nbPath, slPath);
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(0.129240981405388, sf.sim(f1, f2).getProbability(), 0.0001);
		assertEquals(0.129240981405388, sf.sim(f1, f3).getProbability(), 0.0001);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.129240981405388,
				sf.sim(rds[2].getEvents().get(11), rds[2].getEvents().get(15))
				.getProbability(), 0.0001);

	}

}
