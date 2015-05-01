package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;
import org.python.core.PyTuple;

import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.ustu.creta.segmentation.evaluation.impl.AbstractSegEvalMetric;
import de.ustu.creta.segmentation.evaluation.impl.BoundarySimilarity_impl;
import de.ustu.creta.segmentation.evaluation.util.SegmentBoundaryAnnotator;

public class TestAbstractSegEvalMetric {
	AbstractSegEvalMetric metric;
	JCas gold;
	String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {
		metric = new BoundarySimilarity_impl(SegmentBoundary.class);

		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);

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
	}

	@Test
	public void testGetMassTuple1() {
		createAnnotation(gold, 0, 0, SegmentBoundary.class);
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 28, 28, SegmentBoundary.class);
		int[] tuple = metric.getMassTuple(gold, SegmentBoundary.class);
		// System.err.println(tuple);

		assertEquals(4, tuple.length);
		assertEquals(0, tuple[0]);
		assertEquals(4, tuple[1]);
		assertEquals(4, tuple[2]);
		assertEquals(0, tuple[3]);
	}

	@Test
	public void testGetMassTuple2() {
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		int[] tuple = metric.getMassTuple(gold, SegmentBoundary.class);
		// System.err.println(tuple);
		assertEquals(4, tuple[0]);
		assertEquals(4, tuple[1]);
	}

	@Test
	public void testGetMassTuple3() {
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 17, 17, SegmentBoundary.class);
		PyTuple tuple = metric.getPyMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);
		assertEquals(4, tuple.__getitem__(0).asInt());
		assertEquals(1, tuple.__getitem__(1).asInt());
		assertEquals(3, tuple.__getitem__(2).asInt());
	}

	@Test
	public void testRealExample() throws UIMAException, IOException {
		JCas jcas1 =
				JCasFactory.createJCas("src/test/resources/xmi/1009.v1.xmi",
						TypeSystemDescriptionFactory
						.createTypeSystemDescription());
		JCas jcas2 =
				JCasFactory.createJCas("src/test/resources/xmi/1009.v2.xmi",
						TypeSystemDescriptionFactory
						.createTypeSystemDescription());

		AnalysisEngineDescription[] aeds =
				new AnalysisEngineDescription[] {
				AnalysisEngineFactory.createEngineDescription(
						SegmentationUnitAnnotator.class,
						SegmentationUnitAnnotator.PARAM_BASE_TYPE,
						Token.class.getCanonicalName()),
						AnalysisEngineFactory.createEngineDescription(
								SegmentBoundaryAnnotator.class,
								SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
								SegmentBoundaryLevel1.class.getCanonicalName()) };

		SimplePipeline.runPipeline(jcas1, aeds);
		SimplePipeline.runPipeline(jcas2, aeds);

		assertFalse(jcas1 == jcas2);
		int[] mt1 = metric.getMassTuple(jcas1, SegmentBoundary.class);
		int[] mt2 = metric.getMassTuple(jcas2, SegmentBoundary.class);

		assertTrue(JCasUtil.exists(jcas1, SegmentBoundary.class));
		assertTrue(JCasUtil.exists(jcas2, SegmentBoundary.class));

		int mt1s = 0;
		for (int i = 0; i < mt1.length; i++) {
			mt1s += mt1[i];
		}
		int mt2s = 0;
		for (int i = 0; i < mt2.length; i++) {
			mt2s += mt2[i];
		}
		assertEquals(mt1s, mt2s);
	}
}
