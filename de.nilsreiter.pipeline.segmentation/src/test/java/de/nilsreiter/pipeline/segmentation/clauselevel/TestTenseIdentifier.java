package de.nilsreiter.pipeline.segmentation.clauselevel;

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

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.nilsreiter.pipeline.tense.EnglishTense;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestTenseIdentifier {
	AnalysisEngine[] pipeline;

	@Before
	public void setUp() throws UIMAException, IOException {

		pipeline =
				new AnalysisEngine[] {
				createEngine(StanfordSegmenter.class),
				createEngine(StanfordParser.class,
						StanfordParser.PARAM_MODE, "TREE"),
						createEngine(TreeMaker.class),
						createEngine(PrepareClauseAnnotations.class),
						createEngine(TenseIdentifier.class) };
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
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Present_Perfect_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimplePastPerfect() throws UIMAException {
		JCas jcas = getJCas("He had worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Simple_Past_Perfect.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimplePresent() throws UIMAException {
		JCas jcas = getJCas("He works.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Simple_Present.toString(), clause.getTense());
		}
	}

	@Test
	public void testPresentProgressive() throws UIMAException {
		JCas jcas = getJCas("He is working. He is going to the supermarket.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Present_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testSimplePast() throws UIMAException {
		JCas jcas = getJCas("He worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Simple_Past.toString(), clause.getTense());
		}
	}

	@Test
	public void testPastProgressive() throws UIMAException {
		JCas jcas = getJCas("He was working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Past_Progressive.toString(), clause.getTense());
		}
	}

	@Test
	public void testPresentPerfect() throws UIMAException {
		JCas jcas = getJCas("He has worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Present_Perfect.toString(), clause.getTense());
		}
	}

	@Test
	public void testPastPerfectProgressive() throws UIMAException {
		JCas jcas = getJCas("He had been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Past_Perfect_Progressive.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testWillFuture() throws UIMAException {
		JCas jcas = getJCas("He will work.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Will_Future.toString(), clause.getTense());
		}
	}

	@Test
	public void testGoingToFuture() throws UIMAException {
		JCas jcas = getJCas("He is going to work.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.going_to_Future.toString(), clause.getTense());
		}
	}

	@Test
	public void testFutureProgressive() throws UIMAException {
		JCas jcas = getJCas("He will be working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Future_Progressive.toString(), clause.getTense());
		}
	}

	@Test
	public void testSimpleFuturePerfect() throws UIMAException {
		JCas jcas = getJCas("He will have worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Simple_Future_Perfect.toString(),
					clause.getTense());
		}
	}

	@Test
	public void testFuturePerfectProgressive() throws UIMAException {
		JCas jcas =
				getJCas("He'll have been working. He will have been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			assertEquals(EnglishTense.Future_Perfect_Progressive.toString(),
					clause.getTense());
		}
	}

}
