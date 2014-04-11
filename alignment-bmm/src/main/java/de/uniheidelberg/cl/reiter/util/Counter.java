package de.uniheidelberg.cl.reiter.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Counter<K> extends HashMap<K, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Counter() {
	}

	public Counter(final Counter<K> c) {
		for (K k : c.keySet()) {
			this.put(k, c.get(k));
		}
	}

	public void add(final K k) {
		this.add(k, 1);
	}

	public void add(final K k, final int i) {
		if (!super.containsKey(k)) {
			super.put(k, i);
		} else {
			super.put(k, super.get(k) + i);
		}
	}

	public void subtract(final K k) {
		if (!super.containsKey(k)) {
			super.put(k, -1);
		} else {
			super.put(k, super.get(k) - 1);
		}
	}

	public void addAll(final Collection<? extends K> arg) {
		for (K k : arg) {
			this.add(k);
		}
	}

	public BasicPair<Integer, Set<K>> getMax() {
		HashSet<K> set = new HashSet<K>();

		int r = Integer.MIN_VALUE;
		for (K k : this.keySet()) {
			int i = this.get(k);
			if (i > r) {
				set.clear();
				set.add(k);
				r = i;
			} else if (i == r) {
				set.add(k);
			}
		}

		return new BasicPair<Integer, Set<K>>(r, set);
	}

	public BasicPair<Integer, Set<K>> getMin() {
		HashSet<K> set = new HashSet<K>();

		int r = Integer.MAX_VALUE;
		for (K k : this.keySet()) {
			int i = this.get(k);
			if (i < r) {
				set.clear();
				set.add(k);
				r = i;
			} else if (i == r) {
				set.add(k);
			}
		}

		return new BasicPair<Integer, Set<K>>(r, set);
	}

	public int getHighestCount() {
		return this.getMax().getElement1();
	}

	public Set<K> getKeysWithMaxCount() {
		return this.getMax().getElement2();
	}

	@Override
	public Integer get(final Object k) {
		if (super.containsKey(k)) {
			return super.get(k);
		}
		return 0;
	}
}
