package de.nilsreiter.pipeline.tense;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.tense.type.Tense;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestTenseAnnotator {
	AnalysisEngine[] pipeline;

	@Before
	public void setUp() throws UIMAException, IOException {

		pipeline =
				new AnalysisEngine[] { createEngine(StanfordSegmenter.class),
				createEngine(StanfordPosTagger.class),
						createEngine(TenseAnnotator.class) };
	}

	public JCas getJCas(String text) throws UIMAException {
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");
		return jcas;
	}

	@Test
	public void testPresent() throws UIMAException {
		JCas jcas =
				getJCas("He has been working. He works. He is working. He is going to the supermarket. He has worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(ETense.PRESENT.toString(), clause.getTense());
		}
	}

	@Test
	public void testPast() throws UIMAException {
		JCas jcas =
				getJCas("He had worked. He worked. He was working. He had been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(ETense.PAST.toString(), clause.getTense());
		}
	}

	@Test
	public void testFuture() throws UIMAException {
		JCas jcas =
				getJCas("He will work. He is going to work. He will be working. He will have worked. He'll have been working. He will have been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(clause.getCoveredText(), ETense.FUTURE.toString(),
					clause.getTense());
		}
	}

}
