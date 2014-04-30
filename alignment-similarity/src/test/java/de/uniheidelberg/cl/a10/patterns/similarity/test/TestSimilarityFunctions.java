package de.uniheidelberg.cl.a10.patterns.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.impl.FrameEvent_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.ArgumentTextSimilarity;
import de.uniheidelberg.cl.a10.patterns.similarity.FrameNetSimilarity;
import de.uniheidelberg.cl.a10.patterns.similarity.GaussianDistanceSimilarity;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.Levenshtein;
import de.uniheidelberg.cl.a10.patterns.similarity.MantraSimilarity;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.similarity.UnconfiguredException;
import de.uniheidelberg.cl.a10.patterns.similarity.VerbNetSimilarity;
import de.uniheidelberg.cl.a10.patterns.similarity.WordNetSimilarity;

public class TestSimilarityFunctions {
	Document rd = null;
	Document[] rds = new Document[2];
	SimilarityConfiguration sConf;

	@Before
	public void setUp() throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException {
		// Main.initProperties();
		DataReader dr = new DataReader();
		rd = dr.read(new File(this.getClass().getResource("/r0003.xml")
				.getFile()));
		rds[0] = dr.read(new File(this.getClass().getResource("/r0009.xml")
				.getFile()));
		rds[1] = dr.read(new File(this.getClass().getResource("/r0016.xml")
				.getFile()));

		sConf = new SimilarityConfiguration();
		sConf.sf_arg_idf = true;
		sConf.saveHistory = false;

	}

	@Test(expected = UnconfiguredException.class)
	public void testUnconfiguredArgumentTextSimilarity()
			throws IncompatibleException {
		ArgumentTextSimilarity sf = new ArgumentTextSimilarity();
		assertEquals(Probability.ONE, sf.sim(
				new FrameEvent_impl(rd.getFrameById("f0")),
				new FrameEvent_impl(rd.getFrameById("f0"))));
	}

	@Test
	public void testAtomicArgumentTextSimilarity() {
		ArgumentTextSimilarity sf = new ArgumentTextSimilarity();
		sf.readConfiguration(sConf);
		assertEquals(1.0, sf
				.sim(new String[] { "the" }, new String[] { "the" })
				.getProbability(), 0.0);
		assertEquals(0.0, sf
				.sim(new String[] { "the" }, new String[] { "dog" })
				.getProbability(), 0.0);
		assertEquals(1.0,
				sf.sim(new String[] { "mantra" }, new String[] { "mantra" })
						.getProbability(), 0.0);
		assertEquals(
				0.1089,
				sf.sim(new String[] { "the", "dog" },
						new String[] { "the", "cat" }).getProbability(), 0.0001);
		assertEquals(
				0.8587,
				sf.sim(new String[] { "a", "dog" },
						new String[] { "the", "dog" }).getProbability(), 0.0001);

		sConf.sf_arg_idf = false;
		sf.readConfiguration(sConf);
		assertEquals(1.0, sf
				.sim(new String[] { "the" }, new String[] { "the" })
				.getProbability(), 0.0);
		assertEquals(0.0, sf
				.sim(new String[] { "the" }, new String[] { "dog" })
				.getProbability(), 0.0);
		assertEquals(1.0,
				sf.sim(new String[] { "mantra" }, new String[] { "mantra" })
						.getProbability(), 0.0);
		assertEquals(
				0.5,
				sf.sim(new String[] { "the", "dog" },
						new String[] { "the", "cat" }).getProbability(), 0.0001);
		assertEquals(
				0.5,
				sf.sim(new String[] { "a", "dog" },
						new String[] { "the", "dog" }).getProbability(), 0.0001);
	}

