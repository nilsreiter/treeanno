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
	public void testPresentPerfectProgressive() throws UIMAException {
		JCas jcas = getJCas("He has been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Present_Perfect_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimplePastPerfect() throws UIMAException {
		JCas jcas = getJCas("He had worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Simple_Past_Perfect.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimplePresent() throws UIMAException {
		JCas jcas = getJCas("He works.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Simple_Present.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testPresentProgressive() throws UIMAException {
		JCas jcas = getJCas("He is working. He is going to the supermarket.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Present_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimplePast() throws UIMAException {
		JCas jcas = getJCas("He worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Simple_Past.toString(), clause.getTense());
		}
	}

	@Test
	public void testPastProgressive() throws UIMAException {
		JCas jcas = getJCas("He was working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Past_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testPresentPerfect() throws UIMAException {
		JCas jcas = getJCas("He has worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Present_Perfect.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testPastPerfectProgressive() throws UIMAException {
		JCas jcas = getJCas("He had been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Past_Perfect_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testWillFuture() throws UIMAException {
		JCas jcas = getJCas("He will work.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Will_Future.toString(), clause.getTense());
		}
	}

	@Test
	public void testGoingToFuture() throws UIMAException {
		JCas jcas = getJCas("He is going to work.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.going_to_Future.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testFutureProgressive() throws UIMAException {
		JCas jcas = getJCas("He will be working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Future_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimpleFuturePerfect() throws UIMAException {
		JCas jcas = getJCas("He will have worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Simple_Future_Perfect.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testFuturePerfectProgressive() throws UIMAException {
		JCas jcas =
				getJCas("He'll have been working. He will have been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Tense clause : JCasUtil.select(jcas, Tense.class)) {
			assertEquals(EnglishTenseAspect.Future_Perfect_Progressive.toString(),
					clause.getTense());
		}
	}

}
