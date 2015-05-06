package de.ustu.creta.segmentation.evaluation;

import org.apache.uima.jcas.JCas;

public interface SegmentationSimilarity extends Metric {
	double getSegmentationSimilarity(JCas jcas1, JCas jcas2);
}
