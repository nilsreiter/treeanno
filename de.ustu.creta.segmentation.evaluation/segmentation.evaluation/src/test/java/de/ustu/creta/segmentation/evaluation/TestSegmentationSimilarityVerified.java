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
import de.ustu.creta.segmentation.evaluation.FournierMetric.Transposition;
import de.ustu.creta.segmentation.evaluation.impl.AbstractFournierMetric.Substitution;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationUtil;

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

		List<Substitution> potSubst = bd.getPotentialSubstitions2(boundaries);
		assertEquals(2, potSubst.size());
		Counter<Transposition> tps = bd.getTranspositions2(potSubst);
		assertEquals(1, tps.size());
		assertEquals(0.5, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.9444, bd.score(silv, gold), 1e-3);

		bd.setWindowSize(1);

		boundaries = bd.getBoundaries(new int[] { 6, 4 }, new int[] { 5, 5 });

		potSubst = bd.getPotentialSubstitions2(boundaries);
		assertEquals(2, potSubst.size());
		tps = bd.getTranspositions2(potSubst);
		assertEquals(0, tps.size());
		assertEquals(2.0, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.7777, bd.score(silv, gold), 1e-3);
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

		List<Substitution> potSubst = bd.getPotentialSubstitions2(boundaries);
		assertEquals(2, potSubst.size());
		Counter<Transposition> tps = bd.getTranspositions2(potSubst);
		assertEquals(1, tps.size());
		assertEquals(0.5, bd.getEditDistance(gold, silv), 1e-3);
		assertEquals(0.9444, bd.score(silv, gold), 1e-3);
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

		assertEquals(2.5, bd.getEditDistance(silv, gold), 1e-3);
		assertEquals(0.7222, bd.score(silv, gold), 1e-3);
	}

	/**
	 * <pre>
	 * >>> ds = Dataset(
	 *    {'text':
	 *       {'1':(1,2,2,2,2,1),
	 *        '2':(2,2,2,2,2)
	 *       }
	 *    })
	 * >>> segeval.segmentation_similarity(ds['text']['1'],ds['text']['2'])
	 * Decimal('0.66666666')
	 * </pre>
	 */
	@Test
	public void test4() {
		for (int i = 1; i < text.length(); i++) {
			if (i % 2 == 0)
				createAnnotation(gold, i, i, SegmentBoundary.class);
			else
				createAnnotation(silv, i, i, SegmentBoundary.class);
		}
		boolean[][] boundaries =
				bd.getBoundaries(SegmentationUtil.getMassTuple(gold,
						SegmentBoundary.class), SegmentationUtil.getMassTuple(
						silv, SegmentBoundary.class));
		List<Substitution> poSub = bd.getPotentialSubstitions2(boundaries);
		assertEquals(9, poSub.size());
		assertEquals(4, bd.getTranspositions2(poSub).size());
		assertEquals(3, bd.getEditDistance(silv, gold), 1e-3);
		assertEquals(0.6666, bd.score(gold, silv), 1e-3);
	}
}
