package de.nilsreiter.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyTuple;

import de.nilsreiter.segmentation.evaluation.FleissKappaBoundarySimilarity;
import de.nilsreiter.segmentation.evaluation.PotentialBoundarySettable;

public class FleissKappaBoundarySimilarity_impl extends AbstractSegEvalMetric
implements FleissKappaBoundarySimilarity, PotentialBoundarySettable {
	Class<? extends Annotation> annoType;
	Class<? extends Annotation> potentialBoundaryType = null;

	public FleissKappaBoundarySimilarity_impl(
			Class<? extends Annotation> annotationType) {
		annoType = annotationType;
		ensureInterpreter();
	}

	public boolean init(JCas gold) {
		return ensureInterpreter();
	}

	public Map<String, Double> score(JCas gold, JCas silver) {
		if (!ensureInterpreter()) {
			return new HashMap<String, Double>();
		};
		PyTuple goldTuple, silverTuple;
		if (potentialBoundaryType == null) {
			goldTuple =
					getMassTuple(JCasUtil.select(gold, annoType), gold
							.getDocumentText().length());
			silverTuple =
					getMassTuple(JCasUtil.select(silver, annoType), silver
							.getDocumentText().length());
		} else {
			goldTuple = getMassTuple(gold, annoType, potentialBoundaryType);
			silverTuple = getMassTuple(silver, annoType, potentialBoundaryType);
		}

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(),
				getPyFunctionValueFromDictionary(goldTuple, silverTuple,
						"segeval.fleiss_kappa_linear"));
		return r;
	}

	public Class<? extends Annotation> getPotentialBoundaryType() {
		return potentialBoundaryType;
	}

	public void setPotentialBoundaryType(
			Class<? extends Annotation> potentialBoundaryType) {
		this.potentialBoundaryType = potentialBoundaryType;
	}

}