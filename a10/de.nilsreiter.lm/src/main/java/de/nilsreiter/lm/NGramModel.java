package de.nilsreiter.lm;

import java.util.List;

public interface NGramModel<T> {

	double getProbability(List<T> list);

	double getProbability(T[] list);

	double getProbability(T next, List<T> history);

	double getProbability(T next, T[] history);

	int getSize();

	int getN();

}
