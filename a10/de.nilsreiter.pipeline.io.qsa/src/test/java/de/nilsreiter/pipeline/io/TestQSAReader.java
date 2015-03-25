package de.nilsreiter.pipeline.io;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.uima.type.qsa.QSAPerson;
import de.nilsreiter.pipeline.uima.type.qsa.Quote;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Heading;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class TestQSAReader {

	CollectionReaderDescription reader;
	AnalysisEngineDescription writer;
	JCas jcas;

	@Before
	public void setUp() throws ResourceInitializationException {
		writer =
				createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						"target/test/resources/data");
		reader =
				createReaderDescription(QSAReader.class,
						QSAReader.PARAM_INPUT_DIRECTORY,
						"src/test/resources/data");
	}

	@Test
	public void testReader() throws ResourceInitializationException,
			UIMAException, IOException {

		jcas = SimplePipeline.iteratePipeline(reader, writer).iterator().next();

		assertTrue(JCasUtil.exists(jcas, QSAPerson.class));
		assertTrue(JCasUtil.exists(jcas, Heading.class));
		assertTrue(JCasUtil.exists(jcas, Paragraph.class));
		assertTrue(JCasUtil.exists(jcas, Quote.class));

		assertTrue(jcas.getDocumentText().startsWith("\nVOLUME I\nCHAPTER I"));

		QSAPerson p = JCasUtil.selectByIndex(jcas, QSAPerson.class, 0);
		assertEquals("I", p.getCoveredText());
		assertEquals("person21", p.getEntityId());
		assertEquals("en", jcas.getDocumentLanguage());
	}

	@Test
	public void testReaderEntireQSC() throws UIMAException, IOException {
		reader =
				createReaderDescription(QSAReader.class,
						QSAReader.PARAM_INPUT_DIRECTORY,
						"/Users/reiterns/Desktop/corpora-DH/Columbia_QSA_Corpus_1.01");
		SimplePipeline.runPipeline(reader, writer);
	}
}
