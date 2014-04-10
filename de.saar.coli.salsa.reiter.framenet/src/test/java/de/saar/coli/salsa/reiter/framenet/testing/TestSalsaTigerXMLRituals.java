package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.junit.Test;

import de.saar.coli.salsa.reiter.framenet.AHasTarget;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.STXDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.Sentence;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.SalsaTigerXML;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.TokenRange;

public class TestSalsaTigerXMLRituals {

    // @Rule
    // public MethodRule benchmarkRun = new BenchmarkRule();

    static String example1 = System.getProperty("user.dir") + "/data/r0003.xml";
    static String example2 = System.getProperty("user.dir")
	    + "/data/SalsaTigerExample-2.xml";
    static String example3 = System.getProperty("user.dir")
	    + "/data/SalsaTigerExample-3.xml";
    static String example4 =
	    "/proj/rituals/annotation/salto-repository/master/r0025_Placing_merged_jom.nl.xml";
    static String example5 = System.getProperty("user.dir")
	    + "/data/test_0.xml";
    static String example6 = System.getProperty("user.dir")
	    + "/../A10/tasks/srl/semafor/training_test_output";

    static String example7 = System.getProperty("user.dir")
	    + "/data/r0003_full_annotation_merged.jom.bl.xml";

    @Test
    public void testLoading() {
	FrameNet fn = new FrameNet();
	fn.readData(new STXDatabaseReader(new File(example3)));
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
	SalsaTigerXML stx = new SalsaTigerXML(fn, null);
	try {
	    stx.parse(new File(example1));
	    assertEquals(101, stx.getSentences().size());

	    assertEquals(0, stx.getSentences().get(0).getRealizedFrames()
		    .size());
	    assertEquals(0, stx.getSentences().get(1).getRealizedFrames()
		    .size());
	    assertEquals(0, stx.getSentences().get(4).getRealizedFrames()
		    .size());
	    assertEquals(3, stx.getSentences().get(18).getRealizedFrames()
		    .size());
	    Sentence sentence = stx.getSentences().get(18);
	    IRealizedFrame rf = sentence.getRealizedFrames().get(0);
	    assertEquals("cut", rf.getTarget().toString());
	    assertEquals("Cutting", rf.getFrame().getName());
	    // assertEquals(0, rf.getStart());
	    // assertEquals(0, rf.getEnd());
	    assertEquals(3, rf.getFrameElements().size());
	    assertEquals("he",
		    ((AHasTarget) rf.getFrameElements().get("Agent"))
			    .getTargetString());
	    assertEquals(38, sentence.getTokenList().size());
	} catch (FrameNotFoundException e) {
	    e.printStackTrace();
	} catch (FrameElementNotFoundException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testCorpusData2() {
	FrameNet fn = new FrameNet();
	SalsaTigerXML stx = new SalsaTigerXML(fn, null);
	try {
	    stx.parse(new File(example3));
	    assertEquals(2, stx.getSentences().size());

	    assertEquals(1, stx.getSentences().get(0).getRealizedFrames()
		    .size());
	    assertEquals(1, stx.getSentences().get(1).getRealizedFrames()
		    .size());
	    Sentence sentence = stx.getSentences().get(1);
	    RealizedFrame rf =
		    (RealizedFrame) sentence.getRealizedFrames().get(0);
	    assertEquals("puts", rf.getTarget().toString());
	    assertEquals("Placing", rf.getFrame().getName());
	    // assertEquals(0, rf.getStart());
	    // assertEquals(0, rf.getEnd());
	    assertEquals(3, rf.getFrameElements().size());
	    assertEquals("The priest", rf.getFrameElements().get("Agent")
		    .getTargetString());
	    assertEquals(9, sentence.getTokenList().size());
	    Iterator<RealizedFrameElement> rfeIter =
		    rf.frameElements().iterator();
	    RealizedFrameElement rfe = rfeIter.next();
	    assertEquals("Agent", rfe.getFrameElement().getName());

	    rfe = rfeIter.next();
	    assertEquals("Theme", rfe.getFrameElement().getName());

	    rfe = rfeIter.next();
	    assertEquals("Goal", rfe.getFrameElement().getName());
	    assertEquals(1, rfe.getTargetList().size());
	    assertEquals(null, rfe.getIType());
	    IToken token = rfe.getTarget(0);
	    assertEquals(29, token.getCharacterRange().getElement1().intValue());
	    assertEquals(46, token.getCharacterRange().getElement2().intValue());
	    assertEquals(29, rfe.getTargetCharacterRange().getElement1()
		    .intValue());
	    assertEquals(46, rfe.getTargetCharacterRange().getElement2()
		    .intValue());

	} catch (FrameNotFoundException e) {
	    e.printStackTrace();
	} catch (FrameElementNotFoundException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testAllSemaforData() throws FrameNotFoundException,
	    FrameElementNotFoundException {
	FrameNet fn = new FrameNet();
	SalsaTigerXML stx = new SalsaTigerXML(fn, null);

	stx.parse(new File(example6));
	for (Sentence sentence : stx.getSentences()) {
	    for (IRealizedFrame rf : sentence.getRealizedFrames()) {
		assertNotNull(rf);
		assertNotNull(rf.getTargetList());
	    }
	}
    }

    @Test
    public void testSemaforData() {
	FrameNet fn = new FrameNet();
	SalsaTigerXML stx = new SalsaTigerXML(fn, null);

	Sentence sentence;
	RealizedFrame rf;
	RealizedFrameElement rfe;
	Iterator<? extends IRealizedFrameElement> iter;

	try {
	    stx.parse(new File(example5));
	    assertEquals(138, stx.getSentences().size());
	    sentence = stx.getSentences().get(8);
	    assertEquals(
		    "The musicians recite a poem for the sealing of the ritual .",
		    sentence.getText());
	    assertEquals(1, sentence.getRealizedFrames().size());
	    rf = (RealizedFrame) sentence.getRealizedFrames().get(0);
	    assertEquals(3, rf.overtFrameElements().size());

	    iter = rf.frameElements().iterator();

	    rfe = (RealizedFrameElement) iter.next();
	    assertEquals("Author", rfe.getFrameElement().getName());
	    assertEquals(1, rfe.getTargetList().size());
	    assertEquals(new CharacterRange(0, 13),
		    rfe.getTargetCharacterRange());
	    assertEquals(new TokenRange(0, 1), rfe.getTargetTokenRange());

	    assertEquals(new TokenRange(3, 4),
		    ((AHasTarget) iter.next()).getTargetTokenRange());
	    assertEquals(new TokenRange(5, 10),
		    ((AHasTarget) iter.next()).getTargetTokenRange());

	    sentence = stx.getSentences().get(13);
	    iter =
		    sentence.getRealizedFrames().get(0).overtFrameElements()
			    .iterator();
	    assertEquals(new TokenRange(1, 1),
		    ((AHasTarget) iter.next()).getTargetTokenRange());

	    sentence = stx.getSentences().get(0);
	    iter =
		    sentence.getRealizedFrames().get(0).overtFrameElements()
			    .iterator();
	    assertEquals(new TokenRange(12, 12),
		    ((AHasTarget) iter.next()).getTargetTokenRange());
	    assertEquals(new TokenRange(13, 15),
		    ((AHasTarget) iter.next()).getTargetTokenRange());

	    sentence = stx.getSentenceIndex().get("r000456+r000456_f0");
	    rf = (RealizedFrame) sentence.getRealizedFrames().get(0);
	    iter = rf.overtFrameElements().iterator();
	    rfe = (RealizedFrameElement) iter.next();
	    assertEquals("Agent", rfe.getFrameElement().getName());
	    rfe = (RealizedFrameElement) iter.next();
	    assertEquals("Manner", rfe.getFrameElement().getName());
	    rfe = (RealizedFrameElement) iter.next();
	    assertEquals("Entity", rfe.getFrameElement().getName());
	    assertEquals(new TokenRange(27, 29), rfe.getTargetTokenRange());
	    assertEquals("three kindling sticks", rfe.getTargetString());

	} catch (FrameNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FrameElementNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testFullAnnotation() throws FrameNotFoundException,
	    FrameElementNotFoundException, FileNotFoundException,
	    SecurityException {
	FrameNet fn = new FrameNet();
	fn.readData(new FNDatabaseReader15(new File("../A10/framenet-1.5"),
		false));
	SalsaTigerXML stx = new SalsaTigerXML(fn, null);
	File f = new File("data/full/r0003.xml");
	stx.parse(f);

	Sentence sentence = stx.getSentenceIndex().get("r000316");
	IToken token = sentence.getTokenList().get(76);
	assertEquals(
		token.toString(),
		sentence.getText().substring(
			token.getCharacterRange().getElement1(),
			token.getCharacterRange().getElement2()));

	for (IRealizedFrame rf : sentence.getRealizedFrames()) {
	    if (rf.getFrame() == fn.getFrame("Filling")) {
		IRealizedFrameElement rfe = rf.getFrameElements().get("Goal");

		assertEquals(sentence.getTokenList().get(76), rfe
			.getTargetList().last());
		// System.out.println(rfe);
	    }

	}

	/*
	 * File dir = new File("data/full"); for (File f : dir.listFiles()) {
	 * stx.parse(f); for (IRealizedFrame rf : stx.getRealizedFrames()) {
	 * System.out.println(rf); for (IRealizedFrameElement rfe :
	 * rf.frameElements()) { System.out.println(rfe.toString()); } } }
	 */
    }
}