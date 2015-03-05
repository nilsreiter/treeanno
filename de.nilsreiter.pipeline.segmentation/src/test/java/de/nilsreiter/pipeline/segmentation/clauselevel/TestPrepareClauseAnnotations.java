package de.nilsreiter.pipeline.segmentation.clauselevel;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestPrepareClauseAnnotations {
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
				createEngineDescription(StanfordSegmenter.class),
				createEngineDescription(StanfordParser.class,
						StanfordParser.PARAM_MODE, "TREE"),
						createEngineDescription(TreeMaker.class));
	}

	@Test
	public void testPrepareClauseAnnotations() throws UIMAException {
		assertFalse(JCasUtil.exists(jcas, Clause.class));
		SimplePipeline.runPipeline(jcas,
				createEngineDescription(PrepareClauseAnnotations.class));
		assertTrue(JCasUtil.exists(jcas, Clause.class));
		Clause clause = JCasUtil.selectByIndex(jcas, Clause.class, 0);
		assertNull(clause.getTense());
		assertNotNull(clause.getExtent());
		assertEquals(10, clause.getExtent().size());
		Token token;
		token = clause.getExtent(0);
		assertNotNull(token);
	}
}
