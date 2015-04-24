package de.nilsreiter.segmentation.evaluation;

import static org.junit.Assert.assertEquals;

import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;

public class TestFleissKappaBoundarySimilarity_Tokens {

	JCas gold, silv;

	FleissKappaBoundarySimilarity bd;

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
		AnnotationFactory.createAnnotation(gold, 0, 0, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 15, 15, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 27, 27, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 0, 3, SegmentationUnit.class);
		AnnotationFactory.createAnnotation(gold, 4, 7, SegmentationUnit.class);
		AnnotationFactory.createAnnotation(gold, 8, 13, SegmentationUnit.class);
		AnnotationFactory
		.createAnnotation(gold, 13, 14, SegmentationUnit.class);
		AnnotationFactory
		.createAnnotation(gold, 15, 17, SegmentationUnit.class);
		AnnotationFactory
		.createAnnotation(gold, 18, 20, SegmentationUnit.class);
		AnnotationFactory
		.createAnnotation(gold, 21, 27, SegmentationUnit.class);
		AnnotationFactory
		.createAnnotation(gold, 27, 28, SegmentationUnit.class);

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);

		AnnotationFactory.createAnnotation(silv, 0, 3, SegmentationUnit.class);
		AnnotationFactory.createAnnotation(silv, 4, 7, SegmentationUnit.class);
		AnnotationFactory.createAnnotation(silv, 8, 13, SegmentationUnit.class);
		AnnotationFactory
				.createAnnotation(silv, 13, 14, SegmentationUnit.class);
		AnnotationFactory
				.createAnnotation(silv, 15, 17, SegmentationUnit.class);
		AnnotationFactory
				.createAnnotation(silv, 18, 20, SegmentationUnit.class);
		AnnotationFactory
				.createAnnotation(silv, 21, 27, SegmentationUnit.class);
		AnnotationFactory
				.createAnnotation(silv, 27, 28, SegmentationUnit.class);

		bd =
				MetricFactory.getMetric(FleissKappaBoundarySimilarity.class,
						SegmentBoundary.class);
	}

	@Test
	public void testNoSilverBreak() {
		assertEquals(0.0,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testPerfect() {
		AnnotationFactory.createAnnotation(silv, 0, 0, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 15, 15, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 27, 27, SegmentBoundary.class);

		assertEquals(1.0,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testOneMissed() {
		AnnotationFactory.createAnnotation(silv, 0, 0, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 27, 27, SegmentBoundary.class);

		assertEquals(0.47872,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testOneMisplaced() {
		AnnotationFactory.createAnnotation(silv, 0, 0, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 18, 18, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 27, 27, SegmentBoundary.class);

		assertEquals(0.72777,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}
}
