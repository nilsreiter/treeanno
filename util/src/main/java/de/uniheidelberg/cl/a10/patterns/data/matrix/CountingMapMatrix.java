package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.math3.util.Pair;

public class CountingMapMatrix<R, C, V extends Comparable<V>> extends
		MapMatrix<R, C, V> implements IterableMatrix<R, C, V> {

	private static final long serialVersionUID = 1L;

	TreeSet<Pair<R, C>> orderedPairs = new TreeSet<Pair<R, C>>(
			new Comparator<Pair<R, C>>() {

				@Override
				public int compare(final Pair<R, C> arg0, final Pair<R, C> arg1) {
					V p0 = get(arg0.getKey(), arg0.getValue());
					V p1 = get(arg1.getKey(), arg1.getValue());
					int i = p0.compareTo(p1);
					// this is a little hacky, because we need a complete
					// ordering
					return (i == 0 ? 1 : i);
				}
			});

	public CountingMapMatrix() {
		super();
	}

	public CountingMapMatrix(final V p) {
		super(p);
	}

	@Override
	public void put(final R r, final C c, final V val) {
		super.put(r, c, val);

		orderedPairs.add(new Pair<R, C>(r, c));
	}

	@Override
	public NavigableSet<Pair<R, C>> getNavigableSet() {
		return orderedPairs;
	}

}
