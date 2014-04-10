package de.uniheidelberg.cl.reiter.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConcatenatingIterator<T> implements ResetableIterator<T> {

	List<ResetableIterator<T>> iterators = new LinkedList<ResetableIterator<T>>();

	int currentIterator = 0;

	public ConcatenatingIterator(
			final Collection<? extends ResetableIterator<T>> iter) {
		iterators.addAll(iter);
	}

	@Override
	public boolean hasNext() {
		return iterators.get(currentIterator).hasNext()
				|| currentIterator < iterators.size() - 1;
	}

	@Override
	public T next() {
		if (iterators.get(currentIterator).hasNext()) {
			return iterators.get(currentIterator).next();
		}
		return iterators.get(++currentIterator).next();
	}

	@Override
	public void remove() {
	}

	@Override
	public synchronized void reset() {
		for (ResetableIterator<T> ri : iterators) {
			ri.reset();
		}
		currentIterator = 0;
	}

}
