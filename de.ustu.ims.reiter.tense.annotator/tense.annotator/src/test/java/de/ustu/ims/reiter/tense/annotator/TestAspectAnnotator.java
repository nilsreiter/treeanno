package de.ustu.ims.reiter.tense.annotator;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import de.ustu.ims.reiter.tense.api.type.Aspect;
import de.ustu.ims.reiter.tense.api.type.Perfective;
import de.ustu.ims.reiter.tense.api.type.PerfectiveProgressive;
import de.ustu.ims.reiter.tense.api.type.Progressive;

public class TestAspectAnnotator {
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
	public void testPerfect() throws UIMAException {
		JCas jcas =
				getJCas("He had worked. He has worked. He will have worked.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Perfective.class));

		Perfective aspect = null;

		aspect = JCasUtil.selectByIndex(jcas, Perfective.class, 0);
		assertNotNull(aspect);
		assertEquals(3, aspect.getBegin());
		assertEquals(13, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, Perfective.class, 1);
		assertNotNull(aspect);
		assertEquals(18, aspect.getBegin());
		assertEquals(28, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, Perfective.class, 2);
		assertNotNull(aspect);
		assertEquals(33, aspect.getBegin());
		assertEquals(49, aspect.getEnd());

		assertEquals(3, JCasUtil.select(jcas, Perfective.class).size());

	}

	@Test
	public void testNone() throws UIMAException {
		JCas jcas = getJCas("He worked. He works. He will work. ");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertFalse(JCasUtil.exists(jcas, Aspect.class));

	}

	@Test
	public void testProgressive() throws UIMAException {
		JCas jcas =
				getJCas("He was working. He is working. He is going to work. He is going to the supermarket. He will be working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, Progressive.class));

		Progressive aspect = null;

		aspect = JCasUtil.selectByIndex(jcas, Progressive.class, 0);
		assertNotNull(aspect);
		assertEquals(3, aspect.getBegin());
		assertEquals(14, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, Progressive.class, 1);
		assertNotNull(aspect);
		assertEquals(19, aspect.getBegin());
		assertEquals(29, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, Progressive.class, 2);
		assertNotNull(aspect);
		assertEquals(34, aspect.getBegin());
		assertEquals(42, aspect.getEnd());

		assertEquals(5, JCasUtil.select(jcas, Progressive.class).size());
	}

	@Test
	public void testPerfectProgressive() throws UIMAException {
		JCas jcas =
				getJCas("He had been working. He has been working. He'll have been working. He will have been working.");
		SimplePipeline.runPipeline(jcas, pipeline);
		assertTrue(JCasUtil.exists(jcas, PerfectiveProgressive.class));

		PerfectiveProgressive aspect = null;

		aspect = JCasUtil.selectByIndex(jcas, PerfectiveProgressive.class, 0);
		assertNotNull(aspect);
		assertEquals(3, aspect.getBegin());
		assertEquals(19, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, PerfectiveProgressive.class, 1);
		assertNotNull(aspect);
		assertEquals(24, aspect.getBegin());
		assertEquals(66 - 26, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, PerfectiveProgressive.class, 2);
		assertNotNull(aspect);
		// assertEquals(96 - 26, aspect.getBegin());
		assertEquals(91 - 26, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, PerfectiveProgressive.class, 3);
		assertNotNull(aspect);
		assertEquals(96 - 26, aspect.getBegin());
		assertEquals(118 - 26, aspect.getEnd());

		aspect = JCasUtil.selectByIndex(jcas, PerfectiveProgressive.class, 1);

		assertEquals(4, JCasUtil.select(jcas, PerfectiveProgressive.class)
				.size());

	}

}
