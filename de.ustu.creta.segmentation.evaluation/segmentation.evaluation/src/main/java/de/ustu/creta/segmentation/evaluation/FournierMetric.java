package de.ustu.creta.segmentation.evaluation;

import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl.Transposition;

public interface FournierMetric extends Metric {

	void setTranspositionPenaltyFunction(TranspositionWeightingFunction tpf);

	TranspositionWeightingFunction getTranspositionPenaltyFunction();

	void setWindowSize(int n);

	int getWindowSize();

	public static interface TranspositionWeightingFunction {
		double getWeight(Transposition tp);
	}
}
