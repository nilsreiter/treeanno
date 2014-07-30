package de.nilsreiter.pipeline.uima.ocr.fix;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.OCRUtil;
import de.nilsreiter.pipeline.uima.ocr.SubstitutionRules;
import de.nilsreiter.pipeline.uima.ocr.WordList;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;

public class Substitution extends JCasAnnotator_ImplBase {
	public final static String RESOURCE_WORDLIST = "Word List";
	public final static String RESOURCE_RULES = "Rules List";

	@ExternalResource(key = RESOURCE_WORDLIST)
	private WordList wordList;

	@ExternalResource(key = RESOURCE_RULES)
	SubstitutionRules rules;

	@Override
	public void process(JCas arg0) throws AnalysisEngineProcessException {
		for (OCRError error : JCasUtil.select(arg0, OCRError.class)) {
			String surface = error.getCoveredText();
			for (String key : rules.keySet()) {
				if (surface.contains(key)) {

					String repl = surface.replace(key, rules.get(key));
					if (wordList.contains(repl)) {
						OCRUtil.correct(arg0, error, repl).setLevel(1);
						// error.removeFromIndexes();
					}
					/*
					 * else OCRUtil.correct(arg0, error, repl).setLevel(2);
					 */

				}

			}
		}

	}

}
