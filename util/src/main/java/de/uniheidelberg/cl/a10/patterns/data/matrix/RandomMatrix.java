package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.util.Random;
import java.util.Set;

/**
 * This class implements the Matrix interface, but returns a new random number
 * on every call to {@link #get(Object, Object)}. Mainly used for testing and
 * demo purposes. Additional calls to {@link #get(Object, Object)} will produce
 * new random numbers.
 * 
 * @author reiterns
 *
 * @param <R>
 * @param <C>
 */
public class RandomMatrix<R, C> implements Matrix<R, C, Double> {

	Random random;

	public RandomMatrix(Random random) {
		this.random = random;
	}

	@Override
	public Double get(R r, C c) {
		return random.nextDouble();
	}

	@Override
	public void put(R r, C c, Double v) {}

	@Override
	public Set<R> getRows() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<C> getColumns() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnNumber() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRowNumber() {
		throw new UnsupportedOperationException();
	}

}
