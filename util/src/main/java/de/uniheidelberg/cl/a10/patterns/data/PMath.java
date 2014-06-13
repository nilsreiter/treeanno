package de.uniheidelberg.cl.a10.patterns.data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.FastMath;
import org.nevec.rjm.BigDecimalMath;

import de.nilsreiter.util.WeightedGeometricMean;

/**
 * This class is a collection of mathematical operations over Probability
 * values.
 * 
 * @author reiter
 * 
 */
public class PMath {

	/**
	 * Calculates arithmetic average over Probability values.
	 * 
	 * @param ps
	 *            Multiple values
	 * @return
	 */
	@Deprecated
	public static Probability average(final Probability... ps) {
		return Probability.fromProbability(PMath.getSummaryStatistics(ps)
				.getMean());
	}

	/**
	 * Calculates arithmetic mean value over Probability values.
	 * 
	 * @param ps
	 *            Multiple values
	 * @return
	 * @deprecated Use {@link #arithmeticMean(List<Probability>)} instead
	 */
	@Deprecated
	public static Probability average(final List<Probability> ps) {
		return arithmeticMean(ps);
	}

	/**
	 * Calculates arithmetic mean value over Probability values.
	 * 
	 * @param ps
	 *            Multiple values
	 * @return
	 */
	public static Probability arithmeticMean(final List<Probability> ps) {
		if (ps.size() == 0)
			return Probability.NULL;

		return Probability.fromProbability(PMath.getSummaryStatistics(ps)
				.getMean());
	}

	public static SummaryStatistics getSummaryStatistics(
			final List<Probability> ps) {
		SummaryStatistics ss = new SummaryStatistics();
		for (Probability p : ps) {
			ss.addValue(p.getProbability());
		}
		return ss;
	}

	public static SummaryStatistics getSummaryStatistics(
			final Probability... ps) {
		return getSummaryStatistics(Arrays.asList(ps));
	}

	/**
	 * Calculates the geometric mean over Probability values.
	 * 
	 * @param ps
	 * @return
	 */
	public static Probability geometricMean(final List<Probability> ps) {
		if (ps.size() == 0)
			return Probability.NULL;

		SummaryStatistics ss = PMath.getSummaryStatistics(ps);
		return Probability.fromProbability(ss.getGeometricMean());
	}

	/**
	 * Calculates the geometric mean over Probability values.
	 * 
	 * @param ps
	 * @return
	 */
	public static Probability geometricMean(final List<Probability> ps,
			List<Double> weights) {
		if (ps.size() == 0)
			return Probability.NULL;

		double[] values = new double[ps.size()];
		double[] w = new double[ps.size()];
		for (int i = 0; i < ps.size(); i++) {
			values[i] = ps.get(i).getProbability();
			if (weights.size() >= i)
				w[i] = weights.get(i);
			else
				w[i] = 1.0;
		}

		WeightedGeometricMean wgm = new WeightedGeometricMean();
		return Probability.fromProbability(wgm.evaluate(values, 0,
				values.length, w));
	}

	/**
	 * Harmonic mean.
	 * 
	 * @param ps
	 * @return
	 */
	public static Probability harmonicMean(final Collection<Probability> ps) {
		if (ps.size() == 0)
			return Probability.NULL;
		double d = 0.0;
		for (Probability p : ps) {
			d += 1.0 / p.getProbability();
		}
		return Probability.fromProbability(ps.size() / d);
	}

	public static Probability median(final Collection<Probability> ps) {
		if (ps.size() == 0)
			return Probability.NULL;
		Probability[] pss = new Probability[ps.size()];
		int i = 0;
		for (Probability p : ps) {
			pss[i++] = p;
		}
		Arrays.sort(pss);
		int l = pss.length;
		if (pss.length % 2 == 0) {
			return PMath.average(pss[(l / 2) - 1], pss[(l / 2)]);
		}

		return pss[l / 2];
	}

	public static Probability pow(final Probability p, final double n) {
		return Probability.fromLogProbability(p.getLogProbability() * n);
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Probability subtract(final Probability p1,
			final Probability p2) {
		if (p2.isNull()) {
			return p1;
		}
		if (p1.probability < Probability.LIMIT
				|| p2.probability < Probability.LIMIT) {
			BigDecimal bd1 = p1.getBDProbability();
			BigDecimal bd2 = p2.getBDProbability();
			return Probability.fromProbability(BigDecimalMath.subtractRound(
					bd1, bd2));
		}
		return new Probability(FastMath.log(p1.getProbability()
				- p2.getProbability()));
	}

	public static Probability divide(final Probability p1, final Probability p2) {
		if (p2.isNull())
			throw new ProbabilityCalculationException("Division by zero.");

		double l1 = p1.getLogProbability();
		double l2 = p2.getLogProbability();
		double r = l1 - l2;
		if (r > 0.0)
			throw new ProbabilityCalculationException(
					"Result of division isn't a probability.");
		return Probability.fromLogProbability(r);
	}

	public static Probability multiply(final Probability... prob) {
		return multiply(Arrays.asList(prob));
	}

	public static Probability multiply(final List<Probability> prob) {
		double s = 0;
		for (Probability p : prob) {
			if (p != Probability.ONE)
				s += p.getLogProbability();
		}
		return Probability.fromLogProbability(s);

	}

	/**
	 * 
	 * @param probs
	 * @return
	 */
	public static Probability add(final Probability... probs) {
		Probability d = Probability.NULL;
		double dd = 0.0;
		boolean approximate = false;
		for (Probability p : probs) {
			d = Probability.fromLogProbability(logAdd(d.getLogProbability(),
					p.getLogProbability()));
			dd += p.getProbability();
			if (d.getLogProbability() < Probability.LIMIT) {
				approximate = true;
			}
		}
		if (approximate)
			return d;
		return Probability.fromProbability(dd);
	}

	public static Probability max(final Probability p1, final Probability p2) {
		return new Probability(Math.max(p1.getLogProbability(),
				p2.getLogProbability()));
	}

	/**
	 * See https://facwiki.cs.byu.edu/nlp/index.php/Log_Domain_Computations
	 * 
	 * @param logX
	 * @param logY
	 * @return
	 */
	public static double logAdd(double logX, double logY) {
		// 1. make X the max
		if (logY > logX) {
			double temp = logX;
			logX = logY;
			logY = temp;
		}
		// 2. now X is bigger
		if (logX == Double.NEGATIVE_INFINITY) {
			return logX;
		}
		// 3. how far "down" (think decibels) is logY from logX?
		// if it's really small (20 orders of magnitude smaller), then ignore
		double negDiff = logY - logX;
		if (negDiff < -0.0000000001) {
			return logX;
		}
		// 4. otherwise use some nice algebra to stay in the log domain
		// (except for negDiff)
		return logX + java.lang.Math.log(1.0 + java.lang.Math.exp(negDiff));
	}

	public static Probability scale(final Probability min,
			final Probability max, final Probability x) {
		return PMath.divide(
				PMath.multiply(Probability.ONE, PMath.subtract(x, min)),
				PMath.subtract(max, min));
	};
}
