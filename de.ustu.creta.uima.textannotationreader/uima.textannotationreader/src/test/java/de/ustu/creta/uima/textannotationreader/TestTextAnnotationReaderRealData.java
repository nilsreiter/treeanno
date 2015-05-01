package de.ustu.creta.uima.textannotationreader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.ustu.creta.uima.textannotationreader.test.type.TestType;

public class TestTextAnnotationReaderRealData {
	JCas jcas;

	static String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {
		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						TextReader.class, TextReader.PARAM_LANGUAGE, "de",
						TextReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/txt/*.txt");

		JCasIterable iterable =
				SimplePipeline.iteratePipeline(crd, AnalysisEngineFactory
						.createEngineDescription(LanguageToolSegmenter.class));
		jcas = iterable.iterator().next();

	}

	@Test
	public void testTextAnnotationReader()
			throws ResourceInitializationException,
			AnalysisEngineProcessException {
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory.createEngine(
				TextAnnotationReader.class,
				TextAnnotationReader.PARAM_ANNOTATION_MARK, "<b1>",
				TextAnnotationReader.PARAM_ANNOTATION_TYPE,
				TestType.class.getCanonicalName(),
				TextAnnotationReader.PARAM_DIRECTORY_NAME,
				"src/test/resources/anno",
				TextAnnotationReader.PARAM_FILE_SUFFIX, ".anno"),
				AnalysisEngineFactory.createEngine(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						"target/test/resources/xmi"));

		assertTrue(JCasUtil.exists(jcas, TestType.class));
		assertEquals(90, JCasUtil.select(jcas, TestType.class).size());
		TestType tt;
		tt = JCasUtil.selectByIndex(jcas, TestType.class, 0);
		assertEquals(451, tt.getBegin());
		assertEquals(456, tt.getEnd());

		tt = JCasUtil.selectByIndex(jcas, TestType.class, 89);
		assertEquals(64947, tt.getBegin());
		assertEquals(64955, tt.getEnd());
	}
}
