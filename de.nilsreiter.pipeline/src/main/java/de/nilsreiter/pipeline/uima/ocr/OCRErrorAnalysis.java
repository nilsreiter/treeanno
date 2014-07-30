package de.nilsreiter.pipeline.uima.ocr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.OCRError;

public class OCRErrorAnalysis extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Map<String, Set<OCRError>> map = new HashMap<String, Set<OCRError>>();
		for (OCRError error : JCasUtil.select(jcas, OCRError.class)) {
			if (!map.containsKey(error.getCoveredText())) {
				map.put(error.getCoveredText(), new HashSet<OCRError>());
			}
			map.get(error.getCoveredText()).add(error);
		}

		int context = 20;
		for (String s : map.keySet()) {
			if (map.get(s).size() > 1) {
				System.out.println(s);
				for (OCRError oerr : map.get(s)) {
					StringBuilder b = new StringBuilder("    ");
					b.append(jcas.getDocumentText().substring(
							oerr.getBegin() - context, oerr.getBegin()));
					b.append(' ');
					b.append(oerr.getCoveredText());
					b.append(' ');
					b.append(jcas.getDocumentText().substring(oerr.getEnd(),
							oerr.getEnd() + context));
					System.out.println(b.toString().replaceAll("\n", "\\\\n"));
				}
				System.out.println("-----------");
			}
		}
	}
}
