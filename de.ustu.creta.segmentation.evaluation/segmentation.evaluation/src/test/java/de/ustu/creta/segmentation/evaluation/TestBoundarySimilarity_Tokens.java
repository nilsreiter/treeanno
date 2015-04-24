package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;
import de.ustu.creta.segmentation.evaluation.MetricFactory;

public class TestBoundarySimilarity_Tokens {

	JCas gold, silv;

	BoundarySimilarity bd;

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
		createAnnotation(gold, 0, 0, SegmentBoundary.class);
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 28, 28, SegmentBoundary.class);

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
				MetricFactory.getMetric(BoundarySimilarity.class,
						SegmentBoundary.class);
	}

	@Test
	public void testNoSilverBreak() {
		assertEquals(0.0,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testPerfect() {
		createAnnotation(silv, 0, 0, SegmentBoundary.class);
		createAnnotation(silv, 15, 15, SegmentBoundary.class);
		createAnnotation(silv, 28, 28, SegmentBoundary.class);

		assertEquals(1.0,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testOneMissed() {
		createAnnotation(silv, 0, 0, SegmentBoundary.class);
		createAnnotation(silv, 28, 28, SegmentBoundary.class);

		assertEquals(0.5,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testOneMisplaced() {
		createAnnotation(silv, 0, 0, SegmentBoundary.class);
		createAnnotation(silv, 28, 28, SegmentBoundary.class);

		// misplaced
		createAnnotation(silv, 18, 18, SegmentBoundary.class);

		assertEquals(0.75,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}
}
