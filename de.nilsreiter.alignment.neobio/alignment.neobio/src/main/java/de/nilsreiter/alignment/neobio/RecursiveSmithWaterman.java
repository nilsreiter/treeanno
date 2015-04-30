package de.nilsreiter.alignment.neobio;

import java.util.LinkedList;
import java.util.List;

import neobio.alignment.IncompatibleScoringSchemeException;

public class RecursiveSmithWaterman<T> extends PairwiseAlignmentAlgorithm<T> {

	List<PairwiseAlignment<T>> alignments = new LinkedList<PairwiseAlignment<T>>();

	@Override
	public PairwiseAlignment<T> computePairwiseAlignment()
			throws IncompatibleScoringSchemeException {

		alignments = this.recurse(seq1, seq2);

		// System.err.println("Found " + alignments.size() + " alignments.");
		return alignments.get(0);
	}

	public List<PairwiseAlignment<T>> recurse(final List<T> seq1,
			final List<T> seq2) throws IncompatibleScoringSchemeException {
		List<PairwiseAlignment<T>> r = new LinkedList<PairwiseAlignment<T>>();
		SmithWaterman<T> sw = new SmithWaterman<T>();
		sw.setScoring(getScoring());
		sw.setSequences(seq1, seq2);
		PairwiseAlignment<T> alignment = sw.computePairwiseAlignment();

		// Split sequences in two parts: left and right of the aligned sub
		// sequence
		if (alignment.score.intValue() > 0) {
			r.add(alignment);

			// left part
			List<T> seq1_left_part = seq1.subList(0, alignment.seq1_start);
			List<T> seq2_left_part = seq2.subList(0, alignment.seq2_start);
			if (seq1_left_part.size() > 3 && seq2_left_part.size() > 3)
				r.addAll(this.recurse(seq1_left_part, seq2_left_part));

			// right part
			List<T> seq1_right_part = seq1.subList(alignment.seq1_end,
					seq1.size());
			List<T> seq2_right_part = seq2.subList(alignment.seq2_end,
					seq2.size());
			if (seq1_right_part.size() > 3 && seq2_right_part.size() > 3)
				r.addAll(this.recurse(seq1_right_part, seq2_right_part));
		}
		return r;
	}

	/**
	 * @return the alignments
	 */
	public List<PairwiseAlignment<T>> getAlignments() {
		return alignments;
	}
}