	@Test
	public void testArgumentTextSimilarity() throws IncompatibleException {
		assertNotNull(rd);

		ArgumentTextSimilarity sf = new ArgumentTextSimilarity();
		sf.readConfiguration(sConf);

		assertEquals(Probability.NULL,
				sf.sim(null, new FrameEvent_impl(rd.getFrameById("f0"))));
		assertEquals(
				0.68729,
				sf.sim(new FrameEvent_impl(rd.getFrameById("f1")),
						new FrameEvent_impl(rd.getFrameById("f0")))
						.getProbability(), 0.0001);

		assertEquals(
				0.01479,
				sf.sim(new FrameEvent_impl(rds[0].getFrameById("f5")),
						new FrameEvent_impl(rds[1].getFrameById("f6")))
						.getProbability(), 0.0001);

		for (Frame frame : rds[0].getFrames()) {
			assertEquals(rds[0].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
		for (Frame frame : rds[1].getFrames()) {
			assertEquals(rds[1].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}

		sConf.sf_arg_idf = false;
		sf.readConfiguration(sConf);

		assertEquals(Probability.NULL,
				sf.sim(null, new FrameEvent_impl(rd.getFrameById("f0"))));
		assertEquals(
				0.66666,
				sf.sim(new FrameEvent_impl(rd.getFrameById("f1")),
						new FrameEvent_impl(rd.getFrameById("f0")))
						.getProbability(), 0.0001);

		assertEquals(
				0.07143,
				sf.sim(new FrameEvent_impl(rds[0].getFrameById("f5")),
						new FrameEvent_impl(rds[1].getFrameById("f6")))
						.getProbability(), 0.0001);

		for (Frame frame : rds[0].getFrames()) {
			assertEquals(rds[0].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
		for (Frame frame : rds[1].getFrames()) {
			assertEquals(rds[1].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
	}

	public void expFrameNetSimilarity() throws FileNotFoundException,
			SecurityException {
		FrameNetSimilarity sf = new FrameNetSimilarity();
		int maxDist = 0;
		for (de.saar.coli.salsa.reiter.framenet.Frame f1 : sf.getFrameNet()
				.getFrames()) {
			for (de.saar.coli.salsa.reiter.framenet.Frame f2 : sf.getFrameNet()
					.getFrames()) {
				Number dist = sf.getDistance(f1.getName(), f2.getName());
				if (dist != null)
					maxDist = Math.max(maxDist, dist.intValue());
			}
		}
		System.out.println(maxDist);
	}

	@Test
	public void testVerbNetSimilarity() throws IncompatibleException {
		assertNotNull(rd);
		FrameEvent_impl f1 = new FrameEvent_impl(rd.getFrames().get(0));
		FrameEvent_impl f2 = new FrameEvent_impl(rd.getFrames().get(1));
		FrameEvent_impl f3 = new FrameEvent_impl(rd.getFrames().get(2));
		FrameEvent_impl f4 = new FrameEvent_impl(rd.getFrames().get(3));
		SimilarityFunction<FrameTokenEvent> sf = new VerbNetSimilarity();
		assertEquals(1.0, sf.sim(f1, f1).getProbability(), 0.0);
		assertEquals(0.129240981405388, sf.sim(f1, f2).getProbability(), 0.0001);
		assertEquals(0.129240981405388, sf.sim(f1, f3).getProbability(), 0.0001);
		assertEquals(0.0, sf.sim(f1, f4).getProbability(), 0.0);
		assertEquals(
				0.129240981405388,
				sf.sim(new FrameEvent_impl(rd.getFrames().get(11)),
						new FrameEvent_impl(rd.getFrames().get(15)))
						.getProbability(), 0.0001);

		for (Frame frame : rds[0].getFrames()) {
			assertEquals(rds[0].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
		for (Frame frame : rds[1].getFrames()) {
			assertEquals(rds[1].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}

	}

	@Test
	public void testGaussianDistanceSimilarity() {
		assertNotNull(rd);
		GaussianDistanceSimilarity sf = new GaussianDistanceSimilarity();
		sf.readConfiguration(sConf);
		assertEquals(
				1.0,
				sf.sim(new FrameEvent_impl(rd.getFrames().get(0)),
						new FrameEvent_impl(rd.getFrames().get(0)))
						.getProbability(), 0.0001);
		assertEquals(
				0.999834,
				sf.sim(new FrameEvent_impl(rd.getFrames().get(0)),
						new FrameEvent_impl(rd.getFrames().get(1)))
						.getProbability(), 0.0001);

		Probability minSim = sf.sim(
				new FrameEvent_impl(rd.getFrames().get(
						rd.getFrames().size() - 1)), new FrameEvent_impl(rd
						.getFrames().get(0)));
		assertEquals(4.080642E-6, minSim.getProbability(), 1E-8);
		assertEquals(
				0.936,
				sf.sim(new FrameEvent_impl(rd.getFrameById("f11")),
						new FrameEvent_impl(rd.getFrameById("f31")))
						.getProbability(), 0.0001);
		for (Frame frame : rds[0].getFrames()) {
			assertEquals(rds[0].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
		for (Frame frame : rds[1].getFrames()) {
			assertEquals(rds[1].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
		/*
		 * for (double d = 0.0; d <= 1.0; d += 0.01) { System.out.println(d +
		 * ": " + sf.getProbability(d)); }
		 */
	}

	@Test
	public void testWordNetSimilarity() throws IncompatibleException,
			IOException {
		assertNotNull(rd);
		WordNetSimilarity sf = new WordNetSimilarity();
		assertEquals(
				1.0,
				sf.sim(new FrameEvent_impl(rd.getFrames().get(0)),
						new FrameEvent_impl(rd.getFrames().get(0)))
						.getProbability(), 0.0);
		assertEquals(
				0.5869,
				sf.sim(new FrameEvent_impl(rd.getFrames().get(13)),
						new FrameEvent_impl(rd.getFrames().get(15)))
						.getProbability(), 0.0001);
		assertEquals(
				0.2382,
				sf.sim(new FrameEvent_impl(rd.getFrames().get(0)),
						new FrameEvent_impl(rd.getFrames().get(1)))
						.getProbability(), 0.0001);
		for (Frame frame : rds[0].getFrames()) {
			assertEquals(rds[0].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}
		for (Frame frame : rds[1].getFrames()) {
			assertEquals(rds[1].getId() + " " + frame.getId(), Probability.ONE,
					sf.sim(new FrameEvent_impl(frame), new FrameEvent_impl(
							frame)));
		}

	}

	@Test
	public void testMantraSimilarity() throws Exception {
		assertNotNull(rd);
		SimilarityFunction<FrameTokenEvent> sf = new MantraSimilarity();
		sf.readConfiguration(sConf);
		assertEquals(Probability.NULL, sf.sim(
				new FrameEvent_impl(rds[0].getFrameById("f25")),
				new FrameEvent_impl(rds[0].getFrameById("f17"))));
		assertEquals(Probability.ONE, sf.sim(
				new FrameEvent_impl(rds[0].getFrameById("f25")),
				new FrameEvent_impl(rds[1].getFrameById("f21"))));
	}

	@Test
	public void testLevenshtein() throws IncompatibleException {
		assertNotNull(rd);
		SimilarityFunction<String> sf = new Levenshtein();
		assertEquals(1.0, sf.sim("abcde", "abcde").getProbability(), 0.0);
		assertEquals(0.6538,
				sf.sim("bhadraṃ karṇebhiḥ...", "bhadraṃ karṇebhiḥ śṛṇuyāma")
						.getProbability(), 0.0001);
		assertEquals(0.6538,
				sf.sim("bhadraṃ karṇebhiḥ", "bhadraṃ karṇebhiḥ śṛṇuyāma")
						.getProbability(), 0.0001);
	}
}
