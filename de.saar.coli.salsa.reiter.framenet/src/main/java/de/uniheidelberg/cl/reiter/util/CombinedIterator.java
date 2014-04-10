package de.uniheidelberg.cl.reiter.util;

import java.util.Iterator;

public class CombinedIterator<T> implements Iterator<T> {
    Iterator<? extends T>[] iterators;

    int currentIterator = 0;

    public CombinedIterator() {
	this.iterators = new Iterator[0];
    }

    public CombinedIterator(final Iterator<T>... iterators) {
	this.iterators = iterators;
    }

    public void addIterator(final Iterator<? extends T> iterator) {
	@SuppressWarnings("unchecked")
	Iterator<? extends T>[] newIterators =
		new Iterator[iterators.length + 1];
	int i;
	for (i = 0; i < iterators.length; i++) {
	    newIterators[i] = iterators[i];
	}
	newIterators[i] = iterator;
    }

    @Override
    public boolean hasNext() {
	return iterators[currentIterator].hasNext()
		|| currentIterator < iterators.length;
    }

    @Override
    public T next() {
	if (iterators[currentIterator].hasNext()) {
	    return iterators[currentIterator].next();
	}
	return iterators[++currentIterator].next();
    }

    @Override
    public void remove() {
	throw new UnsupportedOperationException();
    }

}
