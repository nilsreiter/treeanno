package de.uniheidelberg.cl.reiter.util;

import java.util.Collection;
import java.util.Iterator;

public class CollectionIterator<T> implements ResetableIterator<T> {

	Collection<T> collection;

	Iterator<T> iterator;

	public CollectionIterator(final Collection<T> coll) {
		collection = coll;
		this.reset();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		iterator.remove();
	}

	@Override
	public void reset() {
		iterator = collection.iterator();
	}

}
