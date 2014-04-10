package de.uniheidelberg.cl.a10.patterns.data.matrix;

/**
 * Represents a matrix with double values
 * 
 * @author reiter
 * 
 * @param <R>
 * @param <C>
 */

public interface DoubleMatrix<R, C> extends Matrix<R, C, Double> {
	@Override
	public Double get(R r, C c);

	@Override
	public void put(R r, C c, Double v);
}
