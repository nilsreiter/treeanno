package de.uniheidelberg.cl.a10.patterns.similarity;

import org.apache.commons.math3.analysis.function.Gaussian;

import de.uniheidelberg.cl.a10.Util;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * http://www.cl.uni-heidelberg.de/trac/rituals/wiki/DistanceSimilarity
 * 
 * @author reiter
 * 
 */
public class GaussianDistanceSimilarity extends
		AbstractSimilarityFunction<FrameTokenEvent> {

	public static final long serialVersionUID = 5l;

	double sigma = 0.2;

	Gaussian gauss = null;

	public GaussianDistanceSimilarity(final double mu, final double sigma) {
		gauss = new Gaussian(mu, sigma);
	}

	public GaussianDistanceSimilarity() {
		gauss = new Gaussian(0.0, sigma);
	}

	public GaussianDistanceSimilarity(final double sigma) {
		this.sigma = sigma;
		gauss = new Gaussian(0.0, sigma);
	}

	@Override
	public Probability sim(final FrameTokenEvent arg0, final FrameTokenEvent arg1) {
		/*
		 * if (positivePreCheck(arg0, arg1)) return Probability.ONE; if
		 * (negativePreCheck(arg0, arg1)) return Probability.NULL;
		 */
		Probability p = this.getFromHistory(arg0, arg1);
		if (p != null)
			return p;

		double rpos0 = arg0.position();
		double rpos1 = arg1.position();

		p = this.getProbability(rpos1 - rpos0);
		this.putInHistory(arg0, arg1, p);

		return p;

	}

	public Probability getProbability(final double relativeDistance) {
		double pp = gauss.value(relativeDistance);

		Probability p = Probability.fromProbability(Util.scale(0,
				gauss.value(0.0), 0, 1.0, pp));

		return p;

	}

	@Override
	public void readConfiguration(final SimilarityConfiguration conf) {
		if (conf.sf_gaussiandistance_var != this.sigma) {
			this.sigma = conf.sf_gaussiandistance_var;
			gauss = new Gaussian(0.0, conf.sf_gaussiandistance_var);
		}

	}

	@Override
	public String toString() {
		String s = super.toString() + "." + serialVersionUID + "-" + sigma;

		return s;
	}

	/**
	 * @return the variance
	 */
	public double getVariance() {
		return sigma;
	}

	/**
	 * @param variance
	 *            the variance to set
	 */
	public void setVariance(final double variance) {
		if (variance != this.sigma)
			gauss = new Gaussian(0.0, variance);
		this.sigma = variance;
	}
}
