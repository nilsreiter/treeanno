package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Map;

import org.apache.uima.jcas.JCas;

import de.nilsreiter.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl implements SegmentationSimilarity {

	public boolean init(JCas gold) {
		return false;
	}

	public Map<String, Double> score(JCas gold, JCas silver) {
		return null;
	}

}
