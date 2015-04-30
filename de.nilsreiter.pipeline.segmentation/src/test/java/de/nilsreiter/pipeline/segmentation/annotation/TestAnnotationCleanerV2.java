package de.nilsreiter.pipeline.segmentation.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
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

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel2;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel3;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestAnnotationCleanerV2 {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks, because he is hungry.");
		jcas.setDocumentLanguage("en");
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(StanfordSegmenter.class));

	}

	@Test
	public void testAnnotationCleanerDouble()
			throws AnalysisEngineProcessException,
			ResourceInitializationException {
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);

		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(AnnotationCleanerV2.class));

		assertTrue(JCasUtil.exists(jcas, SegmentBoundaryLevel3.class));
		assertEquals(1, JCasUtil.select(jcas, SegmentBoundaryLevel3.class)
				.size());
	}

	@Test
	public void testAnnotationCleanerTriple()
			throws AnalysisEngineProcessException,
			ResourceInitializationException {
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);

		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(AnnotationCleanerV2.class));

		assertTrue(JCasUtil.exists(jcas, SegmentBoundaryLevel3.class));
		assertEquals(1, JCasUtil.select(jcas, SegmentBoundaryLevel3.class)
				.size());
	}

	@Test
	public void testAnnotationCleanerReal()
			throws AnalysisEngineProcessException,
			ResourceInitializationException {
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel2.class);
		AnnotationFactory.createAnnotation(jcas, 4, 7,
				SegmentBoundaryLevel3.class);

		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(AnnotationCleanerV2.class));

		assertTrue(JCasUtil.exists(jcas, SegmentBoundaryLevel2.class));
		assertEquals(1, JCasUtil.select(jcas, SegmentBoundaryLevel2.class)
				.size());
	}

}
