package de.uniheidelberg.cl.a10.patterns.models.benchmarking;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RandomSequenceFactory implements Iterator<List<String>> {
	int sequenceLength;
	int numberOfDifferentItems;

	RandomStringFactory rsf = null;

	public RandomSequenceFactory(final int sequenceLength,
			final int numberOfDifferentItems) {
		this.sequenceLength = sequenceLength;
		this.numberOfDifferentItems = numberOfDifferentItems;
		this.rsf = new RandomStringFactory(this.numberOfDifferentItems,
				this.sequenceLength);
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public List<String> next() {
		List<String> r = new LinkedList<String>();
		while (rsf.hasNext()) {
			r.add(rsf.next());
		}
		rsf.reset();

		return r;
	}

	@Override
	public void remove() {
	}

}
