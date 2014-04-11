package de.uniheidelberg.cl.a10.patterns.train;

import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.List;

import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class BMMConfiguration extends SimilarityConfiguration {
	@Option(name = "--greedy", usage = "Enables a greedy merging strategy")
	public boolean greedy = false;

	@Option(name = "--threaded", usage = "Use the threaded variant of bayesian model merging", aliases = { "-m" })
	public boolean threaded = false;

	@Option(name = "--prior", usage = "Set the prior probability for the geometric distribution. Default: 0.95", aliases = { "-p" })
	public double prior = 0.95;

	@Option(name = "--precompute-similarities", usage = "Precomputes all the similarities needed", aliases = { "-ps" })
	public boolean precomputeSimilarities = false;

	@Override
	public String getWikiDescription() {
		StringBuilder b = new StringBuilder(super.getWikiDescription());
		if (threaded)
			b.append("m ");
		if (greedy)
			b.append("g ");
		if (prior != 0.95)
			b.append("p=").append(prior).append(" ");
		return b.toString();

	}

	@Override
	public String getInfoDescription() {
		StringBuilder b = new StringBuilder();
		for (Field f : this.getClass().getDeclaredFields()) {
			b.append("  ");
			b.append(f.getName()).append(": ");
			try {
				b.append(f.get(this)).append("\n");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return b.toString();

	}

	public String getLineDescription() throws IllegalArgumentException,
			IllegalAccessException {
		StringBuilder b = new StringBuilder();
		for (Field f : this.getClass().getDeclaredFields()) {
			Option o = f.getAnnotation(Option.class);
			if (o != null) {
				if (o.multiValued()) {
					List<?> l = (List<?>) f.get(this);
					for (Object obj : l) {
						b.append(o.name()).append(" ");
						b.append(obj).append(" ");
					}
				} else {
					b.append(o.name()).append(" ");
					b.append(f.get(this));
					b.append(" ");
				}
			}

		}
		return b.toString();
	}

	/**
	 * @return the greedy
	 */
	public boolean isGreedy() {
		return greedy;
	}

	/**
	 * @param greedy
	 *            the greedy to set
	 */
	public void setGreedy(final boolean greedy) {
		this.greedy = greedy;
	}

	/**
	 * @return the threaded
	 */
	public boolean isThreaded() {
		return threaded;
	}

	/**
	 * @return the prior
	 */
	public double getPrior() {
		return prior;
	}

	/**
	 * @param prior
	 *            the prior to set
	 */
	public void setPrior(final double prior) {
		this.prior = prior;
	}

	/**
	 * @param threaded
	 *            the threaded to set
	 */
	public void setThreaded(final boolean threaded) {
		this.threaded = threaded;
	}

	@Override
	public String getCommandLine() {
		StringBuilder b = new StringBuilder(super.getCommandLine());
		for (Field f : BMMConfiguration.class.getDeclaredFields()) {
			Option opt = f.getAnnotation(Option.class);
			try {
				if (opt != null) {
					if (opt.aliases() != null && opt.aliases().length > 0) {
						b.append(opt.aliases()[0]).append(" ");
					} else {
						b.append(opt.name()).append(" ");
					}

					if (f.get(this) instanceof Double) {
						b.append(format("%1$1.2f", f.getDouble(this))).append(
								' ');
					} else {
						b.append(f.get(this)).append(" ");
					}
				}
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return b.toString();
	}

	@Override
	public String toString() {
		return this.getCommandLine();
	}

	@Override
	public BMMConfiguration clone() throws CloneNotSupportedException {
		return (BMMConfiguration) super.clone();
	}

	public static String format(final String s, final Object... ob) {
		StringBuilder b = new StringBuilder();
		Formatter f = new Formatter(b);
		f.format(s, ob);
		f.close();
		return b.toString();
	}
}
