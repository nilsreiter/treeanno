package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class implements a matrix based on embedded hash maps. This matrix
 * should be used for sparse maps, as only non-zero entries occupy memory.
 * 
 * @author reiter
 * 
 * @param <R>
 *            The class representing rows.
 * @param <C>
 *            The class representing columns.
 * @param <V>
 *            The class representing values.
 */
public class MapMatrix<R, C, V> implements Matrix<R, C, V>, Serializable {

	private static final long serialVersionUID = 1L;

	Map<R, Map<C, V>> data;

	V defaultValue = null;

	boolean hasDefault = false;

	/**
	 * Constructs a new empty matrix
	 */
	public MapMatrix() {
		data = new HashMap<R, Map<C, V>>();
	}

	public MapMatrix(final V defVale) {
		this.data = new HashMap<R, Map<C, V>>();
		this.defaultValue = defVale;
		this.hasDefault = true;
	}

	/**
	 * Creates a new matrix by copying the given matrix. This is a deep copy!
	 * 
	 * @param matrix
	 */

	public MapMatrix(final MapMatrix<R, C, V> matrix) {
		data = new HashMap<R, Map<C, V>>();
		defaultValue = matrix.defaultValue;
		for (R r : matrix.getRows()) {
			for (C c : matrix.getColumns()) {
				this.put(r, c, matrix.get(r, c));
			}
		}
	}

	@Override
	public V get(final R r, final C c) {
		if (!data.containsKey(r)) {
			if (hasDefault)
				return defaultValue;
			else
				throw new NoSuchElementException(r.toString());
		}
		if (!data.get(r).containsKey(c) && hasDefault) {
			if (hasDefault)
				return defaultValue;
			else
				throw new NoSuchElementException(c.toString());
		}
		return data.get(r).get(c);

	}

	@Override
	public void put(final R r, final C c, final V val) {
		if (this.isDefaultValue(val) && data.containsKey(r)) {
			data.get(r).remove(c);
			return;
		} else if (this.isDefaultValue(val)) {
			return;
		} else if (!data.containsKey(r)) {
			data.put(r, new HashMap<C, V>());
		}
		data.get(r).put(c, val);

	}

	@Override
	public Set<R> getRows() {
		return data.keySet();
	}

	private boolean isDefaultValue(final V val) {
		return (hasDefault && (defaultValue == val || (defaultValue != null && defaultValue
				.equals(val))));
	}

	/**
	 * Returns a map representing the given row. The map associates values of
	 * class <code>C</code> with values of class <code>V</code> (the actual
	 * matrix values).
	 * 
	 * @param row
	 * @return
	 */
	public Map<C, V> getRow(final R row) {

		if (!data.containsKey(row)) {
			return new HashMap<C, V>();
		}
		return data.get(row);

	}

	@Override
	public Set<C> getColumns() {
		Set<C> set = new HashSet<C>();

		for (R r : data.keySet()) {
			set.addAll(data.get(r).keySet());
		}
		return set;

	}

	@Override
	public String toString() {
		return this.data.toString();
	}

	@Override
	public int getColumnNumber() {
		return this.getColumns().size();
	}

	@Override
	public int getRowNumber() {
		return this.getRows().size();
	}

	/**
	 * @return the defaultValue
	 */
	public V getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(final V defaultValue) {
		this.defaultValue = defaultValue;
		this.hasDefault = true;
	}

	public void clear() {
		data = new HashMap<R, Map<C, V>>();
	}
}
