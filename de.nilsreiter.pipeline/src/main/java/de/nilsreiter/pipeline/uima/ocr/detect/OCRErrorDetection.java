package de.nilsreiter.pipeline.uima.ocr.detect;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.WordList;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;
import de.nilsreiter.pipeline.uima.ocr.type.Token;

public class OCRErrorDetection extends JCasAnnotator_ImplBase {
	public final static String RESOURCE_WORDLIST = "Word List";

	public static final String PARAM_EXCLUDE_UPPER_CASE = "Exclue Upper Case";

	@ExternalResource(key = RESOURCE_WORDLIST)
	private WordList wordList;

	@ConfigurationParameter(name = PARAM_EXCLUDE_UPPER_CASE)
	boolean excludeUpperCase = false;

	@Override
	public void process(JCas arg0) throws AnalysisEngineProcessException {
		for (Token oToken : JCasUtil.select(arg0, Token.class)) {
			String token = oToken.getCoveredText();
			if (excludeUpperCase && token.matches("^[A-Z].*")) {

			} else if (!wordList.contains(token)) {
				OCRError error = new OCRError(arg0);
				error.setBegin(oToken.getBegin());
				error.setEnd(oToken.getEnd());
				error.addToIndexes();
			}
		}
	}
}
