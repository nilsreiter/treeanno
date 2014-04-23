package de.uniheidelberg.cl.a10.patterns.models;

import java.util.List;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

public interface Trellis<T> {
	Probability p(final HiddenMarkovModel<T> hmm, final List<T> sequence);
}
