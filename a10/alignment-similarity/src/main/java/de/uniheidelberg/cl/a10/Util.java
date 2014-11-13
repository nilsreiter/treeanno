package de.uniheidelberg.cl.a10;

public class Util {

	/**
	 * This method does mathematical scaling of values, as described <a href =
	 * "http://stackoverflow.com/questions/5294955/how-to-scale-down-a-range-of
	 * -numbers-with-a-known-min-and-max-value">here</a>.
	 * 
	 * @param min
	 *            The minimal value of x
	 * @param max
	 *            The maximal value of x
	 * @param a
	 *            The lower bound of the target range
	 * @param b
	 *            The upper bound of the target range
	 * @param x
	 *            The actual value x
	 * @return A scaled version of x
	 */
	public static double scale(final double min, final double max,
			final double a, final double b, final double x) {
		double r = ((b - a) * (x - min) / (max - min)) + a;
		return r;
	}
}