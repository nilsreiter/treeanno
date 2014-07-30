package de.uniheidelberg.cl.a10.patterns.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.impl.FrameEvent_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.FrameNetSimilarity;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityCalculationException;

public class TestFrameNetSimilarity {
	Document[] rds = new Document[3];
	File fnhome;

	@Before
	public void setUp() throws Exception {
		// This only works on zwergdrossel
		fnhome = new File("/home/users0/reiterns/local/framenet-1.5/");

		DataReader dr = new DataReader();
		rds[2] =
				dr.read(new File(this.getClass().getResource("/r0003.xml")
						.getFile()));
		rds[0] =
				dr.read(new File(this.getClass().getResource("/r0009.xml")
						.getFile()));
		rds[1] =
				dr.read(new File(this.getClass().getResource("/r0016.xml")
						.getFile()));

	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testFrameNetSimilarity() throws FileNotFoundException,
	SecurityException, SimilarityCalculationException {
		assertNotNull(rds[2]);
		FrameEvent_impl f1 = new FrameEvent_impl(rds[2].getFrames().get(0));
		FrameEvent_impl f2 = new FrameEvent_impl(rds[2].getFrames().get(1));
		FrameEvent_impl f3 = new FrameEvent_impl(rds[2].getFrames().get(2));
		FrameEvent_impl f4 = new FrameEvent_impl(rds[2].getFrames().get(3));
		FrameNetSimilarity sf = new FrameNetSimilarity(fnhome);
		assertEquals(0.5, sf.sim("Filling", "Placing").getProbability(), 0.0);
		assertEquals(0.5, sf.sim("Placing", "Filling").getProbability(), 0.0);
		assertEquals(1.0, sf.sim("Placing", "Placing").getProbability(), 0.0);
		assertEquals(1.0, sf.sim("Rite", "Rite").getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.25, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(
				0.5,
				sf.sim(new FrameEvent_impl(rds[2].getFrames().get(11)),
						new FrameEvent_impl(rds[2].getFrames().get(15)))
						.getProbability(), 0.0);

		sf = new FrameNetSimilarity(fnhome, "Inheritance");
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals("Cause_fluidic_motion", rds[2].getFrames().get(11)
				.getFrameName());
		assertEquals("Placing", rds[2].getFrames().get(15).getFrameName());
		assertEquals(0.25, sf.sim("Cause_fluidic_motion", "Placing")
				.getProbability(), 0.0);
		assertEquals(
				0.25,
				sf.sim(new FrameEvent_impl(rds[2].getFrames().get(11)),
						new FrameEvent_impl(rds[2].getFrames().get(15)))
						.getProbability(), 0.0);

		sf = new FrameNetSimilarity(fnhome);
		sf.setRelations(new HashSet<String>());
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f2).getProbability(), 0.0);
		assertEquals(1.0, sf.sim(f1, f3).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(
				0.0,
				sf.sim(new FrameEvent_impl(rds[2].getFrames().get(11)),
						new FrameEvent_impl(rds[2].getFrames().get(15)))
						.getProbability(), 0.0);
		Set<String> frameNames = new HashSet<String>();
		sf.setRelations(null);

		for (Frame frame : rds[0].getFrames()) {
			assertEquals(rds[0].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
			frameNames.add(frame.getFrameName());
		}
		for (Frame frame : rds[1].getFrames()) {
			assertEquals(rds[1].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
			frameNames.add(frame.getFrameName());
		}
		assertEquals(0.5, sf.sim("Being_wet", "Gradable_attributes")
				.getProbability(), 0.0);
		assertEquals(0.5, sf.sim("Gradable_attributes", "Being_wet")
				.getProbability(), 0.0);
		assertEquals(0.5, sf.sim("Intentionally_act", "Choosing")
				.getProbability(), 0.0);
		/*
		 * System.out.println(sf.sim("Rite", "Ingestion")); for (String fn1 :
		 * frameNames) { for (String fn2 : frameNames) {
		 * System.out.println("sim(" + fn1 + "," + fn2 + ")=" + sf.sim(fn1,
		 * fn2)); }
		 * 
		 * }
		 */
	}

}
