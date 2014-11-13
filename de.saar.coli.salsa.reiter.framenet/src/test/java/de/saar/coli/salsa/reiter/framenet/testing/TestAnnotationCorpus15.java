package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.saar.coli.salsa.reiter.framenet.FrameNetVersion;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.Lexeme;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotatedLexicalUnit;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotatedLexicalUnit15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationSet;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationSet15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.Sentence15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.SubCorpus15;
import de.saar.coli.salsa.reiter.framenet.fncorpus.export.XMLExporter;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.SalsaTigerXML;
import de.uniheidelberg.cl.reiter.pos.BNC;
import de.uniheidelberg.cl.reiter.pos.FN;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.IO;
import de.uniheidelberg.cl.reiter.util.TokenRange;

public class TestAnnotationCorpus15 extends TestBase15 {

	File tempdir;

	@Before
	public void setUp() throws IOException {
		tempdir = IO.createTempDir("test", "");
	}

	@After
	public void tearDown() {
		tempdir.delete();
	}

	@Test
	public void testAllCorpusData() throws Exception {
		AnnotationCorpus15 ac = new AnnotationCorpus15(frameNet,
				Logger.getAnonymousLogger());
		ac.parseWithout(new File(fnhome + "/lu"), "lu5302.xml", "lu13151.xml",
				"lu3217.xml", "lu3914.xml", "lu5160.xml", "lu453.xml",
				"lu9966.xml", "lu1173.xml", "lu2907.xml", "lu6752.xml",
				"lu3928.xml", "lu1134.xml", "lu3916.xml", "lu5954.xml",
				"lu15925.xml", "lu3908.xml", "lu740.xml", "lu6713.xml",
				"lu3913.xml", "lu3907.xml", "lu7714.xml", "lu3915.xml",
				"lu3186.xml", "lu6751.xml", "lu1678.xml", "lu6747.xml",
				"lu3929.xml", "lu2868.xml", "lu4704.xml", "lu5751.xml",
				"lu2872.xml", "lu2880.xml", "lu5816.xml", "lu6702.xml",
				"lu6240.xml", "lu8923.xml", "lu14672.xml", "lu1238.xml",
				"lu2869.xml", "lu8503.xml", "lu2873.xml", "lu7978.xml",
				"lu1246.xml", "lu5867.xml", "lu3185.xml");
		XMLExporter export = new XMLExporter();
		export.setLuXSLT(new File(this.getClass().getResource("/lexUnit.xsl")
				.getFile()));
		export.setLuIndexXSLT(new File(this.getClass()
				.getResource("/luIndex.xsl").getFile()));
		export.writeToDirectory(ac, tempdir);
		/*
		 * for (AnnotatedLexicalUnit alu : ac.getAnnotations()) { LexicalUnit lu
		 * = alu.getLexicalUnit(); for (Lexeme lexeme : lu.getLexemes()) {
		 * assertNotNull(lexeme.toString(), lexeme.getPartOfSpeech());
		 * assertNotNull(lexeme.getPartOfSpeech().toString(), lexeme
		 * .getPartOfSpeech().asFN()); } for (Sentence sentence :
		 * alu.getSentences()) { // System.err.println(" " + sentence); for
		 * (IToken token : sentence.getTokenList()) { // System.err.println("  "
		 * + token); assertNotNull(sentence.getIdString(),
		 * token.getPartOfSpeech()); try { assertNotNull(alu.getId() + " " +
		 * sentence.toString(), token.toString()); } catch
		 * (StringIndexOutOfBoundsException e) {
		 * System.err.println(alu.getId());
		 * System.err.println(sentence.toString());
		 * System.err.println(e.getMessage()); } } for (RealizedFrame rf :
		 * sentence.getRealizedFrames()) { assertNotNull(alu.getId() +
		 * " getFrame()", rf.getFrame()); assertNotNull(alu.getId() +
		 * " getTargetList()", rf.getTargetList()); if
		 * (rf.getTargetList().size() > 0) { assertNotNull(alu.getId() + " " +
		 * rf, rf .getTargetList().get(0).getPartOfSpeech()); }
		 * 
		 * } } }
		 */
	}

