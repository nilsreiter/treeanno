package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.saar.coli.salsa.reiter.framenet.fncorpus.FrameNetCorpus;
import de.saar.coli.salsa.reiter.framenet.fncorpus.FrameNetCorpus13;
import de.uniheidelberg.cl.reiter.util.CharacterRange;

public class TestFNCorpus13 extends TestBase13 {

	File example1;

	@Before
	public void setUp() {

		URL url = this.getClass().getResource("/data/FNCorpus-example.xml");
		example1 = new File(url.getFile());
	}

	@Test
	public void testCorpusData() {
		try {
			FrameNetCorpus fnc = new FrameNetCorpus13(frameNet,
					Logger.getAnonymousLogger());
			fnc.parse(example1);
			assertEquals(4, fnc.getSentences().size());
			Sentence sentence = fnc.getSentences().get(1);

			assertEquals(5, sentence.getRealizedFrames().size());
			RealizedFrame rf = (RealizedFrame) sentence.getRealizedFrames()
					.iterator().next();
			assertEquals("interest", rf.getTarget().toString());
			assertEquals("Emotion_directed", rf.getFrame().getName());
			assertEquals(16, rf.getStart());
			assertEquals(24, rf.getEnd());
			assertEquals(2, rf.getFrameElements().size());
			assertEquals("Libya", rf.getFrameElements().get("Experiencer")
					.getTargetString(' '));
			Iterator<? extends IRealizedFrame> rfIter = fnc.getSentences()
					.get(2).getRealizedFrames().iterator();
			rfIter.next();
			rf = (RealizedFrame) rfIter.next();
			assertEquals("Using", rf.getFrame().getName());

			assertEquals(5, rf.frameElements().size());
			RealizedFrameElement rfe_instr = rf.getFrameElements().get(
					"Instrument");
			RealizedFrameElement rfe_role = rf.getFrameElements().get("Role");
			assertEquals(false, rfe_instr.isNullInstantiated());
			assertEquals(true, rfe_role.isNullInstantiated());
			assertEquals(true, rf.getFrameElements().get("Place")
					.isNullInstantiated());
			assertEquals("INI", rfe_role.getIType());
			assertEquals(-1, rfe_role.getStart());
			assertEquals(54, rfe_instr.getStart());
			assertEquals("INI: Role", rfe_role.toString());
			assertEquals("\"chemical weapons\": Instrument",
					rfe_instr.toString());
			assertEquals("Obj", rfe_instr.getProperty("GF"));
			assertEquals("NP", rfe_instr.getProperty("PT"));
			assertEquals("NP",
					rf.getFrameElements().get("Agent").getProperty("PT"));
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		} catch (FrameElementNotFoundException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTokenData() {
		try {
			FrameNetCorpus fnc = new FrameNetCorpus13(frameNet,
					Logger.getAnonymousLogger());
			fnc.parse(example1);
			de.saar.coli.salsa.reiter.framenet.fncorpus.Sentence sent1 = (de.saar.coli.salsa.reiter.framenet.fncorpus.Sentence) fnc
					.getSentences().get(1);
			System.err.println(sent1.getTokenList());
			assertEquals(22, sent1.getTokenList().size());
			assertEquals("steps", sent1.getToken(new CharacterRange(38, 43))
					.toString());
			assertEquals(5, sent1.getRealizedFrames().size());
			assertEquals("Weapon", sent1.getRealizedFrames().get(3).getFrame()
					.getName());
			assertEquals("weapons of mass destruction", sent1
					.getRealizedFrames().get(3).getTarget().toString());
			assertEquals("weapons of mass destruction", sent1
					.getRealizedFrames().get(3).getFrameElements()
					.get("Weapon").getTarget().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
