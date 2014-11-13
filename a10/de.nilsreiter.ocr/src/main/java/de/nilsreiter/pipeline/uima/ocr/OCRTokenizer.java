package de.nilsreiter.pipeline.uima.ocr;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.ocr.MyStringTokenizer;
import de.nilsreiter.pipeline.uima.ocr.type.Token;

public class OCRTokenizer extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas arg0) throws AnalysisEngineProcessException {
		String text = arg0.getDocumentText();

		MyStringTokenizer st = new MyStringTokenizer(text);
		while (st.hasNext()) {
			st.next();
			Token oToken = new Token(arg0);
			oToken.setBegin(st.getBegin());
			oToken.setEnd(st.getEnd());
			oToken.addToIndexes();
		}
	}

}
