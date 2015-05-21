package de.ustu.ims.reiter.tense.annotator;

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

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.ustu.ims.reiter.tense.api.type.Tense;

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
		assertTrue(JCasUtil.exists(jcas, Tense.class));

		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(ETense.PRESENT.toString(), clause.getTense());
		}

		Tense tense = JCasUtil.selectByIndex(jcas, Tense.class, 0);
		assertEquals(3, tense.getBegin());
		assertEquals(19, tense.getEnd());
	}

	@Test
	public void testPast() throws UIMAException {
		JCas jcas =
				getJCas("He had worked. He worked. He was working. He had been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Tense.class));

		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(ETense.PAST.toString(), clause.getTense());
		}

		Tense tense = JCasUtil.selectByIndex(jcas, Tense.class, 0);
		assertEquals(3, tense.getBegin());
		assertEquals(13, tense.getEnd());
	}

	@Test
	public void testFuture() throws UIMAException {
		JCas jcas =
				getJCas("He will work. He is going to work. He will be working. He will have worked. He'll have been working. He will have been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Tense.class));

		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(clause.getCoveredText(), ETense.FUTURE.toString(),
					clause.getTense());
		}
	}

}
