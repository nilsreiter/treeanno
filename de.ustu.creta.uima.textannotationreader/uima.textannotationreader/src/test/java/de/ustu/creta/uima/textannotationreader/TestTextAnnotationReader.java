package de.ustu.creta.uima.textannotationreader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.creta.uima.textannotationreader.test.type.TestType;

public class TestTextAnnotationReader {
	JCas jcas;

	static String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");

		AnnotationFactory.createAnnotation(jcas, 0, text.length(),
				DocumentMetaData.class).setDocumentId("doc1");

	}

	@Test
	public void testTextAnnotationReader()
			throws ResourceInitializationException,
			AnalysisEngineProcessException {
		AnalysisEngineDescription aed =
				AnalysisEngineFactory.createEngineDescription(
						TextAnnotationReader.class,
						TextAnnotationReader.PARAM_DIRECTORY_NAME,
						"src/test/resources",
						TextAnnotationReader.PARAM_FILE_SUFFIX, ".anno",
						TextAnnotationReader.PARAM_ANNOTATION_TYPE,
						TestType.class.getCanonicalName(),
						TextAnnotationReader.PARAM_ANNOTATION_MARK, "b");
		SimplePipeline.runPipeline(jcas, aed);

		assertTrue(JCasUtil.exists(jcas, TestType.class));
		TestType tt;
		tt = JCasUtil.selectByIndex(jcas, TestType.class, 0);
		assertEquals(8, tt.getBegin());
		assertEquals(813, tt.getEnd());
	}
}
