package de.nilsreiter.ocr.uima.detect;

import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.Hyphenation;

public class HyphenationDetection extends RegexDetection {

	String newlinePattern = "\n";

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		pattern =
				Pattern.compile("\\b[\\wſ]+-+" + newlinePattern + "[\\wſ]+\\b",
						Pattern.MULTILINE);
		this.markRegex(jcas, Hyphenation.class);

	}
}
