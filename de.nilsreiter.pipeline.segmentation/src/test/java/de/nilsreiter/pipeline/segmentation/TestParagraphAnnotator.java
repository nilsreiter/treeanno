package de.nilsreiter.pipeline.segmentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class TestParagraphAnnotator {
	JCas jcas;

	@Before
	public void setUp() throws Exception {
		jcas = JCasFactory.createJCas();
		InputStreamReader isr =
				new InputStreamReader(getClass().getResourceAsStream(
						"/segmentation_test.txt"));
		StringBuilder b = new StringBuilder();
		while (isr.ready()) {
			b.append((char) isr.read());
		}
		AnnotationFactory.createAnnotation(jcas, 0, b.length(),
				DocumentMetaData.class).setDocumentId("segmentation_test.txt");
		jcas.setDocumentText(b.toString());
		jcas.setDocumentLanguage("en");

	};

	@Test
	public void testParagraphs() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		SimplePipeline.runPipeline(jcas,
				AnalysisEngineFactory.createEngine(ParagraphAnnotator.class));
		assertTrue(JCasUtil.exists(jcas, Paragraph.class));
		assertEquals(0, JCasUtil.selectByIndex(jcas, Paragraph.class, 0)
				.getBegin());
		assertEquals(2810, JCasUtil.select(jcas, Paragraph.class).size());
		assertEquals(jcas.getDocumentText().length(),
				JCasUtil.selectByIndex(jcas, Paragraph.class, 2809).getEnd());

	}

	@Test
	public void testParagraphExport() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngine(ParagraphAnnotator.class), AnalysisEngineFactory
				.createEngine(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
						"src/test/resources/output"));
	}
}
