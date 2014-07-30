package de.nilsreiter.lm.impl;

import java.util.LinkedList;

import de.nilsreiter.lm.Trainer;

public abstract class AbstractTrainer_impl<S, T> implements Trainer<S, T> {

	@Override
	public S train(T[] input) {
		LinkedList<T> list = new LinkedList<T>();
		for (T elem : input) {
			list.add(elem);
		}
		return train(list);
	}

}
