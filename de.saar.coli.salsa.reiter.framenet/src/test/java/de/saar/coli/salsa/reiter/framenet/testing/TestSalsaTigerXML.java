package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.STXDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.SalsaTigerXML;

public class TestSalsaTigerXML {

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();

	static String example4 = "/proj/rituals/annotation/salto-repository/master/r0025_Placing_merged_jom.nl.xml";

	File example1, example2, example3;

	@Before
	public void setUp() {
		example1 = new File(this.getClass()
				.getResource("/SalsaTigerExample-1.xml").getFile());
		example2 = new File(this.getClass()
				.getResource("/SalsaTigerExample-2.xml").getFile());
		example3 = new File(this.getClass()
				.getResource("/SalsaTigerExample-3.xml").getFile());
	}

	@Test
	public void testLoading() {
		FrameNet fn = new FrameNet();
		fn.readData(new STXDatabaseReader(example2));
		assertEquals(5, fn.getFrames().size());
		try {
			Frame frame = fn.getFrame("Placing");
			assertEquals(3, frame.frameElements().size());
			assertEquals("Goal", fn.getFrameElement("Placing.Goal").getName());
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (FrameElementNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCorpusData() {
		FrameNet fn = new FrameNet();
		SalsaTigerXML stx = new SalsaTigerXML(fn, Logger.getAnonymousLogger());
		try {
			stx.parse(example3);
			assertEquals(2, stx.getSentences().size());
			Sentence sentence = stx.getSentences().get(0);

			assertEquals(1, sentence.getRealizedFrames().size());
			IRealizedFrame rf = sentence.getRealizedFrames().get(0);
			assertEquals("throw", rf.getTarget().toString());
			assertEquals("Placing", rf.getFrame().getName());
			// assertEquals(0, rf.getStart());
			// assertEquals(0, rf.getEnd());
			assertEquals(3, rf.getFrameElements().size());
			assertEquals(9, sentence.getTokenList().size());

			assertEquals(
					sentence.getTokenList().get(0).toString(),
					sentence.getText().substring(
							sentence.getTokenList().get(0).getCharacterRange()
									.getElement1(),
							sentence.getTokenList().get(0).getCharacterRange()
									.getElement2()));
			assertEquals(
					sentence.getTokenList().get(5).toString(),
					sentence.getText().substring(
							sentence.getTokenList().get(5).getCharacterRange()
									.getElement1(),
							sentence.getTokenList().get(5).getCharacterRange()
									.getElement2()));
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		} catch (FrameElementNotFoundException e) {
			e.printStackTrace();
		}
	}

}
