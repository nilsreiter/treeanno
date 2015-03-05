package de.nilsreiter.pipeline.segmentation.clauselevel;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.JCasIterator;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class TestPrepareClauseStartAnnotations {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException, IOException {
		jcas =
				JCasFactory
				.createJCas(
						"/Users/reiterns/Documents/Workspace/Segmentation Corpus/corpus/en/Genette1.txt.xmi",
						TypeSystemDescriptionFactory
						.createTypeSystemDescription());

		CollectionReaderDescription reader =
				CollectionReaderFactory.createReaderDescription(
						XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/Genette1/*.xmi");
		JCasIterator iterator =
				SimplePipeline
				.iteratePipeline(
						reader,
						createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION,
								"src/test/resources/"),
								createEngineDescription(TreeMaker.class),
								createEngineDescription(PrepareClauseAnnotations.class))
								.iterator();
		while (iterator.hasNext()) {
			jcas = iterator.next();
		}

	}

	@Test
	public void testPrepareClauseStartAnnotations()
			throws AnalysisEngineProcessException,
			ResourceInitializationException {
		assertFalse(JCasUtil.exists(jcas, ClauseStart.class));
		SimplePipeline.runPipeline(jcas,
				createEngineDescription(PrepareClauseStartAnnotations.class));
		assertTrue(JCasUtil.exists(jcas, ClauseStart.class));
	}
}
