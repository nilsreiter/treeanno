package de.ustu.ims.reiter.tense.experiments;

import java.util.Formatter;

public class ClassificationEvaluation {
	private int tp = 0, fp = 0, fn = 0;

	public double getPrecision() {
		return (double) tp / (tp + fp);
	}

	public double getRecall() {
		return (double) tp / (tp + fn);
	}

	public double getFScore(double beta) {
		double p = getPrecision();
		double r = getRecall();
		return (1 + (beta * beta)) * ((p * r) / ((beta * beta) * p + r));
	}

	public double getFScore() {
		return getFScore(1.0);
	}

	public void tp() {
		tp++;
	};

	public void fp() {
		fp++;
	};

	public void fn() {
		fn++;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		Formatter formatter = new Formatter(b);

		formatter.format("%1$d %2$d %3$d %4$f %5$f %6$f", tp, fp, fn,
				getPrecision(), getRecall(), getFScore());
		formatter.close();
		return b.toString();
	}
}
