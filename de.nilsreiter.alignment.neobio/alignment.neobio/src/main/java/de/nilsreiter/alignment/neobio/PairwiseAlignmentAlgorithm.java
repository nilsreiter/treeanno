package de.nilsreiter.alignment.neobio;

import java.util.List;

import neobio.alignment.IncompatibleScoringSchemeException;

public abstract class PairwiseAlignmentAlgorithm<T> extends
SequenceAlignmentAlgorithm<T, IndividualAlignment> implements
PairwiseAlgorithm<T> {

	ScoringScheme<T> scoring;

	/**
	 * The first sequence of an alignment.
	 */
	protected List<T> seq1;

	/**
	 * The second sequence of an alignment.
	 */
	protected List<T> seq2;

	public double scoreInsertion(final T a)
			throws IncompatibleScoringSchemeException {
		return scoring.scoreInsertion(a);
	}

	public double scoreSubstitution(final T a, final T b)
			throws IncompatibleScoringSchemeException {
		return scoring.scoreSubstitution(a, b);
	}

	public double scoreDeletion(final T a)
			throws IncompatibleScoringSchemeException {
		return scoring.scoreDeletion(a);
	}

	/**
	 * @return the scoring
	 */
	public ScoringScheme<T> getScoring() {
		return scoring;
	}

	/**
	 * @param scoring
	 *            the scoring to set
	 */
	public void setScoring(final ScoringScheme<T> scoring) {
		this.scoring = scoring;
	}

	/**
	 * Helper method to compute the the greater of two values.
	 * 
	 * @param v1
	 *            first value
	 * @param v2
	 *            second value
	 * @return the larger of <CODE>v1</CODE> and <CODE>v2</CODE>
	 */
	protected final int max(final int v1, final int v2) {
		return Math.max(v1, v2);
	}

	/**
	 * Helper method to compute the the greater of three values.
	 * 
	 * @param v1
	 *            first value
	 * @param v2
	 *            second value
	 * @param v3
	 *            third value
	 * @return the larger of <CODE>v1</CODE>, <CODE>v2</CODE> and
	 *         <CODE>v3</CODE>
	 */
	protected final int max(final int v1, final int v2, final int v3) {
		return Math.max(v1, Math.max(v2, v3));
	}

	/**
	 * Helper method to compute the the greater of four values.
	 * 
	 * @param v1
	 *            first value
	 * @param v2
	 *            second value
	 * @param v3
	 *            third value
	 * @param v4
	 *            fourth value
	 * @return the larger of <CODE>v1</CODE>, <CODE>v2</CODE> <CODE>v3</CODE>
	 *         and <CODE>v4</CODE>
	 */
	protected final int max(final int v1, final int v2, final int v3,
			final int v4) {
		int m1 = ((v1 >= v2) ? v1 : v2);
		int m2 = ((v3 >= v4) ? v3 : v4);

		return (m1 >= m2) ? m1 : m2;
	}

	protected final double max(final double... values) {
		double r = values[0];
		for (double d : values) {
			r = Math.max(r, d);
		}
		return r;
	}

	public void setSequences(final List<T> predicates1,
			final List<T> predicates2) {
		// load sequences into instances of CharSequence
		this.seq1 = predicates1;
		this.seq2 = predicates2;

	}

	public abstract PairwiseAlignment<T> computePairwiseAlignment()
			throws IncompatibleScoringSchemeException;

}
