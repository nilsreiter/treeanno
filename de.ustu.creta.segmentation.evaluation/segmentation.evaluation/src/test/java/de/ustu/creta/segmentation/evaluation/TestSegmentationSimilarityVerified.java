package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.uniheidelberg.cl.reiter.util.Counter;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl.Transposition;

public class TestSegmentationSimilarityVerified {
	JCas gold, silv;

	SegmentationSimilarity_impl bd;

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
				(SegmentationSimilarity_impl) MetricFactory.getMetric(
						SegmentationSimilarity.class, SegmentBoundary.class);
	}

	/**
	 * <pre>
	 * segeval.segmentation_similarity((6,4), (5,5))
	 * </pre>
	 */
	@Test
	public void test1() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 4, 4, SegmentBoundary.class);

		boolean[][] boundaries =
				bd.getBoundaries(new int[] { 6, 4 }, new int[] { 5, 5 });

		List<Integer> potSubst = bd.getPotentialSubstitions(boundaries);
		assertEquals(2, potSubst.size());
		Counter<Transposition> tps = bd.getTranspositions(potSubst);
		assertEquals(1, tps.size());
		assertEquals(0.5, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.9444, bd.getSegmentationSimilarity(silv, gold), 1e-3);

		bd.setWindowSize(1);

		boundaries = bd.getBoundaries(new int[] { 6, 4 }, new int[] { 5, 5 });

		potSubst = bd.getPotentialSubstitions(boundaries);
		assertEquals(2, potSubst.size());
		tps = bd.getTranspositions(potSubst);
		assertEquals(0, tps.size());
		assertEquals(2.0, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.7777, bd.getSegmentationSimilarity(silv, gold), 1e-3);
	}

	/**
	 * <pre>
	 * segeval.segmentation_similarity((6,4), (5,5))
	 * </pre>
	 */
	@Test
	public void test2() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 4, 4, SegmentBoundary.class);

		boolean[][] boundaries =
				bd.getBoundaries(new int[] { 6, 4 }, new int[] { 5, 5 });

		List<Integer> potSubst = bd.getPotentialSubstitions(boundaries);
		assertEquals(2, potSubst.size());
		Counter<Transposition> tps = bd.getTranspositions(potSubst);
		assertEquals(1, tps.size());
		assertEquals(0.5, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.9444, bd.getSegmentationSimilarity(silv, gold), 1e-3);
	}

	/**
	 * <pre>
	 * >>> segeval.segmentation_similarity((1,1,4,4), (5,5))
	 * Decimal('0.7222222222222222222222222222')
	 * </pre>
	 */
	@Test
	public void test3() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 1, 1, SegmentBoundary.class);
		createAnnotation(silv, 2, 2, SegmentBoundary.class);
		createAnnotation(silv, 6, 6, SegmentBoundary.class);

		assertEquals(0.7222, bd.getSegmentationSimilarity(silv, gold), 1e-3);
	}
}
