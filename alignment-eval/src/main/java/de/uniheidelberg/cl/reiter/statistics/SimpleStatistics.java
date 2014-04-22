package de.uniheidelberg.cl.reiter.statistics;

public class SimpleStatistics implements IResult {
	static char separator = '\t';
	public int tp = 0, fp = 0, fn = 0;
	String name;

	public void tp() {
		tp++;
	}

	public void fp() {
		fp++;
	}

	public void fn() {
		fn++;
	}

	@Override
	public double f(final double beta) {
		return Util.fscore(beta, p(), r());
	}

	@Override
	public double p() {
		double p = tp / ((double) tp + (double) fp);
		if (p == Double.NaN)
			p = 0.0;
		return p;
	}

	@Override
	public double r() {
		double r = tp / ((double) tp + (double) fn);
		if (r == Double.NaN)
			r = 0.0;
		return r;
	}

	@Override
	public double f() {
		double f = Util.fscore(1.0, p(), r());
		if (f == Double.NaN)
			f = 0.0;
		return f;
	}

	@Override
	public int count() {
		return tp + fn;
	}

	@Override
	public String name() {
		return name;
	}

	public static String toStaticString() {
		StringBuilder b = new StringBuilder();
		b.append("").append(separator);
		b.append("tp").append(separator);
		b.append("fp").append(separator);
		b.append("fn").append(separator);
		b.append("p").append(separator);
		b.append("r").append(separator);
		b.append("f").append(separator);
		return b.toString();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(name()).append(separator);
		b.append(tp).append(separator);
		b.append(fp).append(separator);
		b.append(fn).append(separator);
		b.append(p()).append(separator);
		b.append(r()).append(separator);
		b.append(f()).append(separator);
		return b.toString();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

}
