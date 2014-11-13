package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.fncorpus.FrameNetCorpus;
import de.saar.coli.salsa.reiter.framenet.fncorpus.FrameNetCorpus15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.Sentence;
import de.uniheidelberg.cl.reiter.util.CharacterRange;

public class TestFNCorpus15 extends TestBase15 {

	File example1;

	@Before
	public void setUp() {
		example1 = new File(this.getClass()
				.getResource("/NTI__LibyaCountry1.xml").getFile());
	}

	@Test
	public void testCorpusData() {
		try {
			FrameNetCorpus fnc = new FrameNetCorpus15(frameNet,
					Logger.getAnonymousLogger());
			fnc.parse(example1);
			assertEquals(41, fnc.getSentences().size());
			Sentence sentence = (Sentence) fnc.getSentences().get(1);

			assertEquals(
					"Libya has shown interest in and taken steps to acquire weapons of mass destruction ( WMD ) and their delivery systems . ",
					sentence.getText());
			assertEquals(5, sentence.getRealizedFrames().size());
			RealizedFrame rf = sentence.getRealizedFrames().get(0);
			assertEquals("interest", rf.getTarget().toString());
			assertEquals("Emotion_directed", rf.getFrame().getName());
			assertEquals(16, rf.getStart());
			assertEquals(24, rf.getEnd());
			assertEquals(2, rf.getFrameElements().size());
			assertEquals("Libya", rf.getFrameElements().get("Experiencer")
					.getTargetString());
			rf = (RealizedFrame) fnc.getSentences().get(2).getRealizedFrames()
					.get(1);
			assertEquals("Using", rf.getFrame().getName());

			assertEquals(4, rf.frameElements().size());
			RealizedFrameElement rfe_instr = rf.getFrameElements().get(
					"Instrument");
			RealizedFrameElement rfe_role = rf.getFrameElements().get("Role");
			assertEquals(false, rfe_instr.isNullInstantiated());
			assertEquals(true, rfe_role.isNullInstantiated());

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
			FrameNetCorpus fnc = new FrameNetCorpus15(frameNet,
					Logger.getAnonymousLogger());
			fnc.parse(example1);
			Sentence sent1 = (Sentence) fnc.getSentences().get(1);
			assertEquals("steps", sent1.getToken(new CharacterRange(38, 43))
					.toString());
			assertEquals(22, sent1.getTokenList().size());
			assertEquals(5, sent1.getRealizedFrames().size());
			assertEquals("Weapon", sent1.getRealizedFrames().get(3).getFrame()
					.getName());
			assertEquals("weapons of mass destruction", sent1
					.getRealizedFrames().get(3).getTargetList().toString());
			assertEquals("weapons of mass destruction", sent1
					.getRealizedFrames().get(3).getFrameElements()
					.get("Weapon").getTarget().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
