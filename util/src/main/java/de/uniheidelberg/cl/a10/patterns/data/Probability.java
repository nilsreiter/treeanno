package de.uniheidelberg.cl.a10.patterns.data;

import java.math.BigDecimal;

import org.apache.commons.math3.util.FastMath;
import org.nevec.rjm.BigDecimalMath;

public class Probability extends Number implements Comparable<Probability> {

	public static Probability NULL = Probability
			.fromLogProbability(Double.NEGATIVE_INFINITY);

	public static Probability ONE = Probability.fromLogProbability(0.0);

	static double LIMIT = -745.0;

	// public static double SAME = 0.0000000000000000000001;

	double probability;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Probability() {
		this.probability = Double.NaN;
	}

	protected Probability(final double prob) {
		this.probability = prob;
	}

	protected Probability(final Probability prob) {
		this.probability = prob.probability;
	}

	private void setProbability(final double p) {
		this.probability = FastMath.log(p);
	}

	private void setProbability(final BigDecimal p) {
		try {
			this.probability = BigDecimalMath.log(p).doubleValue();
		} catch (ArithmeticException e) {
			System.err.println(p);
			System.err.println(e.getMessage());
		}

	};

	private void setLogProbability(final double lp) {
		this.probability = lp;
	}

	public double getProbability() {
		if (this == NULL)
			return 0.0;
		return FastMath.exp(this.probability);
	}

	public BigDecimal getBDProbability() {
		if (this == NULL)
			return BigDecimal.ZERO;
		return BigDecimalMath.exp(new BigDecimal(this.probability));
	}

	public double getLogProbability() {
		return this.probability;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null)
			return false;
		// return (this == other);

		if (other != null && other.getClass() == Probability.class) {
			Probability p = ((Probability) other);
			if (this.probability == p.probability)
				return true;
			/*
			 * if (this.probability - p.probability < SAME) return true; if
			 * (p.probability - this.probability < SAME) return true;
			 */
		}
		return false;

	}

	/**
	 * Generates a string representation for this probability value. If the log
	 * probability is under a certain threshold ({@link #LIMIT}), the
	 * probability value is printed using BigDecimal and BigDecimalMath.
	 * Otherwise, we do a standard conversion into a double value and then to a
	 * string.
	 */
	@Override
	public String toString() {
		if (this.probability < LIMIT
				&& this.probability != Double.NEGATIVE_INFINITY) {
			BigDecimal bd = new BigDecimal(this.probability);
			bd = BigDecimalMath.exp(bd);
			return bd.toString();
		}
		return String.valueOf(this.getProbability());
	}

	@Override
	public double doubleValue() {
		return this.probability;
	}

	@Override
	public float floatValue() {
		return (float) this.probability;
	}

	@Override
	public int intValue() {
		return (int) this.probability;
	}

	@Override
	public long longValue() {

		return (long) this.probability;
	}

	public static Probability fromProbability(final double d) {
		if (Double.isNaN(d) || d == 0.0)
			return NULL;
		if (d == 1.0)
			return ONE;
		if (d > 1.0)
			throw new IllegalArgumentException(
					"Probability can't be greater than 1. " + d + " given.");
		if (d < 0.0)
			throw new IllegalArgumentException(
					"Probability can't be less than 0. " + d + " given.");
		Probability p = new Probability();
		p.setProbability(d);
		return p;
	}

	public static Probability fromProbability(final BigDecimal bd) {
		if (bd == BigDecimal.ZERO || bd.compareTo(BigDecimal.ZERO) == 0)
			return NULL;
		if (bd == BigDecimal.ONE || bd.compareTo(BigDecimal.ONE) == 0)
			return ONE;
		if (bd.compareTo(BigDecimal.ONE) == 1)
			throw new IllegalArgumentException(
					"Probability can't be greater than 1. " + bd + " given.");
		if (bd.compareTo(BigDecimal.ZERO) == -1)
			throw new IllegalArgumentException(
					"Probability can't be less than 0. " + bd + " given.");
		Probability p = new Probability();
		p.setProbability(bd);
		return p;
	}

	public static Probability fromLogProbability(final double d) {
		if (d == Double.NEGATIVE_INFINITY && NULL != null)
			return NULL;
		if ((d == 0.0 || d > -0.0000000000000000000000000001) && ONE != null)
			return ONE;
		Probability p = new Probability();
		p.setLogProbability(d);

		return p;
	}

	public boolean isPositive() {
		return this.probability > NULL.probability;
	}

	public boolean isNull() {
		return this == Probability.NULL;
	}

	public Probability complement() {
		return Probability.fromProbability(1 - this.getProbability());
	}

	@Override
	public int compareTo(final Probability arg0) {
		return Double.compare(this.probability, arg0.probability);
	}

	@Override
	public int hashCode() {
		return new Double(probability).hashCode();
	}

}
