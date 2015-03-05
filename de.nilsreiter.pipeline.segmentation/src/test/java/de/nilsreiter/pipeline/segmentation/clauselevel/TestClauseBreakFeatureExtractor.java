package de.nilsreiter.pipeline.segmentation.clauselevel;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestClauseBreakFeatureExtractor {

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
						"TREE"), createEngine(TreeMaker.class),
				createEngine(PrepareClauseAnnotations.class),
				createEngine(TenseIdentifier.class));
	}

	@Test
	public void testSegmenter() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		SimplePipeline.runPipeline(jcas,
				createEngine(ClauseBreakFeatureExtractor.class));
	}
}
