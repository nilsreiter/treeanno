package de.ustu.creta.segmentation.evaluation;

public interface FournierMetric extends Metric {

	void setTranspositionPenaltyFunction(TranspositionWeightingFunction tpf);

	TranspositionWeightingFunction getTranspositionPenaltyFunction();

	void setWindowSize(int n);

	int getWindowSize();

	public static interface TranspositionWeightingFunction {
		double getWeight(Transposition tp);
	}

	public static interface Transposition {
		int getMass();

		int getSource();

		int getTarget();
	}
}
