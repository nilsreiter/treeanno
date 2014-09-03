package de.nilsreiter.pipeline.uima.ocr.fix;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.CamelCaseSplitter;
import de.nilsreiter.pipeline.uima.ocr.OCRUtil;
import de.nilsreiter.pipeline.uima.ocr.WordList;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;

public class OCRCamelCaseCorrection extends JCasAnnotator_ImplBase {
	public final static String RESOURCE_WORDLIST = "Word List";

	@ExternalResource(key = RESOURCE_WORDLIST)
	private WordList wordList;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		CamelCaseSplitter ccs = new CamelCaseSplitter();
		for (OCRError error : JCasUtil.select(jcas, OCRError.class)) {
			if (ccs.isCamelCase(error.getCoveredText())) {
				String correctionString =
						ccs.splitAtCaseChange(error.getCoveredText());
				int count = 0;
				for (String part : correctionString.split(" ")) {
					if (wordList.contains(part)) count++;
				}

				if (count > 1) {
					OCRUtil.correct(jcas, error, correctionString);
				}
			}
		}

	}

}
