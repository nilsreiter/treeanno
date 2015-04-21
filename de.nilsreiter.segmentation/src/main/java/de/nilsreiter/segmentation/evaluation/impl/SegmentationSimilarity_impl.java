package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Map;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl implements SegmentationSimilarity {
	Class<? extends Annotation> annoType;

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {
		annoType = annotationType;
	}

	public boolean init(JCas gold) {
		// No operation necessary
		return true;
	}

	public Map<String, Double> score(JCas gold, JCas silver) {

		return null;
	}

}
