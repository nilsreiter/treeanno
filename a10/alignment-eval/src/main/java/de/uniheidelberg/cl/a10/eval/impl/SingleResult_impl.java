package de.uniheidelberg.cl.a10.eval.impl;

import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.reiter.statistics.IHasPRF;

public class SingleResult_impl extends AbstractSingleResult_impl implements IHasPRF {

	int count;

	public SingleResult_impl(final String name) {
		super(name);
	}

	public SingleResult_impl(final Object obj) {
		super(obj);
	}

	public SingleResult_impl() {
	}

	@Override
	public double p() {
		return this.getScore(Evaluation.PRECISION);
	}

	@Override
	public double r() {
		return this.getScore(Evaluation.RECALL);
	}

	@Override
	public double f() {
		return this.getScore(Evaluation.FSCORE);
	}

	public int count() {
		return count;
	}

	/**
	 * @param precision
	 *            the precision to set
	 */
	public void setPrecision(final double precision) {
		this.setScore(Evaluation.PRECISION, precision);
	}

	/**
	 * @param recall
	 *            the recall to set
	 */
	public void setRecall(final double recall) {
		this.setScore(Evaluation.RECALL, recall);
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(final int count) {
		this.count = count;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(final String name) {
		this.identifier = name;
	}

	@Override
	public double f(final double beta) {
		return Double.NaN;
	}

}
