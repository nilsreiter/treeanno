package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a matrix as a fast copy of an existing matrix. This
 * matrix stores a pointer to the original matrix and overwrites with own data.
 * If this matrix is changed, the new value is stored separately from the old
 * matrix.
 * 
 * @author reiter
 * 
 * @param <R>
 *            The row type
 * @param <C>
 *            The column type
 * @param <V>
 *            The value type
 */
public class DiffMatrix<R, C, V> extends MapMatrix<R, C, V> implements
		Matrix<R, C, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MapMatrix<R, C, V> original = null;

	public DiffMatrix(final MapMatrix<R, C, V> original) {
		this.original = original;
	}

	@Override
	public V get(final R r, final C c) {
		if (!data.containsKey(r)) {
			return original.get(r, c);
		}
		if (!data.get(r).containsKey(c)) {
			return original.get(r, c);
		}
		return data.get(r).get(c);
	}

	@Override
	public void put(final R r, final C c, final V v) {
		if (!data.containsKey(r)) {
			data.put(r, new HashMap<C, V>());
		}
		data.get(r).put(c, v);

	}

	@Override
	public Set<R> getRows() {
		Set<R> r = new HashSet<R>();
		r.addAll(data.keySet());
		r.addAll(original.getRows());
		return r;
	}

	@Override
	public Set<C> getColumns() {
		Set<C> set = new HashSet<C>();
		for (R r : data.keySet()) {
			set.addAll(data.get(r).keySet());
		}
		set.addAll(original.getColumns());
		return set;
	}

	@Override
	public Map<C, V> getRow(final R row) {
		Map<C, V> r = original.getRow(row);
		Map<C, V> sr = super.getRow(row);
		for (C c : sr.keySet()) {
			r.put(c, sr.get(c));
		}
		return r;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.original.toString());
		b.append(" + ");
		b.append(super.toString());
		return b.toString();
	}

}
