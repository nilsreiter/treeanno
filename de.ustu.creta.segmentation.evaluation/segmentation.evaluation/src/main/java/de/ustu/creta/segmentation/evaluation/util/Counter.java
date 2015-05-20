package de.ustu.creta.segmentation.evaluation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

public class Counter<K> extends HashMap<K, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Counter() {}

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

	public void subtractAll(final Collection<? extends K> arg) {
		for (K k : arg) {
			this.subtract(k);
		}
	}

	public Pair<Integer, Set<K>> getMax() {
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

		return new Pair<Integer, Set<K>>(r, set);
	}

	public Pair<Integer, Set<K>> getMin() {
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

		return new Pair<Integer, Set<K>>(r, set);
	}

	public int getHighestCount() {
		return this.getMax().getFirst();
	}

	public Set<K> getKeysWithMaxCount() {
		return this.getMax().getSecond();
	}

	@Override
	public Integer get(final Object k) {
		if (super.containsKey(k)) {
			return super.get(k);
		}
		return 0;
	}

	public static Counter<String> fromString(Reader r) throws IOException {
		Counter<String> c = new Counter<String>();

		BufferedReader buf = new BufferedReader(r);
		String l;
		while ((l = buf.readLine()) != null) {
			if (l != null) {
				String[] line = l.split("\t");
				c.add(line[0], Integer.valueOf(line[1]));
			}
		}
		buf.close();
		return c;
	}
}
