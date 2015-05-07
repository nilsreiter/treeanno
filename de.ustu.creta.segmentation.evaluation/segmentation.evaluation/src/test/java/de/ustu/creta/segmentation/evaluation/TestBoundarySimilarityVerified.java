package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.impl.BoundarySimilarity_impl;

public class TestBoundarySimilarityVerified {
	JCas gold, silv;

	BoundarySimilarity_impl bd;

	String text = "1234567890";

	@Before
	public void setUp() throws Exception {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);
		for (int i = 0; i < text.length(); i++) {
			createAnnotation(gold, i, i + 1, SegmentationUnit.class);
		}

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);
		for (int i = 0; i < text.length(); i++) {
			createAnnotation(silv, i, i + 1, SegmentationUnit.class);
		}

		bd =
				(BoundarySimilarity_impl) MetricFactory.getMetric(
						BoundarySimilarity.class, SegmentBoundary.class);
	}

	/**
	 * <pre>
	 * segeval.boundary_similarity((6,4), (5,5))
	 * </pre>
	 */
	@Test
	public void test1() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 4, 4, SegmentBoundary.class);

		assertEquals(0.5, bd.score(silv, gold), 1e-3);

		bd.setWindowSize(1);

		assertEquals(0, bd.score(silv, gold), 1e-3);
	}

	/**
	 * <pre>
	 * >>> segeval.boundary_similarity((1,1,4,4), (5,5))
	 * Decimal('0.166666')
	 * </pre>
	 */
	@Test
	public void test3() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 1, 1, SegmentBoundary.class);
		createAnnotation(silv, 2, 2, SegmentBoundary.class);
		createAnnotation(silv, 6, 6, SegmentBoundary.class);

		assertEquals(0.16666, bd.score(silv, gold), 1e-3);
	}

	/**
	 * <pre>
	 * >>> segeval.boundary_similarity((1,2,2,2,2,1), (2,2,2,2,2))
	 * Decimal('0.4')
	 * </pre>
	 */
	@Test
	public void test4() {
		for (int i = 0; i < text.length(); i++) {
			if (i % 2 == 0)
				createAnnotation(gold, i, i, SegmentBoundary.class);
			else
				createAnnotation(silv, i, i, SegmentBoundary.class);
		}

		assertEquals(0.4, bd.score(gold, silv), 1e-3);
	}
}
