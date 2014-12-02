package de.nilsreiter.pipeline.uima.entitydetection;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordCoreferenceResolver;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestEntityAnnotator {

	JCas jcas;

	@Before
	public void setUp() throws UIMAException {

		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("Peter, the dog, barked. Peter chased the mouse.");
		jcas.setDocumentLanguage("en");

		SimplePipeline
		.runPipeline(
				jcas,
				createEngineDescription(StanfordSegmenter.class),
				createEngineDescription(StanfordPosTagger.class),
				createEngineDescription(StanfordLemmatizer.class),
				createEngineDescription(StanfordNamedEntityRecognizer.class),
				createEngineDescription(StanfordParser.class),
				createEngineDescription(
						StanfordCoreferenceResolver.class,
						StanfordCoreferenceResolver.PARAM_POSTPROCESSING,
						true));

	}

	@Test
	public void testEntityAnnotator() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		SimplePipeline.runPipeline(jcas,
				createEngineDescription(EntityAnnotator.class));

		assertNotNull(jcas);
		assertTrue(JCasUtil.exists(jcas, POS.class));
		assertTrue(JCasUtil.exists(jcas, Dependency.class));
		assertTrue(JCasUtil.exists(jcas, CoreferenceChain.class));
		assertEquals(2, JCasUtil.select(jcas, CoreferenceLink.class).size());
		assertEquals(2, JCasUtil.select(jcas, EntityMention.class).size());
	}
}
