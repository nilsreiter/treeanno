package de.ustu.creta.segmentation.evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.ustu.creta.segmentation.evaluation.Metric;
import de.ustu.creta.segmentation.evaluation.MetricFactory;
import de.ustu.creta.segmentation.evaluation.SegmentationSimilarity;

public class TestSegmentationSimilarity {
	JCas gold, silv;

	Metric bd;

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
		AnnotationFactory.createAnnotation(gold, 5, 6, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 20, 21, SegmentBoundary.class);

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);

		bd =
				MetricFactory.getMetric(SegmentationSimilarity.class,
						SegmentBoundary.class);

	}

	@Test
	public void testInit() {
		assertTrue(bd.init(gold));
	}

	@Test
	public void testNoSilverBreak() {
		assertEquals(0.92592,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}
}
