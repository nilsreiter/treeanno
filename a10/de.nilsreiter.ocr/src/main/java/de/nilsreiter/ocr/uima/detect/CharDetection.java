package de.nilsreiter.ocr.uima.detect;

import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.OCRError;

public class CharDetection extends RegexDetection {
	public static final String PARAM_CHAR = "Character";

	@ConfigurationParameter(name = PARAM_CHAR)
	char ch;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		pattern = Pattern.compile(String.valueOf(ch));

		this.markRegex(jcas, OCRError.class);
	}

}
