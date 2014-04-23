package de.uniheidelberg.cl.a10.patterns.baseline;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public interface Baseline<T> {
	Alignment<T> getAlignment(List<List<T>> sequences);

	static enum Type {
		NoAlignment, Harmonic, SameLemma, WeightedHarmonic, SameSurface
	};
}
