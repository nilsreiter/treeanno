package de.uniheidelberg.cl.reiter.util;

import java.util.NoSuchElementException;

/**
 * This iterator gives a sequence of double values. By default, the values are
 * between 0.1 and 0.9 and are increased in steps of 0.1. these settings can be
 * overwritten with the constructor
 * {@link #DoubleIterator(double, double, double)}.
 * 
 * @author reiter
 * 
 */
public class DoubleIterator implements ResetableIterator<Double> {
	double current = 0.0;

	double step = 0.1;
	double upper = 1.0;
	double lower = 0.0;

	public DoubleIterator() {

	}

	public DoubleIterator(final double l, final double u, final double s) {
		step = s;
		lower = l;
		upper = u;
	}

	@Override
	public boolean hasNext() {
		return current + step <= upper;
	}

	@Override
	public Double next() {
		current += step;
		if (current > upper)
			throw new NoSuchElementException();
		return current;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset() {
		current = lower;
	}

}