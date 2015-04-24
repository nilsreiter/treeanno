package de.nilsreiter.segmentation.evaluation;

import java.io.File;

import org.kohsuke.args4j.Option;

public class EvaluationOptions {
	enum EvaluationMetric {
		WindowDiff, BreakDiff, WinPR
	}

	@Option(name = "--silver", required = true)
	File silverFile;

	@Option(name = "--gold", required = true)
	File goldFile;

	@Option(name = "--metric", required = false)
	EvaluationMetric metric = EvaluationMetric.WindowDiff;

	public File getSilverFile() {
		return silverFile;
	}

	public void setSilverFile(File silverFile) {
		this.silverFile = silverFile;
	}

	public File getGoldFile() {
		return goldFile;
	}

	public void setGoldFile(File goldFile) {
		this.goldFile = goldFile;
	}

	public EvaluationMetric getMetric() {
		return metric;
	}

	public void setMetric(EvaluationMetric metric) {
		this.metric = metric;
	}
}
