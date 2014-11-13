package de.nilsreiter.lm;

import java.util.List;

public interface Trainer<T, S> {
	T train(List<S> input);

	T train(S[] input);
}
