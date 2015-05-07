package de.ustu.creta.segmentation.agreement;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.agreement.impl.CohensKappa_impl;
import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;
import de.ustu.creta.segmentation.evaluation.MetricFactory;
import de.ustu.creta.segmentation.evaluation.SegmentationSimilarity;
import de.ustu.creta.segmentation.evaluation.impl.AbstractFournierMetric;

public class TestCohensKappa {
	JCas gold, silv;

	AbstractFournierMetric bd;

	String text = "123456789012345";
	CohensKappa spi;

	@Before
	public void setUp() throws Exception {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);

		for (int i = 0; i < text.length() - 1; i++) {
			createAnnotation(gold, i, i + 1, SegmentationUnit.class);
		}

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);
		for (int i = 0; i < text.length() - 1; i++) {
			createAnnotation(silv, i, i + 1, SegmentationUnit.class);
		}

		spi = new CohensKappa_impl();
		spi.setObservedAgreementMetric(MetricFactory.getMetric(
				SegmentationSimilarity.class, SegmentBoundary.class));

	}

	@Test
	public void testPerfectAgreements() {
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int r = random.nextInt(text.length() - 2) + 1;
			createAnnotation(gold, r, r, SegmentBoundary.class);
			createAnnotation(silv, r, r, SegmentBoundary.class);

			assertEquals(1.0, spi.agr(gold, silv), 1e-3);
		}
	}

	@Test
	public void testPerfectAgreementsObserved() {
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int r = random.nextInt(text.length() - 2) + 1;
			createAnnotation(gold, r, r, SegmentBoundary.class);
			createAnnotation(silv, r, r, SegmentBoundary.class);

			assertEquals(1.0, spi.getObservedAgreement(gold, silv), 1e-3);
		}
	}

	@Test
	public void testGetChanceAgreement() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 6, 6, SegmentBoundary.class);
		assertEquals(0.0059, spi.getChanceAgreement(gold, silv), 1e-3);

		createAnnotation(silv, 5, 5, SegmentBoundary.class);
		createAnnotation(gold, 6, 6, SegmentBoundary.class);
		assertEquals(0.0237, spi.getChanceAgreement(gold, silv), 1e-3);

	}

	/**
	 * 
	 * <pre>
	 * >>> ds = Dataset(
	 *    {'text':
	 *       {'1':(1,2,2,2,2,2,2,1),
	 *        '2':(2,2,2,2,2,2,2)
	 *       }
	 *    })
	 * >>> segeval.fleiss_kappa_linear(ds)
	 * Decimal('0.2395950506186726659167604050')
	 * </pre>
	 */
	@Test
	public void testFullDisagreement() {
		for (int i = 1; i < text.length() - 1; i++) {
			if (i % 2 == 0)
				createAnnotation(gold, i, i, SegmentBoundary.class);
			else
				createAnnotation(silv, i, i, SegmentBoundary.class);

		}
		assertEquals(0.6923, spi.getObservedAgreement(gold, silv), 1e-4);
		assertEquals(0.5905, spi.agr(gold, silv), 1e-4);

		spi.setObservedAgreementMetric(MetricFactory.getMetric(
				BoundarySimilarity.class, SegmentBoundary.class));
		assertEquals(0.428571, spi.getObservedAgreement(gold, silv), 1e-4);
		assertEquals(0.239595, spi.agr(gold, silv), 1e-4);
	}

	/**
	 * Verified with segeval.
	 * <pre>
	 * >>> dataset3 = Dataset(
	 * {'text':
	 *    {'1':(5,9),
	 *     '2':(6,8)
	 *    }
	 * })
	 * >>> segeval.actual_agreement_linear(dataset3, fnc_compare=segeval.segmentation_similarity)
	 * Decimal('0.9615384615384615384615384615')
	 * >>> segeval.fleiss_kappa_linear(dataset3, fnc_compare=segeval.segmentation_similarity)
	 * Decimal('0.9613095238095238095238095238')
	 * >>> segeval.fleiss_kappa_linear(dataset3)
	 * Decimal('0.4970238095238095238095238095')
	 * </pre>
	 */
	@Test
	public void testNearMiss() {
		createAnnotation(gold, 5, 5, SegmentBoundary.class);
		createAnnotation(silv, 6, 6, SegmentBoundary.class);

		assertEquals(0.961538,
				spi.getObservedAgreementMetric().score(gold, silv), 1e-4);
		assertEquals(0.961538, spi.getObservedAgreement(gold, silv), 1e-4);
		assertEquals(0.961309, spi.agr(gold, silv), 1e-4);

		spi.setObservedAgreementMetric(MetricFactory.getMetric(
				BoundarySimilarity.class, SegmentBoundary.class));
		assertEquals(0.497023, spi.agr(gold, silv), 1e-4);

	}
}
