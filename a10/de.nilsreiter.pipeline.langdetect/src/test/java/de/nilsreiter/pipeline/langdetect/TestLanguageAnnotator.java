package de.nilsreiter.pipeline.langdetect;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TestLanguageAnnotator {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("Bitte informieren Sie sich am info point. Das ist so deep.");
		jcas.setDocumentLanguage("de");

		AnnotationFactory.createAnnotation(jcas, 0, 5, Token.class);
		AnnotationFactory.createAnnotation(jcas, 6, 17, Token.class);
		AnnotationFactory.createAnnotation(jcas, 18, 21, Token.class);
		AnnotationFactory.createAnnotation(jcas, 22, 26, Token.class);
		AnnotationFactory.createAnnotation(jcas, 27, 29, Token.class);
		AnnotationFactory.createAnnotation(jcas, 30, 34, Token.class);
		AnnotationFactory.createAnnotation(jcas, 35, 40, Token.class);
		AnnotationFactory.createAnnotation(jcas, 40, 41, Token.class);
		AnnotationFactory.createAnnotation(jcas, 42, 45, Token.class);
		AnnotationFactory.createAnnotation(jcas, 46, 49, Token.class);
		AnnotationFactory.createAnnotation(jcas, 50, 52, Token.class);
		AnnotationFactory.createAnnotation(jcas, 53, 57, Token.class);
		AnnotationFactory.createAnnotation(jcas, 57, 58, Token.class);

	}

	@Test
	public void testLanguageAnnotator() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		SimplePipeline.runPipeline(jcas,
				createEngineDescription(LanguageAnnotator.class));

	}

}
