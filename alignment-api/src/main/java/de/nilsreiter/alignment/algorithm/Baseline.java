package de.nilsreiter.alignment.algorithm;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public interface Baseline<T> extends AlignmentAlgorithm<T> {

	@Override
	Alignment<T> align(String id, List<T> seq1, List<T> seq2);

	@Deprecated
	Alignment<T> getAlignment(List<List<T>> sequences);

	@Deprecated
	static enum Type {
		NoAlignment, Harmonic, SameLemma, WeightedHarmonic, SameSurface
	};
}
