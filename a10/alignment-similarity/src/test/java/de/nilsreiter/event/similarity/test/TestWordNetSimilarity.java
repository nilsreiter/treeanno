package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.similarity.WordNet;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;

public class TestWordNetSimilarity extends TestBasics {

	File wnhome;
	File nbPath;

	@Before
	public void setUp() throws Exception {
		nbPath = new File("/home/users0/reiterns/local/nombank-1.0");
		wnhome = new File("/home/users0/reiterns/local/wordnet");
		super.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWordNetSimilarity() throws IncompatibleException,
			IOException {
		WordNet sf = new WordNet(wnhome, nbPath);
		assertEquals(1.0,
				sf.sim(rds[2].getEvents().get(0), rds[2].getEvents().get(0))
						.getProbability(), 0.0);
		assertEquals(0.5869,
				sf.sim(rds[2].getEvents().get(13), rds[2].getEvents().get(15))
						.getProbability(), 0.0001);
		assertEquals(0.2382,
				sf.sim(rds[2].getEvents().get(0), rds[2].getEvents().get(1))
						.getProbability(), 0.0001);

	}
}
