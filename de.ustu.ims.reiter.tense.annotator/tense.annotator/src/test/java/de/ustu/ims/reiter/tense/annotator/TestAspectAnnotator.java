package de.ustu.ims.reiter.tense.annotator;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import de.nilsreiter.util.StringUtil;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.ustu.ims.reiter.tense.api.type.Aspect;
import de.ustu.ims.reiter.tense.api.type.Progressive;

public class TestAspectAnnotator {
	AnalysisEngine[] pipeline;

	@Before
	public void setUp() throws UIMAException, IOException {

		pipeline =
				new AnalysisEngine[] { createEngine(StanfordSegmenter.class),
				createEngine(StanfordPosTagger.class),
						createEngine(AspectAnnotator.class) };
	}

	public JCas getJCas(String text) throws UIMAException {
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");
		return jcas;
	}

	@Test
	public void testPerfect() throws UIMAException {
		JCas jcas =
				getJCas("He had worked. He has worked. He will have worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Aspect.class));

		for (Aspect clause : JCasUtil.select(jcas, Aspect.class)) {
			assertEquals(clause.getCoveredText(),
					StringUtil.toString(EAspect.PERFECTIVE), clause.getAspect());
		}
	}

	@Test
	public void testNone() throws UIMAException {
		JCas jcas = getJCas("He worked. He works. He will work. ");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertFalse(JCasUtil.exists(jcas, Aspect.class));

		for (Aspect clause : JCasUtil.select(jcas, Aspect.class)) {
			System.err.println(clause.getAspect() + "  "
					+ clause.getCoveredText());
		}

	}

	@Test
	public void testProgressive() throws UIMAException {
		JCas jcas =
				getJCas("He was working. He is working. He is going to work. He is going to the supermarket. He will be working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Progressive.class));

		for (Aspect clause : JCasUtil.select(jcas, Progressive.class)) {
			assertEquals(clause.getCoveredText(),
					EAspect.PROGRESSIVE.toString(), clause.getAspect());
		}
	}

	@Test
	public void testPerfectProgressive() throws UIMAException {
		JCas jcas =
				getJCas("He had been working. He has been working. He'll have been working. He will have been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Aspect.class));
		for (Aspect clause : JCasUtil.select(jcas, Aspect.class)) {
			assertEquals(EAspect.PERFECTIVE_PROGRESSIVE.toString(),
					clause.getAspect());
		}
	}

}
