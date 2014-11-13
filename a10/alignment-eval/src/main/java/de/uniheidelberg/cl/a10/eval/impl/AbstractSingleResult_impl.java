package de.uniheidelberg.cl.a10.eval.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.eval.SingleResult;

public abstract class AbstractSingleResult_impl implements SingleResult {

	protected Object identifier;

	Map<String, Double> scores = new HashMap<String, Double>();

	public AbstractSingleResult_impl(final String name2) {
		identifier = name2;
	}

	public AbstractSingleResult_impl(final Object name2) {
		identifier = name2;
	}

	public AbstractSingleResult_impl() {
		identifier = null;
	}

	@Override
	public String name() {
		if (identifier == null)
			return null;
		return identifier.toString();
	}

	@Override
	public double getScore(final String measureName) {
		double d = scores.get(measureName);
		if (d == Double.NaN)
			return 0.0;
		return d;
	}

	public void setScore(final String measureName, final double score) {
		scores.put(measureName, score);
	}

	@Override
	public SortedSet<String> getMeasures() {
		return new TreeSet<String>(scores.keySet());
	}

	/**
	 * @return the identifier
	 */
	@Override
	public Object getIdentifier() {
		return identifier;
	}

}
