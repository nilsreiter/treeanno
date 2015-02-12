package de.nilsreiter.pipeline.segmentation.clauselevel;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestAddRootDependency {

	JCas jcas;

	@Before
	public void setUp() throws Exception {
		jcas = JCasFactory.createJCas();
		InputStreamReader isr =
				new InputStreamReader(getClass().getResourceAsStream(
						"/short_file.txt"));
		StringBuilder b = new StringBuilder();
		while (isr.ready()) {
			b.append((char) isr.read());
		}
		AnnotationFactory.createAnnotation(jcas, 0, b.length(),
				DocumentMetaData.class).setDocumentId("segmentation_test.txt");
		jcas.setDocumentText(b.toString());
		jcas.setDocumentLanguage("en");
		SimplePipeline.runPipeline(
				jcas,
				createEngine(StanfordSegmenter.class),
				createEngine(StanfordParser.class, StanfordParser.PARAM_MODE,
						"TREE"));

	};

	@Test
	public void testAddRootDependency() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		assertTrue(JCasUtil.exists(jcas, Dependency.class));
		SimplePipeline.runPipeline(jcas, createEngine(AddRootDependency.class));
		assertTrue(JCasUtil.exists(jcas, Dependency.class));

		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			int roots = 0;
			Set<Token> rootTokens = new HashSet<Token>();
			for (Token token : JCasUtil.selectCovered(jcas, Token.class,
					sentence)) {
				for (Dependency dep : JCasUtil.selectCovering(jcas,
						Dependency.class, token)) {
					if (dep.getGovernor() == null) {
						roots++;
						rootTokens.add(token);
					}
				}
			}
			assertEquals(sentence.getCoveredText(), 1, roots);
		}
	}
}
