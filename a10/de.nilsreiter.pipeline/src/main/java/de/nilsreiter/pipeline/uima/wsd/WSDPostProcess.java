package de.nilsreiter.pipeline.uima.wsd;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;

public class WSDPostProcess extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (WSDResult wsdr : JCasUtil.select(jcas, WSDResult.class)) {
			if (wsdr.getWsdItem() != null) {
				wsdr.setBegin(wsdr.getWsdItem().getBegin());
				wsdr.setEnd(wsdr.getWsdItem().getEnd());
			}
		}

	}

}
