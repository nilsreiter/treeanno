package de.ustu.creta.segmentation.evaluation;

import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl.Transposition;

public interface SegmentationSimilarity extends Metric {

	void setTranspositionPenaltyFunction(TranspositionWeightingFunction tpf);

	TranspositionWeightingFunction getTranspositionPenaltyFunction();

	public static interface TranspositionWeightingFunction {
		double getWeight(Transposition tp);
	}

	void setWindowSize(int n);

	int getWindowSize();
}
