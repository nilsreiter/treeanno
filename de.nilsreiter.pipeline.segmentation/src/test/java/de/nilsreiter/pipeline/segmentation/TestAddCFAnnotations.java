package de.nilsreiter.pipeline.segmentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterator;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestAddCFAnnotations {
	JCas jcas;

	@Before
	public void setUp() throws ResourceInitializationException {
		JCasIterator jcasIterator =
				SimplePipeline
						.iteratePipeline(
								CollectionReaderFactory.createReaderDescription(
										TextReader.class,
										TextReader.PARAM_SOURCE_LOCATION,
								"src/test/resources/CFText/*.txt",
								TextReader.PARAM_LANGUAGE, "en"),
								AnalysisEngineFactory
										.createEngineDescription(StanfordSegmenter.class))
								.iterator();
		jcas = jcasIterator.next();

	}

	@Test
	public void testAddCFAnnotationsPreparations() {
		assertNotNull(jcas);
	}

	@Test
	public void testAddCFAnnotations() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(AddCFAnnotations.class,
						AddCFAnnotations.PARAM_INPUT_DIRECTORY,
						"src/test/resources/CFAnnotations"));
		assertNotNull(jcas);
		assertTrue(JCasUtil.exists(jcas, SegmentBoundary.class));
		SegmentBoundary sb =
				JCasUtil.selectByIndex(jcas, SegmentBoundary.class, 0);
		assertEquals(0.8205, sb.getConfidence(), 1e-5);
		assertTrue(sb.getCoveredText().startsWith("But one stormy night"));
	}
}
