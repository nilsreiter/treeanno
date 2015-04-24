package de.ustu.creta.segmentation.evaluation;

public interface SegEvalMetric extends Metric {
	void setMaxNearMiss(int maxNearMiss);

	int getMaxNearMiss();
}
