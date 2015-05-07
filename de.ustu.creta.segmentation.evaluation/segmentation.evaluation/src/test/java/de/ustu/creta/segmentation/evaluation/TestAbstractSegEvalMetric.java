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

import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationUtil;
import de.ustu.creta.segmentation.evaluation.util.SegmentBoundaryAnnotator;

@Deprecated
public class TestAbstractSegEvalMetric {
	JCas gold;
	String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {

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
	public void testRealExample1() throws UIMAException, IOException {
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
		int[] mt1 = SegmentationUtil.getMassTuple(jcas1, SegmentBoundary.class);
		int[] mt2 = SegmentationUtil.getMassTuple(jcas2, SegmentBoundary.class);

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

	@Test
	public void testRealExample2() throws UIMAException, IOException {
		JCas jcas1 =
				JCasFactory.createJCas("src/test/resources/xmi/1202.v1.xmi",
						TypeSystemDescriptionFactory
								.createTypeSystemDescription());
		JCas jcas2 =
				JCasFactory.createJCas("src/test/resources/xmi/1202.v2.xmi",
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
								SegmentBoundaryLevel1.class.getCanonicalName()),
						AnalysisEngineFactory.createEngineDescription(
										XmiWriter.class,
										XmiWriter.PARAM_TARGET_LOCATION,
										"target/resources/xmi") };

		SimplePipeline.runPipeline(jcas1, aeds);
		SimplePipeline.runPipeline(jcas2, aeds);

		assertFalse(jcas1 == jcas2);
		assertTrue(JCasUtil.exists(jcas1, SegmentationUnit.class));
		assertTrue(JCasUtil.exists(jcas2, SegmentationUnit.class));
		assertTrue(JCasUtil.exists(jcas1, SegmentBoundary.class));
		assertTrue(JCasUtil.exists(jcas2, SegmentBoundary.class));
		int[] mt1 = SegmentationUtil.getMassTuple(jcas1, SegmentBoundary.class);
		int[] mt2 = SegmentationUtil.getMassTuple(jcas2, SegmentBoundary.class);

		int mt1s = 0;
		for (int i = 0; i < mt1.length; i++) {
			mt1s += mt1[i];
		}
		int mt2s = 0;
		for (int i = 0; i < mt2.length; i++) {
			mt2s += mt2[i];
		}
		assertEquals(JCasUtil.select(jcas1, SegmentationUnit.class).size(),
				mt1s);
		assertEquals(JCasUtil.select(jcas2, SegmentationUnit.class).size(),
				mt2s);
		assertEquals(mt1s, mt2s);
	}

	@Test
	public void testRealExample21() throws UIMAException, IOException {

		JCas jcas2 =
				JCasFactory.createJCas("src/test/resources/xmi/1202.v2.xmi",
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
								SegmentBoundaryLevel1.class.getCanonicalName()),
						AnalysisEngineFactory.createEngineDescription(
										XmiWriter.class,
										XmiWriter.PARAM_TARGET_LOCATION,
										"target/resources/xmi") };

		SimplePipeline.runPipeline(jcas2, aeds);

		assertTrue(JCasUtil.exists(jcas2, SegmentationUnit.class));
		assertTrue(JCasUtil.exists(jcas2, SegmentBoundary.class));
		int[] mt2 = SegmentationUtil.getMassTuple(jcas2, SegmentBoundary.class);

	}
}
