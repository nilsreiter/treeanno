package de.ustu.creta.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyTuple;

import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;

public class BoundarySimilarity_impl extends AbstractSegEvalMetric implements
BoundarySimilarity {
	Class<? extends Annotation> annoType;

	public BoundarySimilarity_impl(Class<? extends Annotation> annotationType) {
		annoType = annotationType;
		ensureInterpreter();
	}

	@Override
	public boolean init(JCas gold) {
		return ensureInterpreter();
	}

	@Override
	public double score(JCas gold, JCas silver) {
		ensureInterpreter();
		PyTuple goldTuple, silverTuple;
		if (JCasUtil.exists(gold, SegmentationUnit.class)
				&& JCasUtil.exists(silver, SegmentationUnit.class)) {
			goldTuple = getPyMassTuple(gold, annoType);
			silverTuple = getPyMassTuple(silver, annoType);
		} else {
			goldTuple =
					getPyMassTuple(JCasUtil.select(gold, annoType), gold
							.getDocumentText().length());
			silverTuple =
					getPyMassTuple(JCasUtil.select(silver, annoType), silver
							.getDocumentText().length());
		}
		System.err.println("goldTuple = " + goldTuple);
		System.err.println("silverTuple = " + silverTuple);

		return getPyFunctionValue(goldTuple, silverTuple,
				"segeval.boundary_similarity");
	}

	@Override
	public Map<String, Double> scores(JCas gold, JCas silver) {

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(), score(gold, silver));
		return r;
	}
}
