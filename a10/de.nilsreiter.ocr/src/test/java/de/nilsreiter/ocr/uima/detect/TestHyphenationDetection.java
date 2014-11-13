package de.nilsreiter.ocr.uima.detect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.uima.ocr.type.Hyphenation;

public class TestHyphenationDetection {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
	};

	@Test
	public void testPositiveDetection() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		jcas.setDocumentText("The dog is bark-\ning. It is hung-\rry");
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(HyphenationDetection.class));
		assertTrue(JCasUtil.exists(jcas, Hyphenation.class));
		assertEquals(1, JCasUtil.select(jcas, Hyphenation.class).size());
		assertEquals("bark-\ning",
				JCasUtil.selectByIndex(jcas, Hyphenation.class, 0)
				.getCoveredText());
	}

	@Test
	public void testNegativeDetection() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		jcas.setDocumentText("Her name is Leutheuser-\nSchnarrenberger.");
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(HyphenationDetection.class));
		assertFalse(JCasUtil.exists(jcas, Hyphenation.class));

	}
}
