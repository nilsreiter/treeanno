package de.uniheidelberg.cl.a10.patterns.baseline;

import de.uniheidelberg.cl.a10.HasTarget;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.baseline.impl.Harmonic_impl;
import de.uniheidelberg.cl.a10.patterns.baseline.impl.NoAlignment_impl;
import de.uniheidelberg.cl.a10.patterns.baseline.impl.SameLemma_impl;
import de.uniheidelberg.cl.a10.patterns.baseline.impl.SameSurface_impl;
import de.uniheidelberg.cl.a10.patterns.baseline.impl.WeightedHarmonic_impl;

public class BaselineFactory<T extends HasDocument & HasTarget> {

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
