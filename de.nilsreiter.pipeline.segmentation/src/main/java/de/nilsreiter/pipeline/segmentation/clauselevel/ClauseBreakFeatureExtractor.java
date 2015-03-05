package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;

public class ClauseBreakFeatureExtractor extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		List<Clause> clauses = new LinkedList<Clause>();

		// 1. Identify clauses by dependency relations
		for (Clause dep : JCasUtil.select(aJCas, Clause.class)) {
			clauses.add(dep);
		}

		// 2. Extract features for each pair
		for (int i = 1; i < clauses.size(); i++) {
			Clause previous = clauses.get(i - 1);
			Clause current = clauses.get(i);

			Pair<String, String> tenses =
					new Pair<String, String>(previous.getTense(),
							current.getTense());
			System.err.println(tenses.getFirst() + " - " + tenses.getSecond());
		}

	}

}
