package de.nilsreiter.pipeline.segmentation.clauselevel;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestTenseIdentifier {
	JCas jcas;
	AnalysisEngine[] pipeline;

	@Before
	public void setUp() throws UIMAException, IOException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("He works.");
		jcas.setDocumentLanguage("en");

		pipeline =
				new AnalysisEngine[] {
						createEngine(StanfordSegmenter.class),
						createEngine(StanfordParser.class,
								StanfordParser.PARAM_MODE, "TREE"),
						createEngine(TreeMaker.class),
						createEngine(PrepareClauseAnnotations.class),
						createEngine(TenseIdentifier.class) };
	}

	@Test
	public void testSimplePresent() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("He works.");
		jcas.setDocumentLanguage("en");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Clause.class));
		Clause clause = JCasUtil.select(jcas, Clause.class).iterator().next();
		assertEquals("Simple_Present", clause.getTense());
	}
}
