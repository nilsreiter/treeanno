package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import java.util.List;

public interface PairwiseAlgorithm<T> {
	void setSequences(List<T> seq1, List<T> seq2);
}
