package de.ustu.creta.segmentation.evaluation;

public interface WindowDifference extends Metric {
	int getWindowSize();

	void setWindowSize(int windowSize);
}
