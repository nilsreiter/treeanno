package de.nilsreiter.pipeline.segmentation;

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

import de.nilsreiter.pipeline.segmentation.type.Segment;

public class TestSegmentMerger {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks. The cat meows. The mouse flees.");

		AnnotationFactory.createAnnotation(jcas, 0, 10, Segment.class)
				.setValue("1");
		AnnotationFactory.createAnnotation(jcas, 12, 17, Segment.class)
				.setValue("1");
		AnnotationFactory.createAnnotation(jcas, 18, 22, Segment.class)
		.setValue("2");

	}

	@Test
	public void testSegmentMerger() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		assertTrue(JCasUtil.exists(jcas, Segment.class));
		assertEquals(3, JCasUtil.select(jcas, Segment.class).size());

		SimplePipeline.runPipeline(jcas,
				AnalysisEngineFactory.createEngine(SegmentMerger.class));
		assertTrue(JCasUtil.exists(jcas, Segment.class));
		assertEquals(2, JCasUtil.select(jcas, Segment.class).size());
		Segment segment;
		segment = JCasUtil.selectByIndex(jcas, Segment.class, 0);
		assertEquals(0, segment.getBegin());
		assertEquals(17, segment.getEnd());

		segment = JCasUtil.selectByIndex(jcas, Segment.class, 1);
		assertEquals(18, segment.getBegin());
		assertEquals(22, segment.getEnd());

	}
}
