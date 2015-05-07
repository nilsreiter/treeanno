package de.ustu.creta.segmentation.evaluation;

@Deprecated
public interface SegEvalMetric extends Metric {
	void setMaxNearMiss(int maxNearMiss);

	int getMaxNearMiss();
}
