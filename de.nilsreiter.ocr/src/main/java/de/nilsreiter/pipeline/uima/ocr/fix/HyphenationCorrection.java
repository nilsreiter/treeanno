package de.nilsreiter.pipeline.uima.ocr.fix;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.OCRUtil;
import de.nilsreiter.pipeline.uima.ocr.type.Hyphenation;

public class HyphenationCorrection extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Hyphenation hyph : JCasUtil.select(jcas, Hyphenation.class)) {
			String surface =
					hyph.getCoveredText().replaceAll("[\n\r\f\t ]", "");
			String[] parts = surface.split("-+");
			if (parts.length == 2) {
				String whole = parts[0] + parts[1];
				OCRUtil.correct(jcas, hyph, whole).setLevel(3);

			}
		}

	}

}
