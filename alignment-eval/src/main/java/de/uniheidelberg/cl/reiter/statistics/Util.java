package de.uniheidelberg.cl.reiter.statistics;

public class Util {
	public static double fscore(final double beta, final double precision,
			final double recall) {
		double f = ((1 + beta * beta) * precision * recall)
				/ ((beta * beta * precision) + recall);
		if (Double.isNaN(f))
			return 0.0;
		return f;
	}
}
