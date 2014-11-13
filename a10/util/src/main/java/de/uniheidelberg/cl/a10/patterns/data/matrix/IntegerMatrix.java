package de.uniheidelberg.cl.a10.patterns.data.matrix;

/**
 * Represents a matrix with integer values.
 * 
 * @author reiter
 * 
 * @param <R>
 * @param <C>
 */
public interface IntegerMatrix<R, C> extends Matrix<R, C, Integer> {
	@Override
	public Integer get(R r, C c);

	@Override
	public void put(R r, C c, Integer v);
}
