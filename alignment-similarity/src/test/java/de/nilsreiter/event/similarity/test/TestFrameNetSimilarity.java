package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.similarity.FrameNetSimilarity;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;

public class TestFrameNetSimilarity {
	Document[] rds = new Document[3];
	File fnhome;
	FrameEventFactory frameEventFactory;

	@Before
	public void setUp() throws Exception {
		// This only works on zwergdrossel
		fnhome = new File("/home/users0/reiterns/local/framenet-1.5");

		DataReader dr = new DataReader();
		rds[2] = dr.read(new File(this.getClass().getResource("/r0003.xml")
				.getFile()));
		rds[0] = dr.read(new File(this.getClass().getResource("/r0009.xml")
				.getFile()));
		rds[1] = dr.read(new File(this.getClass().getResource("/r0016.xml")
				.getFile()));

		frameEventFactory = new FrameEventFactory();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFrameNetSimilarity() throws FileNotFoundException,
			SecurityException, IncompatibleException {
		assertNotNull(rds[2]);

		Event f1 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(0));
		Event f2 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(1));
		Event f3 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(2));
		Event f4 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(3));
		FrameNetSimilarity sf = new FrameNetSimilarity(fnhome);
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.25, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(
				0.5,
				sf.sim(this.frameEventFactory.makeEvent(rds[2].getFrames().get(
						11)),
						this.frameEventFactory.makeEvent(rds[2].getFrames()
								.get(15))).getProbability(), 0.0);

		sf = new FrameNetSimilarity(fnhome, "Inheritance");
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals("Cause_fluidic_motion", rds[2].getFrames().get(11)
				.getFrameName());
		assertEquals("Placing", rds[2].getFrames().get(15).getFrameName());
		assertEquals(
				0.25,
				sf.sim(this.frameEventFactory.makeEvent(rds[2].getFrames().get(
						11)),
						this.frameEventFactory.makeEvent(rds[2].getFrames()
								.get(15))).getProbability(), 0.0);

		sf = new FrameNetSimilarity(fnhome);
		sf.setRelations(new HashSet<String>());
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(
				0.0,
				sf.sim(this.frameEventFactory.makeEvent(rds[2].getFrames().get(
						11)),
						this.frameEventFactory.makeEvent(rds[2].getFrames()
								.get(15))).getProbability(), 0.0);

	}
}
