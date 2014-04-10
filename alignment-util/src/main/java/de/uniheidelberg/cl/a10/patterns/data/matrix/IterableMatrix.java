package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.util.NavigableSet;

import org.apache.commons.math3.util.Pair;

public interface IterableMatrix<R, C, V extends Comparable<V>> extends
		Matrix<R, C, V> {
	NavigableSet<Pair<R, C>> getNavigableSet();
}
