package de.uniheidelberg.cl.a10.patterns.models.benchmarking;

import java.util.Iterator;
import java.util.Random;

public class RandomStringFactory implements Iterator<String> {
	Random random = new Random();

	int numberOfStrings = Integer.MAX_VALUE;

	int numberOfDifferentStrings = 50;

	int produced = 0;

	public RandomStringFactory() {
		this.numberOfDifferentStrings = 50;
	}

	public RandomStringFactory(final int numberOfDifferentStrings,
			final int numberOfStrings) {
		this.numberOfDifferentStrings = numberOfDifferentStrings;
		this.numberOfStrings = numberOfStrings;
	}

	@Override
	public boolean hasNext() {
		return produced < this.numberOfStrings;
	}

	@Override
	public String next() {
		produced++;
		return String.valueOf(random.nextInt(numberOfDifferentStrings))
				.intern();

	}

	public void reset() {
		produced = 0;
	}

	@Override
	public void remove() {

	}

}