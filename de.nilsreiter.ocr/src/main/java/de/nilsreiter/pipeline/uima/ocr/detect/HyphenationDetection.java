package de.nilsreiter.pipeline.uima.ocr.detect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.Hyphenation;

public class HyphenationDetection extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = jcas.getDocumentText();
		Pattern pattern =
				Pattern.compile("\\b\\w+-+\n\\w+\\b", Pattern.MULTILINE);
		Matcher m = pattern.matcher(text);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			Hyphenation err = new Hyphenation(jcas);
			err.setBegin(start);
			err.setEnd(end);
			err.addToIndexes();
		}
	}
}
