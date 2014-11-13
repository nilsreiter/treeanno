package de.nilsreiter.pipeline.uima.ocr.fix;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.ocr.resources.WordList;
import de.nilsreiter.pipeline.uima.ocr.OCRUtil;
import de.nilsreiter.pipeline.uima.ocr.type.Hyphenation;

public class HyphenationCorrectionWordList extends JCasAnnotator_ImplBase {
	public final static String RESOURCE_WORDLIST = "Word List";

	@ExternalResource(key = RESOURCE_WORDLIST)
	private WordList wordList;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Hyphenation hyph : JCasUtil.select(jcas, Hyphenation.class)) {
			String surface =
					hyph.getCoveredText().replaceAll("[\n\r\f\t ]", "");
			String[] parts = surface.split("-+");
			if (parts.length == 2) {
				String whole = parts[0] + parts[1];

				// L1: strictest case
				if (wordList.contains(whole) && !wordList.contains(parts[0])
						&& !wordList.contains(parts[1])) {
					OCRUtil.correct(jcas, hyph, whole).setLevel(1);
				}

				// L2: one of the parts may be contained in word list
				else if (wordList.contains(whole)
						&& !(wordList.contains(parts[0]) || wordList
								.contains(parts[1]))) {
					OCRUtil.correct(jcas, hyph, whole).setLevel(2);
				}

				// L3: weakest measure: only the whole needs to be in word list
				else if (wordList.contains(whole)) {
					OCRUtil.correct(jcas, hyph, whole).setLevel(3);
				}
			}
		}

	}

}
