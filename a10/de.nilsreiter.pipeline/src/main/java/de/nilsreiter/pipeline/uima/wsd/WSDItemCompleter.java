package de.nilsreiter.pipeline.uima.wsd;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.wsd.si.POS;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDItem;

public class WSDItemCompleter extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (WSDItem wsditem : JCasUtil.select(jcas, WSDItem.class)) {
			Token token = JCasUtil.selectCovered(Token.class, wsditem).get(0);
			char c = token.getPos().getPosValue().charAt(0);
			switch (c) {
			default:
			case 'n':
			case 'N':
				wsditem.setPos(POS.NOUN.toString());
				break;
			case 'v':
			case 'V':
				wsditem.setPos(POS.VERB.toString());
				break;
			}
		}

	}

}
