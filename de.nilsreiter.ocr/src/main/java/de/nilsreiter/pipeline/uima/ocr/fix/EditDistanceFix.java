package de.nilsreiter.pipeline.uima.ocr.fix;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.ocr.resources.WordList;
import de.nilsreiter.pipeline.uima.ocr.OCRUtil;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;

public class EditDistanceFix extends JCasAnnotator_ImplBase {
	public final static String RESOURCE_WORDLIST = "Word List";

	public static final String PARAM_ALLOWED_DISTANCE_PERCENTAGE =
			"Allowed Distance Percentage";

	@ExternalResource(key = RESOURCE_WORDLIST)
	private WordList wordList;

	@ConfigurationParameter(name = PARAM_ALLOWED_DISTANCE_PERCENTAGE,
			mandatory = false, defaultValue = "0.25")
	double allowedDistancePercentage = 0.25;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (OCRError error : JCasUtil.select(jcas, OCRError.class)) {
			int length = error.getEnd() - error.getBegin();
			Set<String> closeWords = new HashSet<String>();
			for (String word : wordList) {
				if (word.length() < length + 1 && word.length() > length - 1) {
					closeWords.add(word);
				}
			}

			for (String cword : closeWords) {
				int lev = OCRUtil.levenshtein(cword, error.getCoveredText());
				if (lev < (length * allowedDistancePercentage)) {
					OCRUtil.correct(jcas, error, cword).setLevel(lev);
				}
			}
		}
	}
}
