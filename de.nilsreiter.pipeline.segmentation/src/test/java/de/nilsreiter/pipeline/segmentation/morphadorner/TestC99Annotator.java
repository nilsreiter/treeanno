package de.nilsreiter.pipeline.segmentation.morphadorner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.SegmentationSubUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestC99Annotator {
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

		jcas.setDocumentText(b.toString());
		jcas.setDocumentLanguage("en");
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngine(StanfordSegmenter.class), AnalysisEngineFactory
				.createEngine(SegmentationUnitAnnotator.class),
				AnalysisEngineFactory
						.createEngine(SegmentationSubUnitAnnotator.class));
	};

	@Test
	public void testC99() throws AnalysisEngineProcessException {
		C99Annotator anno = new C99Annotator();
		anno.process(jcas);

		assertTrue(JCasUtil.exists(jcas, SegmentBoundary.class));
		assertEquals(36, JCasUtil.select(jcas, SegmentBoundary.class).size());
		assertEquals("T", JCasUtil
				.selectByIndex(jcas, SegmentBoundary.class, 0).getCoveredText());
		assertEquals("s", JCasUtil
				.selectByIndex(jcas, SegmentBoundary.class, 5).getCoveredText());
	}
}
