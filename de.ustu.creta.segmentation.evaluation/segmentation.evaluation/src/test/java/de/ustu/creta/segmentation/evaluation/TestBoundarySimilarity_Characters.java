package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.ustu.creta.segmentation.evaluation.impl.BoundarySimilarity_impl;
import de.ustu.ims.segmentation.type.SegmentBoundary;
import de.ustu.ims.segmentation.type.SegmentationUnit;

public class TestBoundarySimilarity_Characters {

	JCas gold, silv;

	BoundarySimilarity_impl bd;

	String text = "The dog barks.";

	@BeforeClass
	public static void setUpClass() {
		System.setProperty("python.path",
				"src/main/resources/python/segeval-2.0.11");
	}

	@Before
	public void setUp() throws Exception {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);
		AnnotationFactory.createAnnotation(gold, 5, 5, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 13, 13, SegmentBoundary.class);
		for (int i = 0; i < text.length() - 1; i++) {
			createAnnotation(gold, i, i + 1, SegmentationUnit.class);
		}

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);
		for (int i = 0; i < text.length() - 1; i++) {
			createAnnotation(silv, i, i + 1, SegmentationUnit.class);
		}

		bd =
				(BoundarySimilarity_impl) MetricFactory.getMetric(
						BoundarySimilarity.class, SegmentBoundary.class);
	}

	@Test
	public void testNoSilverBreak() {
		assertEquals(0.0,
				bd.scores(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testBeginAndEnd() {
		AnnotationFactory.createAnnotation(silv, 0, 0, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, text.length() - 1,
				text.length() - 1, SegmentBoundary.class);
		assertEquals(0.0,
				bd.scores(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	/**
	 * <pre>
	 * >>> segeval.boundary_similarity((5,8), (1,1,1,1,1,1,1,1,1,1,1,1,1))
	 * Decimal('0.08333333333333333333333333333')	
	 * </pre>
	 */
	@Test
	public void testEverywhere() {
		for (int i = 1; i < text.length(); i++) {
			AnnotationFactory.createAnnotation(silv, i, i,
					SegmentBoundary.class);
		}

		assertEquals(0.083333, bd.score(gold, silv), 1e-5);
	}

	@Test
	public void testPerfect() {
		AnnotationFactory.createAnnotation(silv, 5, 5, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 20, 20, SegmentBoundary.class);

		assertEquals(1.0, bd.score(gold, silv), 1e-5);
	}
}
