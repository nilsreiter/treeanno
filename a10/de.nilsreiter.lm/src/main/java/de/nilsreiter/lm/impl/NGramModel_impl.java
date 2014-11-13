package de.nilsreiter.lm.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.nilsreiter.lm.NGramModel;
import de.uniheidelberg.cl.reiter.util.Counter;

public class NGramModel_impl<T> extends AbstractModel_impl<T> implements
		Serializable, NGramModel<T> {

	private static final long serialVersionUID = 1L;
	Counter<List<T>> counter;
	int size;
	int n;

	public int getCount(List<T> list) {
		return counter.get(list);
	}

	@Override
	public double getProbability(List<T> list) {
		return (double) counter.get(list) / (double) size;
	}

	@Override
	public double getProbability(T next, List<T> history) {
		List<T> full = new LinkedList<T>();
		full.addAll(history);
		full.add(next);
		// System.err.println("P(" + full + ") = " + getProbability(full));
		return getProbability(full) / getProbability(history);
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getN() {
		return n;
	}
}
