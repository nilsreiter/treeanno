package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.similarity.VerbNetSimilarity;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class TestVerbNetSimilarity {
	Document[] rds = new Document[3];
	FrameEventFactory frameEventFactory;
	File nbPath;
	File slPath;

	@Before
	public void setUp() throws Exception {
		nbPath = new File("/home/users0/reiterns/local/nombank-1.0");
		slPath = new File("/home/users0/reiterns/local/semlink1.1");
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
	public void testVerbNetSimilarity() throws IncompatibleException {
		assertNotNull(rds[2]);

		Event f1 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(0));
		Event f2 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(1));
		Event f3 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(2));
		Event f4 = this.frameEventFactory.makeEvent(rds[2].getFrames().get(3));

		SimilarityFunction<Event> sf = new VerbNetSimilarity(nbPath, slPath);
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(0.129240981405388, sf.sim(f1, f2).getProbability(), 0.0001);
		assertEquals(0.129240981405388, sf.sim(f1, f3).getProbability(), 0.0001);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(
				0.129240981405388,
				sf.sim(frameEventFactory.makeEvent(rds[2].getFrames().get(11)),
						frameEventFactory.makeEvent(rds[2].getFrames().get(15)))
						.getProbability(), 0.0001);

	}

}
