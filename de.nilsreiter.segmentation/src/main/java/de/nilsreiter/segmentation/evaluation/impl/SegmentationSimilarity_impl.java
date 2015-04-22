package de.nilsreiter.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyTuple;

import de.nilsreiter.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl extends AbstractSegEvalMetric
implements SegmentationSimilarity {
	Class<? extends Annotation> annoType;

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {

		annoType = annotationType;
		ensureInterpreter();
	}

	public boolean init(JCas gold) {
		return ensureInterpreter();
	}

	public Map<String, Double> score(JCas gold, JCas silver) {
		ensureInterpreter();

		PyTuple goldTuple =
				getMassTuple(JCasUtil.select(gold, annoType), gold
						.getDocumentText().length());
		PyTuple silverTuple =
				getMassTuple(JCasUtil.select(silver, annoType), silver
						.getDocumentText().length());

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(),
				getPyFunctionValue(goldTuple, silverTuple,
						"segeval.segmentation_similarity"));
		return r;
	}

}
