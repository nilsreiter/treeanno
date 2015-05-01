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

	public boolean init(JCas gold) {
		return ensureInterpreter();
	}

	public Map<String, Double> score(JCas gold, JCas silver) {
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

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(),
				getPyFunctionValue(goldTuple, silverTuple,
						"segeval.boundary_similarity"));
		return r;
	}

}
