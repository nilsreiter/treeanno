package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl;

public class TestSegmentationSimilarityArtificial {
	JCas gold, silv;

	SegmentationSimilarity_impl bd;

	String text = "12345678901234";

	@BeforeClass
	public static void setUpClass() {
		System.setProperty("python.path",
				"src/main/resources/python/segeval-2.0.11");
	}

	@Before
	public void setUp() throws Exception {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);

		AnnotationFactory.createAnnotation(gold, 1, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 3, 3, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 5, 5, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 8, 8, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 11, 11, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 12, 12, SegmentBoundary.class);
		// AnnotationFactory.createAnnotation(gold, 14, 14,
		// SegmentBoundary.class);

		for (int i = 0; i < text.length() - 1; i++) {
			createAnnotation(gold, i, i + 1, SegmentationUnit.class);
		}

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);
		for (int i = 0; i < text.length() - 1; i++) {
			createAnnotation(silv, i, i + 1, SegmentationUnit.class);
		}

		bd =
				(SegmentationSimilarity_impl) MetricFactory.getMetric(
						SegmentationSimilarity.class, SegmentBoundary.class);

	}

	@Test
	public void testInit() {
		assertTrue(bd.init(gold));
	}

	@Test
	public void testEditDistance1() {

		// no silver annotations
		assertEquals(6, bd.getEditDistance(gold, silv, 2));
		assertEquals(0, bd.getEditDistance(silv, silv, 10));
		assertEquals(0, bd.getEditDistance(gold, gold, 10));

		// some silver annotations
		AnnotationFactory.createAnnotation(silv, 1, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 3, 3, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 4, 4, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 6, 6, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 12, 12, SegmentBoundary.class);

		assertEquals(3, bd.getEditDistance(gold, silv, 2));
		assertEquals(0, bd.getEditDistance(silv, silv, 10));
		assertEquals(0, bd.getEditDistance(gold, gold, 10));
	}
}
