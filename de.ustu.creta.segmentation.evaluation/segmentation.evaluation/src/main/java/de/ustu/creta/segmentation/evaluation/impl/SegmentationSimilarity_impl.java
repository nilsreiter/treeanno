package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Map;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.ustu.creta.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl implements SegmentationSimilarity {
	Class<? extends Annotation> annoType;

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {

		annoType = annotationType;
	}

	@Override
	public boolean init(JCas gold) {
		return false;
	}

	@Override
	public Map<String, Double> score(JCas gold, JCas silver) {
		return null;

	}

}
