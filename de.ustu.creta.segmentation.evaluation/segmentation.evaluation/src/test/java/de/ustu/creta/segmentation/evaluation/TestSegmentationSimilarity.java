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

import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl;
import de.ustu.ims.segmentation.type.SegmentBoundary;
import de.ustu.ims.segmentation.type.SegmentationUnit;

public class TestSegmentationSimilarity {
	JCas gold, silv;

	SegmentationSimilarity_impl bd;

	String text = "The dog barks. It is hungry.";

	@BeforeClass
	public static void setUpClass() {
		System.setProperty("python.path",
				"src/main/resources/python/segeval-2.0.11");
	}

	@Before
	public void setUp() throws Exception {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);
		AnnotationFactory.createAnnotation(gold, 8, 8, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 21, 21, SegmentBoundary.class);
		// sentence 1
		createAnnotation(gold, 0, 3, SegmentationUnit.class);
		createAnnotation(gold, 4, 7, SegmentationUnit.class);
		createAnnotation(gold, 8, 13, SegmentationUnit.class);
		createAnnotation(gold, 13, 14, SegmentationUnit.class);

		// sentence 2
		createAnnotation(gold, 15, 17, SegmentationUnit.class);
		createAnnotation(gold, 18, 20, SegmentationUnit.class);
		createAnnotation(gold, 21, 27, SegmentationUnit.class);
		createAnnotation(gold, 27, 28, SegmentationUnit.class);

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);

		createAnnotation(silv, 0, 3, SegmentationUnit.class);
		createAnnotation(silv, 4, 7, SegmentationUnit.class);
		createAnnotation(silv, 8, 13, SegmentationUnit.class);
		createAnnotation(silv, 13, 14, SegmentationUnit.class);

		createAnnotation(silv, 15, 17, SegmentationUnit.class);
		createAnnotation(silv, 18, 20, SegmentationUnit.class);
		createAnnotation(silv, 21, 27, SegmentationUnit.class);
		createAnnotation(silv, 27, 28, SegmentationUnit.class);

		bd =
				(SegmentationSimilarity_impl) MetricFactory.getMetric(
						SegmentationSimilarity.class, SegmentBoundary.class);

	}

	@Test
	public void testInit() {
		assertTrue(bd.init(gold));
	}

	@Test
	public void testNoSilverBreak() {

		assertEquals(2, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.7143, bd.score(gold, silv), 1e-3);

		AnnotationFactory.createAnnotation(silv, 8, 8, SegmentBoundary.class);

		assertEquals(1, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.8571, bd.score(gold, silv), 1e-3);

		AnnotationFactory.createAnnotation(silv, 21, 21, SegmentBoundary.class);
		assertEquals(0, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(1.0, bd.score(gold, silv), 1e-3);

	}

}
