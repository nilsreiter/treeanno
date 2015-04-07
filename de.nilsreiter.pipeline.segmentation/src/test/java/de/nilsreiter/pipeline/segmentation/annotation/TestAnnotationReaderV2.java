package de.nilsreiter.pipeline.segmentation.annotation;

import static org.junit.Assert.assertTrue;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterator;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class TestAnnotationReaderV2 {

	JCas jcas;

	@Before
	public void setUp() throws ResourceInitializationException {
		JCasIterator jcasIterator =
				SimplePipeline.iteratePipeline(
						CollectionReaderFactory.createReaderDescription(
								TextReader.class,
								TextReader.PARAM_SOURCE_LOCATION,
								"src/test/resources/AnnotationV2/txt/*.txt",
								TextReader.PARAM_LANGUAGE, "en")).iterator();
		jcas = jcasIterator.next();
	}

	@Test
	public void testAnnotationReaderV2() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(AnnotationReaderV2.class,
						AnnotationReaderV2.PARAM_DIRECTORY_NAME,
						"src/test/resources/AnnotationV2/anno",
						AnnotationReaderV2.PARAM_FILE_SUFFIX, ".HF.v2.txt"),
				AnalysisEngineFactory.createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION,
						"src/test/resources/AnnotationV2/xmi"));
		assertTrue(JCasUtil.exists(jcas, SegmentBoundary.class));

	}
}
