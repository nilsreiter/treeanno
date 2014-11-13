package de.nilsreiter.ocr.uima.detect;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;

import java.io.IOException;
import java.io.StringReader;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.OCRError;
import de.nilsreiter.pipeline.uima.ocr.type.Token;
import de.uniheidelberg.cl.reiter.util.Counter;

public class InfrequentWordsMarker extends OCRErrorDetection {

	public static final String PARAM_FREQUENCY_THRESHOLD =
			"Frequency Threshold";

	@ConfigurationParameter(name = PARAM_FREQUENCY_THRESHOLD,
			mandatory = false, defaultValue = "1")
	int limit = 1;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Counter<String> tokens = null;
		try {
			tokens =
					Counter.fromString(new StringReader(jcas.getView(
							"FrequencyDictionary").getDocumentText()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		} catch (CASException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}

		for (Token token : JCasUtil.select(jcas, Token.class)) {
			if (tokens.get(token.getCoveredText()) <= limit) {
				createAnnotation(jcas, token.getBegin(), token.getEnd(),
						OCRError.class);
			}
		}
	}

}
