package de.nilsreiter.ocr.uima.analysis;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.Token;
import de.uniheidelberg.cl.reiter.util.Counter;

public class FrequencyDictionaryExtractor extends JCasAnnotator_ImplBase {

	Counter<String> tokens = new Counter<String>();

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Token token : JCasUtil.select(jcas, Token.class)) {
			tokens.add(token.getCoveredText());
		}

		StringBuilder b = new StringBuilder();

		for (String s : tokens.keySet()) {
			b.append(s);
			b.append('\t');
			b.append(tokens.get(s));
			b.append('\n');
		}

		try {
			jcas.createView("FrequencyDictionary")
					.setDocumentText(b.toString());
		} catch (CASException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}
	}

}
