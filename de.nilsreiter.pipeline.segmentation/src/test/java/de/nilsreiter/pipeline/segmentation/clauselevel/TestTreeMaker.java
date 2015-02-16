package de.nilsreiter.pipeline.segmentation.clauselevel;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestTreeMaker {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException, IOException {
		jcas =
				JCasFactory
				.createJCas(
						"/Users/reiterns/Documents/Workspace/Segmentation Corpus/corpus/en/Genette1.txt.xmi",
						TypeSystemDescriptionFactory
						.createTypeSystemDescription());

		SimplePipeline.runPipeline(
				jcas,
				createEngine(StanfordSegmenter.class),
				createEngine(StanfordParser.class, StanfordParser.PARAM_MODE,
						"TREE"));
	}

	@Test
	public void testTreeMaker() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		assertFalse(JCasUtil.exists(jcas, DepRel.class));
		SimplePipeline.runPipeline(jcas, createEngine(TreeMaker.class));
		assertTrue(JCasUtil.exists(jcas, DepRel.class));
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			int roots = 0;
			for (DepRel depRel : JCasUtil.selectCovered(jcas, DepRel.class,
					sentence)) {
				if (depRel.getGovenor() == null) roots++;
				// assertNotNull(depRel.getRelation());
			}
			assertEquals(1, roots);
		}
	}
}
