package de.uniheidelberg.cl.a10.patterns.train;

import java.util.Iterator;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.Filter;
import de.uniheidelberg.cl.a10.patterns.models.HiddenMarkovModel;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

class PairIterator implements Iterator<Pair<Integer, Integer>> {
	Iterator<Integer> state1Iterator;
	Iterator<Integer> state2Iterator;
	HiddenMarkovModel<?> hmm;
	boolean hasNext = true;

	int state1;
	int state2;

	Filter<Pair<Integer, Integer>> filter = null;

	public PairIterator(final SEHiddenMarkovModel_impl<?> hmm,
			final Filter<Pair<Integer, Integer>> pairFilter) {
		this.state1Iterator = hmm.getStates().iterator();
		this.state2Iterator = hmm.getStates().iterator();
		this.hmm = hmm;
		this.state1 = state1Iterator.next();
		this.state2 = state2Iterator.next();
		this.filter = pairFilter;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	public boolean check(final Pair<Integer, Integer> pair) {

		return (filter == null || filter.check(pair));

	}

	@Override
	public Pair<Integer, Integer> next() {
		while (!check(new Pair<Integer, Integer>(state1, state2))) {
			if (state2Iterator.hasNext()) {
				state2 = state2Iterator.next();
			} else {
				if (state1Iterator.hasNext()) {
					state1 = state1Iterator.next();
					state2Iterator = hmm.getMM().getStates().iterator();
				} else {
					hasNext = false;
				}
			}
		}
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(state1, state2);
		do {
			if (state2Iterator.hasNext()) {
				state2 = state2Iterator.next();
			} else {
				if (state1Iterator.hasNext()) {
					state1 = state1Iterator.next();
					state2Iterator = hmm.getMM().getStates().iterator();
				} else {
					hasNext = false;
				}
			}
		} while (hasNext && !check(new Pair<Integer, Integer>(state1, state2)));
		return pair;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}