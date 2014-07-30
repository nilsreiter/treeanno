package de.uniheidelberg.cl.a10.patterns.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LimitedQueue<T> extends LinkedList<T> implements List<T> {
	private static final long serialVersionUID = 1L;

	int size = 2;

	public LimitedQueue(final int size) {
		this.size = size;
	}

	public LimitedQueue(final int size, final T filler) {
		this.size = size;
		for (int i = 0; i < size; i++) {
			this.add(filler);
		}
	}

	@Override
	public boolean add(final T obj) {
		boolean b = super.add(obj);
		if (size() > size) {
			super.remove(0);
		}
		return b;
	}

	public List<T> getList() {
		return new ArrayList<T>(this);
	}
}
