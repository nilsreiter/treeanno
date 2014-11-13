package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Test;

import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotatedLexicalUnit;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus13;

public class TestAnnotationCorpus13 extends TestBase13 {

    @Test
    public void testCorpusData() throws Exception {
	AnnotationCorpus ac =
		new AnnotationCorpus13(frameNet, Logger.getAnonymousLogger());
	ac.parse(new File(fnhome + "/luXML"), "lu4224.xml");
	assertNotNull(frameNet.getLexicalUnit(4224));
	assertEquals(
		"COD: a container used to hold or carry things, made from interwoven strips of cane or wire",
		ac.getAnnotation(frameNet.getLexicalUnit(4224)).getDefinition());
	AnnotatedLexicalUnit alu =
		ac.getAnnotation(frameNet.getLexicalUnit(4224));
	assertEquals(40, alu.getSentences().size());
	assertEquals(3, alu.getSentences().iterator().next().getTokenList()
		.size());
	assertEquals("Containers", alu.getFrame().getName());
    }

}
