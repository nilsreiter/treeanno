package de.nilsreiter.util;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.util.FastMath;

public class WeightedGeometricMean extends GeometricMean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public double evaluate(double[] values, int begin, int length,
			double[] weights) {

		double r = 1.0;
		double w = 0.0;
		for (int i = begin; i < length; i++) {
			w += weights[i];
			r *= FastMath.pow(values[i], weights[i]);
		}

		return FastMath.pow(r, (1 / w));
	}

}
