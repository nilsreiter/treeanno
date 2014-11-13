package de.nilsreiter.ocr.uima.detect;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.ocr.resources.WordList;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;
import de.nilsreiter.pipeline.uima.ocr.type.Token;

public class OCRErrorDetectionWordList extends JCasAnnotator_ImplBase {
	public final static String RESOURCE_WORDLIST = "Word List";

	public static final String PARAM_EXCLUDE_UPPER_CASE = "Exclue Upper Case";
	public static final String PARAM_EXCLUDE_PUNCTUATION = "Exclue Punctuation";
	public static final String PARAM_EXCLUDE_NUMBERS = "Exclue Numbers";

	@ExternalResource(key = RESOURCE_WORDLIST)
	private WordList wordList;

	@ConfigurationParameter(name = PARAM_EXCLUDE_UPPER_CASE)
	boolean excludeUpperCase = false;

	@ConfigurationParameter(name = PARAM_EXCLUDE_PUNCTUATION)
	boolean excludePunctuation = false;

	@ConfigurationParameter(name = PARAM_EXCLUDE_NUMBERS, mandatory = false)
	boolean excludeNumbers = true;

	@Override
	public void process(JCas arg0) throws AnalysisEngineProcessException {
		for (Token oToken : JCasUtil.select(arg0, Token.class)) {
			String token = oToken.getCoveredText();
			if (excludeUpperCase && token.matches("^[A-Z]+$")) {

			} else if (excludePunctuation && token.matches("^\\p{Punct}$")) {

			} else if (excludeNumbers && token.matches("^[0-9]+$")) {

			} else if (!wordList.contains(token)) {
				OCRError error =
						AnnotationFactory.createAnnotation(arg0,
								oToken.getBegin(), oToken.getEnd(),
								OCRError.class);
				error.setDescription("Not in word list");
				error.setDetector(getClass().getCanonicalName());
			}
		}
	}
}
