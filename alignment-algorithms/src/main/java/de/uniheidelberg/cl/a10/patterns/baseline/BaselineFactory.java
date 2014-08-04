package de.uniheidelberg.cl.a10.patterns.baseline;

import de.nilsreiter.alignment.algorithm.Baseline;
import de.nilsreiter.alignment.algorithm.impl.Harmonic_impl;
import de.nilsreiter.alignment.algorithm.impl.NoAlignment_impl;
import de.nilsreiter.alignment.algorithm.impl.SameLemma_impl;
import de.nilsreiter.alignment.algorithm.impl.SameSurface_impl;
import de.nilsreiter.alignment.algorithm.impl.WeightedHarmonic_impl;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.HasTokens;

@Deprecated
public class BaselineFactory<T extends HasDocument & HasTokens> {

	public Baseline<T> getBaseline(final Baseline.Type type) {
		switch (type) {
		case Harmonic:
			return new Harmonic_impl<T>();
		case SameLemma:
			return new SameLemma_impl<T>();
		case WeightedHarmonic:
			return new WeightedHarmonic_impl<T>();
		case SameSurface:
			return new SameSurface_impl<T>();
		default:
			return new NoAlignment_impl<T>();
		}
	}

	public boolean isWeighted(final Baseline.Type type) {
		return type == Baseline.Type.WeightedHarmonic;
	}

}
