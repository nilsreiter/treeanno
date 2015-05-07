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

@Deprecated
public class FleissKappaBoundarySimilarity_impl extends AbstractSegEvalMetric
implements FleissKappaBoundarySimilarity {
	Class<? extends Annotation> annoType;

	Logger logger = LoggerFactory.getLogger(getClass());

	public FleissKappaBoundarySimilarity_impl(
			Class<? extends Annotation> annotationType) {
		annoType = annotationType;
		ensureInterpreter();
	}

	@Override
	public boolean init(JCas gold) {
		return ensureInterpreter();
	}

	@Override
	public Map<String, Double> scores(JCas gold, JCas silver) {
		if (!ensureInterpreter()) {
			return new HashMap<String, Double>();
		};
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
				getPyFunctionValueFromDictionary(goldTuple, silverTuple,
						"segeval.fleiss_kappa_linear"));
		return r;
	}

	@Override
	public double score(JCas gold, JCas silver) {
		// TODO Auto-generated method stub
		return 0;
	}

}
