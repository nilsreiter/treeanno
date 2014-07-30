package de.nilsreiter.lm.impl;

import java.util.Arrays;

import de.nilsreiter.lm.NGramModel;

public abstract class AbstractModel_impl<T> implements NGramModel<T> {
	@Override
	public double getProbability(T[] list) {
		return this.getProbability(Arrays.asList(list));
	}

	@Override
	public double getProbability(T next, T[] history) {
		return this.getProbability(next, Arrays.asList(history));
	}

}
