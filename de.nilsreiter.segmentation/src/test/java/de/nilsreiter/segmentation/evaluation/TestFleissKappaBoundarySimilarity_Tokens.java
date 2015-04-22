package de.nilsreiter.segmentation.evaluation;

import static org.junit.Assert.assertEquals;

import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

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
		AnnotationFactory.createAnnotation(gold, 0, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 15, 16, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 27, 28, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(gold, 0, 3, Token.class);
		AnnotationFactory.createAnnotation(gold, 4, 7, Token.class);
		AnnotationFactory.createAnnotation(gold, 8, 13, Token.class);
		AnnotationFactory.createAnnotation(gold, 13, 14, Token.class);
		AnnotationFactory.createAnnotation(gold, 15, 17, Token.class);
		AnnotationFactory.createAnnotation(gold, 18, 20, Token.class);
		AnnotationFactory.createAnnotation(gold, 21, 27, Token.class);
		AnnotationFactory.createAnnotation(gold, 27, 28, Token.class);

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);

		AnnotationFactory.createAnnotation(silv, 0, 3, Token.class);
		AnnotationFactory.createAnnotation(silv, 4, 7, Token.class);
		AnnotationFactory.createAnnotation(silv, 8, 13, Token.class);
		AnnotationFactory.createAnnotation(silv, 13, 14, Token.class);
		AnnotationFactory.createAnnotation(silv, 15, 17, Token.class);
		AnnotationFactory.createAnnotation(silv, 18, 20, Token.class);
		AnnotationFactory.createAnnotation(silv, 21, 27, Token.class);
		AnnotationFactory.createAnnotation(silv, 27, 28, Token.class);

		bd =
				MetricFactory.getMetric(FleissKappaBoundarySimilarity.class,
						SegmentBoundary.class);
		bd.setPotentialBoundaryType(Token.class);
	}

	@Test
	public void testNoSilverBreak() {
		assertEquals(0.0,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testPerfect() {
		AnnotationFactory.createAnnotation(silv, 0, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 15, 16, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 27, 28, SegmentBoundary.class);

		assertEquals(1.0,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testOneMissed() {
		AnnotationFactory.createAnnotation(silv, 0, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 27, 28, SegmentBoundary.class);

		assertEquals(0.47872,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}

	@Test
	public void testOneMisplaced() {
		AnnotationFactory.createAnnotation(silv, 0, 1, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 18, 20, SegmentBoundary.class);
		AnnotationFactory.createAnnotation(silv, 27, 28, SegmentBoundary.class);

		assertEquals(0.72777,
				bd.score(gold, silv).get(bd.getClass().getSimpleName()), 1e-5);
	}
}
