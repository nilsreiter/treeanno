package de.nilsreiter.pipeline.uima.event;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import de.nilsreiter.pipeline.uima.event.type.Event;
import de.nilsreiter.pipeline.uima.event.type.Role;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticArgument;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;

public class EventAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (SemanticPredicate pred : JCasUtil.select(jcas,
				SemanticPredicate.class)) {
			Event event = new Event(jcas);
			event.setBegin(pred.getBegin());
			event.setEnd(pred.getEnd());
			event.setName(pred.getCategory());
			event.setArguments(new FSArray(jcas, pred.getArguments().size()));
			event.setAnchor(pred);
			int i = 0;

			for (SemanticArgument arg : JCasUtil.select(pred.getArguments(),
					SemanticArgument.class)) {
				Role role = new Role(jcas);
				role.setBegin(arg.getBegin());
				role.setEnd(arg.getEnd());
				role.setName(arg.getRole());
				role.addToIndexes();
				event.setArguments(i++, role);
			}

			event.addToIndexes();
		}

	}

}
