package de.ustu.creta.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.FleissKappaBoundarySimilarity;

public class FleissKappaBoundarySimilarity_impl extends AbstractSegEvalMetric
		implements FleissKappaBoundarySimilarity {
	Class<? extends Annotation> annoType;

	Logger logger = LoggerFactory.getLogger(getClass());

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
		if (JCasUtil.exists(gold, SegmentationUnit.class)
				&& JCasUtil.exists(silver, SegmentationUnit.class)) {
			goldTuple = getMassTuple(gold, annoType);
			silverTuple = getMassTuple(silver, annoType);
		} else {
			goldTuple =
					getMassTuple(JCasUtil.select(gold, annoType), gold
							.getDocumentText().length());
			silverTuple =
					getMassTuple(JCasUtil.select(silver, annoType), silver
							.getDocumentText().length());
		}

		System.err.println("goldTuple = " + goldTuple);
		System.err.println("silverTuple = " + silverTuple);
		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(),
				getPyFunctionValueFromDictionary(goldTuple, silverTuple,
						"segeval.fleiss_kappa_linear"));
		return r;
	}

}
