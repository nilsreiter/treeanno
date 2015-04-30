package de.nilsreiter.alignment.neobio;

import java.util.List;

public interface PairwiseAlgorithm<T> {
	void setSequences(List<T> seq1, List<T> seq2);
}
