package de.nilsreiter.pipeline.segmentation.infinitejest;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.ParagraphAnnotator;
import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

public class TestIJEvalPreparer {
	JCas jcas;

	@Before
	public void setUp() throws ResourceInitializationException {
		String filePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/infinite_jest_annotated.txt";

		CollectionReaderDescription cr =
				CollectionReaderFactory.createReaderDescription(IJReader.class,
						IJReader.PARAM_SOURCE_LOCATION, filePath,
						IJReader.PARAM_CREATE_BOUNDARY_ANNOTATION, false,
						IJReader.PARAM_CREATE_SEGMENT_ANNOTATION, true);

		JCasIterable jcasIter = SimplePipeline.iteratePipeline(cr);
		jcas = jcasIter.iterator().next();
	}

	@Test
	public void testIJEvalPreparer() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		assertEquals(125, JCasUtil.select(jcas, Segment.class).size());
		assertFalse(JCasUtil.exists(jcas, Paragraph.class));

		SimplePipeline.runPipeline(jcas,
				createEngine(ParagraphAnnotator.class),
				createEngine(IJEvalPreparer.class));
		assertTrue(JCasUtil.exists(jcas, Paragraph.class));
		assertEquals(184, JCasUtil.select(jcas, Paragraph.class).size());
		assertEquals(184, JCasUtil.select(jcas, Segment.class).size());

	}
}
