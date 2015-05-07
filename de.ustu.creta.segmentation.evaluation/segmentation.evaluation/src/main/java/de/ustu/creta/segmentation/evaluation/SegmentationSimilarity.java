package de.ustu.creta.segmentation.evaluation;

import org.apache.uima.jcas.JCas;

import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl.Transposition;

public interface SegmentationSimilarity extends Metric {
	double getSegmentationSimilarity(JCas jcas1, JCas jcas2);

	void setTranspositionPenaltyFunction(TranspositionWeightingFunction tpf);

	TranspositionWeightingFunction getTranspositionPenaltyFunction();

	public static interface TranspositionWeightingFunction {
		double getWeight(Transposition tp);
	}

	void setWindowSize(int n);

	int getWindowSize();
}
