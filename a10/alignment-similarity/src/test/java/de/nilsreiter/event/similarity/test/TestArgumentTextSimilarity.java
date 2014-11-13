package de.nilsreiter.event.similarity.test;

import static de.nilsreiter.event.impl.FrameEventFactory.event;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.similarity.ArgumentText;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;

public class TestArgumentTextSimilarity {

	Document[] rds = new Document[3];
	File fnhome;
	FrameEventFactory frameEventFactory;

	@Before
	public void setUp() throws Exception {
		// This only works on zwergdrossel
		fnhome = new File("/home/users0/reiterns/local/framenet-1.5");

		DataReader dr = new DataReader();
		rds[2] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0003.xml").getFile()));
		rds[0] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0009.xml").getFile()));
		rds[1] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0016.xml").getFile()));

		frameEventFactory = new FrameEventFactory();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = NullPointerException.class)
	public void testNullArguments() throws IncompatibleException {
		ArgumentText sf = new ArgumentText();
		sf.sim(null, event(rds[2].getFrameById("f1")));
	}

	@Test
	public void testArgumentTextSimilarity() throws IncompatibleException {
		assertNotNull(rds[2]);

		ArgumentText sf = new ArgumentText();

		assertEquals(0.6666,
				sf.sim(rds[2].getEventById("ev1"), rds[2].getEventById("ev0"))
						.getProbability(), 0.0001);

		assertEquals(0.5,
				sf.sim(rds[0].getEventById("ev5"), rds[1].getEventById("ev6"))
						.getProbability(), 0.0001);

	}

}
