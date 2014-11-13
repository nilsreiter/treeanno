package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.similarity.FrameNet;
import de.uniheidelberg.cl.a10.data2.Event;

public class TestFrameNetSimilarity extends TestBasics {
	File fnhome;

	@Before
	public void setUp() throws Exception {
		// This only works on zwergdrossel
		fnhome = new File("/home/users0/reiterns/local/framenet-1.5");

		super.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFrameNetSimilarity() throws FileNotFoundException,
			SecurityException {
		assertNotNull(rds[2]);

		Event f1 = rds[2].getEvents().get(0);
		Event f2 = rds[2].getEvents().get(1);
		Event f3 = rds[2].getEvents().get(2);
		Event f4 = rds[2].getEvents().get(3);
		FrameNet sf = new FrameNet(fnhome);
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.25, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.5,
				sf.sim(rds[2].getEvents().get(11), rds[2].getEvents().get(15))
						.getProbability(), 0.0);

		sf = new FrameNet(fnhome, "Inheritance");
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals("Cause_fluidic_motion", rds[2].getFrames().get(11)
				.getFrameName());
		assertEquals("Placing", rds[2].getFrames().get(15).getFrameName());
		assertEquals(0.25,
				sf.sim(rds[2].getEvents().get(11), rds[2].getEvents().get(15))
						.getProbability(), 0.0);

		sf = new FrameNet(fnhome);
		sf.setRelations(new HashSet<String>());
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0,
				sf.sim(rds[2].getEvents().get(11), rds[2].getEvents().get(15))
						.getProbability(), 0.0);

	}
}