	@Test
	public void testCorpusData() throws Exception {
		AnnotationCorpus ac = new AnnotationCorpus15(frameNet,
				Logger.getAnonymousLogger());
		ac.parse(new File(fnhome + "/lu"), "lu4224");
		int lexicalUnitId = 4224;
		assertNotNull(frameNet.getLexicalUnit(lexicalUnitId));
		assertNotNull(ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId)));
		assertEquals(
				"COD: a container used to hold or carry things, made from interwoven strips of cane or wire",
				ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId))
						.getDefinition());
		AnnotatedLexicalUnit alu = ac.getAnnotation(frameNet
				.getLexicalUnit(lexicalUnitId));
		assertEquals(40, alu.getSentences().size());
		assertEquals(FN.Noun, alu.getPartOfSpeech());
		assertEquals("Containers", alu.getFrame().getName());
		de.saar.coli.salsa.reiter.framenet.Sentence sentence = alu
				.getSentences().get(0);
		assertEquals(15, sentence.getTokenList().size());
		assertEquals(
				"The Mark Wilkinson team will then install custom-made woven vegetable baskets in that space . ",
				sentence.getText());
		assertEquals(1, sentence.getRealizedFrames().size());
		RealizedFrame rf = (RealizedFrame) sentence.getRealizedFrames()
				.iterator().next();
		assertNotNull(rf);
		assertEquals("Containers", rf.getFrame().getName());
		assertEquals(3, rf.getFrameElements().values().size());
		assertEquals(54, rf.frameElements().first().getStart());
		assertEquals("woven", rf.frameElements().first().getTargetString(' '));
		assertEquals(BNC.NN0, sentence.getTokenList().get(3).getPartOfSpeech());
		assertEquals(BNC.PUN, sentence.getTokenList().get(14).getPartOfSpeech());
		assertEquals(3, rf.overtFrameElements().size());
		for (IRealizedFrameElement rfe : rf.frameElements()) {
			assertFalse(rfe.isNullInstantiated());
		}
		assertEquals("team", sentence.getTokenList().get(3).toString());
		assertEquals(".",
				sentence.getTokenList().get(sentence.getTokenList().size() - 1)
						.toString());

		ac.parse(new File(fnhome + "/lu"), "lu15119");
		lexicalUnitId = 15119;
		assertNotNull(frameNet.getLexicalUnit(lexicalUnitId));
		assertNotNull(ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId)));
		assertEquals("FN: receive reward for competition",
				ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId))
						.getDefinition());
		alu = ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId));
		assertEquals(56, alu.getSentences().size());

		assertEquals("Win_prize", alu.getFrame().getName());

		sentence = alu.getSentences().get(3);
		assertEquals(
				"Eubank took a comfortable unanimous decision -- although I had him winning by only one round and plenty of ringsiders thought he had n't made it .",
				sentence.getText());
		rf = (RealizedFrame) sentence.getRealizedFrames().iterator().next();
		assertEquals(3, rf.frameElements().size());
		assertEquals(2, rf.overtFrameElements().size());

		// Now testing lexical unit 14743 (MWE)
		lexicalUnitId = 14743;
		ac.parse(new File(fnhome + "/lu"), "lu" + String.valueOf(lexicalUnitId));
		alu = ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId));
		assertNotNull(alu);
		assertEquals("a little.adv", alu.getName());
		assertEquals(FN.Adverb, alu.getPartOfSpeech());
		// TODO: This should be extended. More test cases are always welcome.

		/*
		 * lexicalUnitId = 16118; ac.parse(new File(fnhome + "/lu"), "lu" +
		 * String.valueOf(lexicalUnitId)); alu =
		 * ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId));
		 * assertNotNull(alu); assertEquals("a little.adv", alu.getName());
		 * assertEquals(FN.Adverb, alu.getPartOfSpeech());
		 */

		lexicalUnitId = 507;
		ac.parse(new File(fnhome + "/lu"), "lu" + String.valueOf(lexicalUnitId));
		alu = ac.getAnnotation(frameNet.getLexicalUnit(lexicalUnitId));
		assertNotNull(alu);
		assertEquals("discussion.n", alu.getName());
		assertEquals(FN.Noun, alu.getPartOfSpeech());
		sentence = alu.getSentences().get(55);
		assertEquals("PANEL DISCUSSION ON NEW PERFORMANCE", sentence.toString());
		assertEquals(1, sentence.getRealizedFrames().size());
		rf = (RealizedFrame) sentence.getRealizedFrames().get(0);
		assertEquals(2, rf.frameElements().size());
		assertEquals(new TokenRange(1, 1), rf.getTargetTokenRange());
		assertEquals(new TokenRange(2, 4), rf.frameElements().last()
				.getTargetTokenRange());

	}

	@Test
	public void testExport() throws Exception {
		AnnotationCorpus15 ac = new AnnotationCorpus15(frameNet,
				Logger.getAnonymousLogger());
		ac.parse(new File(fnhome + "/lu"), "lu4224");
		int lexicalUnitId = 4224;
		AnnotatedLexicalUnit alu = ac.getAnnotation(frameNet
				.getLexicalUnit(lexicalUnitId));
		XMLExporter export = new XMLExporter(FrameNetVersion.V15);
		// Document doc = export.asDocument((AnnotatedLexicalUnit15) alu);
		export.writeToDirectory(ac, tempdir);
		try {
			AnnotationCorpus ac2 = new AnnotationCorpus15(frameNet,
					Logger.getAnonymousLogger());
			ac2.parse(new File(tempdir, "lu"), "lu4224");
			AnnotatedLexicalUnit alu2 = ac2.getAnnotation(frameNet
					.getLexicalUnit(lexicalUnitId));

			// base data
			assertEquals("Definition", alu.getDefinition(),
					alu2.getDefinition());
			assertEquals("Name", alu.getName(), alu2.getName());
			assertEquals("Part of Speech", alu.getPartOfSpeech(),
					alu2.getPartOfSpeech());

			// lexemes
			assertEquals(alu.getLexemes().size(), alu2.getLexemes().size());
			Lexeme[] lexeme = new Lexeme[] { alu.getLexemes().get(0),
					alu2.getLexemes().get(0) };
			assertEquals(lexeme[0].getPartOfSpeech(),
					lexeme[1].getPartOfSpeech());
			assertEquals(lexeme[0].getIdString(), lexeme[1].getIdString());
			assertEquals(lexeme[0].getPartOfSpeech(),
					lexeme[1].getPartOfSpeech());
			assertEquals(lexeme[0].getValue(), lexeme[1].getValue());
			assertEquals(lexeme[0].isBreakBefore(), lexeme[1].isBreakBefore());
			assertEquals(lexeme[0].isHeadword(), lexeme[1].isHeadword());

			// sentences
			assertEquals(alu.getSentences().size(), alu2.getSentences().size());
			de.saar.coli.salsa.reiter.framenet.Sentence[] sentence = new de.saar.coli.salsa.reiter.framenet.Sentence[] {
					alu.getSentences().get(0), alu2.getSentences().get(0) };
			assertEquals(sentence[0].getIdString(), sentence[1].getIdString());
			assertEquals(sentence[0].getText(), sentence[1].getText());
			assertEquals(sentence[0].getRealizedFrames().size(), sentence[1]
					.getRealizedFrames().size());

			// realized frames
			RealizedFrame[] rf = new RealizedFrame[] {
					(RealizedFrame) sentence[0].getRealizedFrames().iterator()
							.next(),
					(RealizedFrame) sentence[1].getRealizedFrames().iterator()
							.next() };
			assertEquals("Realized frame, Start", rf[0].getStart(),
					rf[1].getStart());
			assertEquals("Realized frame, End", rf[0].getEnd(), rf[1].getEnd());
			assertEquals(rf[0].getIdString(), rf[1].getIdString());
			assertEquals(rf[0].getFrame(), rf[1].getFrame());
			assertEquals(rf[0].getTargetString(' '), rf[1].getTargetString(' '));

			// realized frame elements
			RealizedFrameElement[] rfe = new RealizedFrameElement[] {
					rf[0].frameElements().last(), rf[1].frameElements().last() };
			assertEquals("Realized frame element, Start", rfe[0].getStart(),
					rfe[1].getStart());
			assertEquals("Realized frame element, End", rfe[0].getEnd(),
					rfe[1].getEnd());
			assertEquals("Frame Element", rfe[0].getFrameElement(),
					rfe[1].getFrameElement());
			assertEquals("IType", rfe[0].getIType(), rfe[1].getIType());
			assertEquals("Id", rfe[0].getIdString(), rfe[1].getIdString());
			// tmp.deleteOnExit();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println("Temporary file: " + tempdir.getAbsolutePath());
			fail(e.getClass().getName());
		}

	}

	@Test
	public void testExportFromSTX() throws Exception {
		SalsaTigerXML stx = new SalsaTigerXML(frameNet,
				Logger.getAnonymousLogger());
		stx.parse(new File(this.getClass()
				.getResource("/SalsaTigerExample-3.xml").getFile()));

		AnnotationCorpus15 ac = new AnnotationCorpus15(frameNet,
				Logger.getAnonymousLogger());

		for (de.saar.coli.salsa.reiter.framenet.Sentence sentence : stx
				.getSentences()) {
			for (IRealizedFrame rf : sentence.getRealizedFrames()) {
				ac.addLexicalUnit("test-3", (RealizedFrame) rf);
				assertNotNull(rf.frameElements());
			}
		}

		// RealizedFrame rf =
		// stx.getSentences().get(0).getRealizedFrames().get(0);

		LexicalUnit lu = frameNet.getLexicalUnit(frameNet.getFrame("Placing"),
				"put", FN.Verb);

		assertEquals(2, ac.getNumberOfAnnotations());

		AnnotatedLexicalUnit alu = ac.getAnnotation(lu);
		assertEquals("put.v", alu.getName());
		assertEquals(null, alu.getDefinition());
		assertEquals(2, alu.getId());
		assertEquals("2", alu.getIdString());
		assertEquals(FN.Verb, alu.getPartOfSpeech());

		assertEquals(1, alu.getLexemes().size());

		for (AnnotatedLexicalUnit alu2 : ac.getAnnotations()) {
			AnnotatedLexicalUnit15 alu2_15 = (AnnotatedLexicalUnit15) alu2;
			for (SubCorpus15 sc : alu2_15.getSubCorpora()) {
				for (de.saar.coli.salsa.reiter.framenet.Sentence sentence : sc
						.getSentences()) {
					for (IRealizedFrame rf : sentence.getRealizedFrames()) {
						assertNotNull(rf.frameElements());
					}
				}
			}
		}

		Lexeme lexeme = alu.getLexemes().get(0);
		assertEquals("put", lexeme.getValue());
		assertEquals(null, lexeme.getIdString());
		assertEquals(FN.Verb, lexeme.getPartOfSpeech());

		XMLExporter export = new XMLExporter(FrameNetVersion.V15);
		File tmp = IO.createTempDir("fnapi", null);
		System.err.println(tmp.getAbsolutePath());
		export.writeToDirectory(ac, tmp);
		try {
			AnnotationCorpus ac2 = new AnnotationCorpus15(frameNet,
					Logger.getAnonymousLogger());
			ac2.parse(new File(tmp, "lu"), "lu2");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println("Temporary file: " + tmp.getAbsolutePath());
			fail(e.getClass().getName());
		}

	}

	@Test
	public void testNonXML() throws Exception {
		AnnotationCorpus ac = new AnnotationCorpus15(frameNet,
				frameNet.getLogger());
		AnnotatedLexicalUnit15 alu = new AnnotatedLexicalUnit15(ac,
				frameNet.getLexicalUnit(4224));
		alu.setName("recite.v");
		SubCorpus15 subCorpus = new SubCorpus15(alu, "testCorpus");
		alu.addSubCorpus(subCorpus);
		Sentence15 sentence = new Sentence15(subCorpus, "Recite the mantra .");
		sentence.addToken(new CharacterRange(0, 6));
		sentence.addToken(new CharacterRange(7, 10));
		sentence.addToken(new CharacterRange(11, 17));
		sentence.setId("1");
		Iterator<IToken> tokenIterator = sentence.getTokenIterator();
		subCorpus.addSentence(sentence);
		RealizedFrame rf = frameNet.getFrame("Text_creation").realize(
				sentence.getToken(new CharacterRange(0, 6)));
		rf.addRealizedFrameElement("Text",
				sentence.getToken(new CharacterRange(11, 17)));
		alu.setFrame(rf.getFrame());
		AnnotationSet as = new AnnotationSet15(sentence, rf);
		as.setCreationDate(Calendar.getInstance().getTime());
		sentence.addAnnotationSet(as);
		assertEquals("Recite", tokenIterator.next().toString());
		assertEquals("the", tokenIterator.next().toString());

		XMLExporter export = new XMLExporter(FrameNetVersion.V15);
		Document doc = export.asDocument(alu);
		export.writeToFile(doc, new File(tempdir, "lu.xml"));
	}

}
