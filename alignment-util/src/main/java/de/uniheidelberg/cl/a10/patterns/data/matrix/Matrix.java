package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.util.Set;

/**
 * This interface represents the most abstract matrices.
 * 
 * @author reiter
 * 
 * @param <R>
 *            The class representing the rows
 * @param <C>
 *            The class representing columns
 * @param <V>
 *            The class for values
 */
public interface Matrix<R, C, V> {
	/**
	 * Retrieves the value in row <code>r</code> and column <code>c</code>.
	 * 
	 * @param r
	 *            The row number
	 * @param c
	 *            The column number
	 * @return The value
	 */
	public V get(R r, C c);

	/**
	 * Stores a value in the matrix, overwriting the current value.
	 * 
	 * @param r
	 *            The row number
	 * @param c
	 *            The column number
	 * @param v
	 *            The value
	 */
	public void put(R r, C c, V v);

	/**
	 * Returns the set of row objects of the matrix
	 * 
	 * @return
	 */
	public Set<R> getRows();

	/**
	 * Returns the set of column objects of the matrix
	 * 
	 * @return
	 */
	public Set<C> getColumns();

	/**
	 * Returns the number of columns in the matrix
	 * 
	 * @return a positive number
	 */
	public int getColumnNumber();

	/**
	 * Returns the number of rows in the matrix
	 * 
	 * @return A positive number
	 */
	public int getRowNumber();
}
