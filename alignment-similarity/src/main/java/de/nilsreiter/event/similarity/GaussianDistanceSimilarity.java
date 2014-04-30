package de.nilsreiter.event.similarity;

import org.apache.commons.math3.analysis.function.Gaussian;

import de.uniheidelberg.cl.a10.Util;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * http://www.cl.uni-heidelberg.de/trac/rituals/wiki/DistanceSimilarity
 * 
 * @author reiter
 * 
 */
public class GaussianDistanceSimilarity implements SimilarityFunction<Event> {

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

	private double rpos(Event event) {
		return (double) event.getRitualDocument().getEvents().indexOf(event)
				/ (double) event.getRitualDocument().getEvents().size();
	}

	@Override
	public Probability sim(final Event arg0, final Event arg1) {
		Probability p = Probability.NULL;
		double rpos0 = rpos(arg0);
		double rpos1 = rpos(arg1);

		p = this.getProbability(rpos1 - rpos0);

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
