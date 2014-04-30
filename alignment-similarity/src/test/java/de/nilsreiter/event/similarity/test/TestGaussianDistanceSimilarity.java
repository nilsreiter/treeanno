package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.similarity.GaussianDistanceSimilarity;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class TestGaussianDistanceSimilarity {
	Document[] rds = new Document[3];
	File fnhome;
	FrameEventFactory frameEventFactory;

	@Before
	public void setUp() throws Exception {
		DataReader dr = new DataReader();
		rds[2] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0003.xml").getFile()));
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
	public void testGaussianDistanceSimilarity() {

		GaussianDistanceSimilarity sf = new GaussianDistanceSimilarity();
		assertEquals(1.0,
				sf.sim(rds[2].getEvents().get(0), rds[2].getEvents().get(0))
						.getProbability(), 0.0001);
		assertEquals(0.999834,
				sf.sim(rds[2].getEvents().get(0), rds[2].getEvents().get(1))
						.getProbability(), 0.0001);

		Probability minSim = sf.sim(
				rds[2].getEvents().get(rds[2].getEvents().size() - 1), rds[2]
						.getEvents().get(0));
		assertEquals(4.080642E-6, minSim.getProbability(), 1E-8);
		assertEquals(
				0.936,
				sf.sim((Event) rds[2].getById("ev11"),
						(Event) rds[2].getById("ev31")).getProbability(),
				0.0001);

	}
}
