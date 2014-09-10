package de.nilsreiter.ocr.uima.detect;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.ocr.resources.WordList;
import de.nilsreiter.pipeline.uima.ocr.OCRTokenizer;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;

public class TestOCRErrorDetectionWordList {

	JCas jcas;

	AnalysisEngineDescription annotator;

	@Before
	public void setUp() throws Exception {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("Juty 27, Sn Meſſrs. Caleb Howe, Hilkiah\n"
				+ "17550 Grout, and Benjamin Gafiielch\n"
				+ "who had been hoeing corn in the meadow.\n"
				+ "weft of the river. were returning home, a lito\n"
				+ "tle before ſunſet. to a place called Bridgmanis\n"
				+ "Fort, they were fired upon by twelve Indians.\n");

		SimplePipeline.runPipeline(jcas,
				createEngineDescription(OCRTokenizer.class));

		URL url = getClass().getClassLoader().getResource("wordlist_test.txt");
		ExternalResourceDescription wordList =
				createExternalResourceDescription(WordList.class, url);
		annotator =
				createEngineDescription(OCRErrorDetectionWordList.class,
						OCRErrorDetectionWordList.RESOURCE_WORDLIST, wordList,
						OCRErrorDetectionWordList.PARAM_EXCLUDE_UPPER_CASE,
						true,
						OCRErrorDetectionWordList.PARAM_EXCLUDE_PUNCTUATION,
						true);
	}

	@Test
	public void testProcessJCas() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		SimplePipeline.runPipeline(jcas, annotator);
		assertTrue(JCasUtil.exists(jcas, OCRError.class));
		assertEquals(14, JCasUtil.select(jcas, OCRError.class).size());

		OCRError error;
		int i = 0;

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Juty", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Sn", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Meſſrs", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Caleb", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Howe", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Hilkiah", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Grout", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Benjamin", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Gafiielch", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("weft", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("lito", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("tle", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Bridgmanis", error.getCoveredText());

		error = JCasUtil.selectByIndex(jcas, OCRError.class, i++);
		assertEquals("Fort", error.getCoveredText());

	}

}
