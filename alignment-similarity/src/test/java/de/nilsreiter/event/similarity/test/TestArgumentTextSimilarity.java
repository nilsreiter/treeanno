package de.nilsreiter.event.similarity.test;

import static de.nilsreiter.event.impl.FrameEventFactory.event;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.similarity.ArgumentTextSimilarity;
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

	@Test(expected = NullPointerException.class)
	public void testNullArguments() throws IncompatibleException {
		ArgumentTextSimilarity sf = new ArgumentTextSimilarity();
		sf.sim(null, event(rds[2].getFrameById("f1")));
	}

	@Test
	public void testArgumentTextSimilarity() throws IncompatibleException {
		assertNotNull(rds[2]);

		ArgumentTextSimilarity sf = new ArgumentTextSimilarity();

		assertEquals(
				0.6666,
				sf.sim(event(rds[2].getFrameById("f1")),
						event(rds[2].getFrameById("f0"))).getProbability(),
				0.0001);

		assertEquals(
				0.5,
				sf.sim(event(rds[0].getFrameById("f5")),
						event(rds[1].getFrameById("f6"))).getProbability(),
				0.0001);

	}

}
