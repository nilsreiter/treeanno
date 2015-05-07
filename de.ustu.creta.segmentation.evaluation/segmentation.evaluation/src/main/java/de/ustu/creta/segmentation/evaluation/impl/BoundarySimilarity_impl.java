package de.ustu.creta.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;

public class BoundarySimilarity_impl implements BoundarySimilarity {
	Class<? extends Annotation> annoType;

	public BoundarySimilarity_impl(Class<? extends Annotation> annotationType) {
		annoType = annotationType;
	}

	@Override
	public boolean init(JCas gold) {
		return true;
	}

	@Override
	public double score(JCas gold, JCas silver) {
		return 0.0;
	}

	@Override
	public Map<String, Double> scores(JCas gold, JCas silver) {

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(), score(gold, silver));
		return r;
	}
}
