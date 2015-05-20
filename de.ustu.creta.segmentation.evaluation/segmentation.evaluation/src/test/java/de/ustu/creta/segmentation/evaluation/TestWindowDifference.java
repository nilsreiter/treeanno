package de.ustu.creta.segmentation.evaluation;

import static org.junit.Assert.assertEquals;

import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.ustu.ims.segmentation.type.SegmentBoundary;

/**
 * TODO: Verify results
 * 
 * @author reiterns
 *
 */
public class TestWindowDifference {
	JCas gold, silv;

	Metric bd;

	String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws Exception {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);
		AnnotationFactory.createAnnotation(gold, 5, 6, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 20, 21, SegmentBoundary.class);

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);

		bd =
				MetricFactory.getMetric(WindowDifference.class,
						SegmentBoundary.class);
	}

	@Test
	public void testNoSilverBreak() {
		assertEquals(0.111111,
				bd.scores(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testBeginAndEnd() {
		AnnotationFactory.createAnnotation(silv, 0, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, text.length() - 1,
				text.length(), SegmentBoundary.class);
		assertEquals(0.055555,
				bd.scores(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testEverywhere() {
		for (int i = 0; i < text.length(); i++) {
			AnnotationFactory.createAnnotation(silv, i, i + 1,
					SegmentBoundary.class);
		}
		assertEquals(0.111111,
				bd.scores(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testPerfect() {
		AnnotationFactory.createAnnotation(silv, 5, 6, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 20, 21, SegmentBoundary.class);

		assertEquals(0.0,
				bd.scores(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}
}
