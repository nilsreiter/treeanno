package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import neobio.alignment.IncompatibleScoringSchemeException;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * This abstract class is the superclass of all scoring schemes. It defines
 * basic operations that must be provided by all subclasses. Scoring schemes are
 * used by sequence alignment algorithms to compute the score of an alignment.
 * 
 * @author Sergio A. de Carvalho Jr.
 * @see PairwiseAlignmentAlgorithm
 */
public abstract class ScoringScheme<T> {
	/**
	 * Determines whether this scoring scheme ignores the case of characters
	 * when computing their score. It is set by the constructor and cannot be
	 * changed afterwards.
	 */
	protected boolean case_sensitive;

	/**
	 * Creates a new instance of an scoring scheme. The case of characters is
	 * significant when subsequently computing their score.
	 */
	public ScoringScheme() {
	}

	/**
	 * Returns the score of a substitution of character <CODE>a</CODE> for
	 * character <CODE>b</CODE> according to this scoring scheme. If this
	 * substitution is not defined, an exception is raised.
	 * 
	 * @param a
	 *            first character
	 * @param b
	 *            second character
	 * @return score of substitution of <CODE>a</CODE> for <CODE>b</CODE>
	 * @throws IncompatibleScoringSchemeException
	 *             if this substitution is not defined
	 */
	public abstract double scoreSubstitution(T a, T b)
			throws IncompatibleScoringSchemeException;

	/**
	 * Returns the score of an insertion of character <CODE>a</CODE> according
	 * to this scoring scheme. If this character is not recognised, an exception
	 * is raised.
	 * 
	 * @param a
	 *            the character to be inserted
	 * @return score of insertion of <CODE>a</CODE>
	 * @throws IncompatibleScoringSchemeException
	 *             if character is not recognised by this scoring scheme
	 */
	public abstract double scoreInsertion(T a)
			throws IncompatibleScoringSchemeException;

	/**
	 * Returns the score of a deletion of character <CODE>a</CODE> according to
	 * this scoring scheme. If this character is not recognised, an exception is
	 * raised.
	 * 
	 * @param a
	 *            the character to be deleted
	 * @return score of insertion of <CODE>a</CODE>
	 * @throws IncompatibleScoringSchemeException
	 *             if character is not recognised by this scoring scheme
	 */
	public abstract double scoreDeletion(T a)
			throws IncompatibleScoringSchemeException;

	/**
	 * Returns the maximum absolute score that this scoring scheme can return
	 * for any substitution, deletion or insertion.
	 * 
	 * @return maximum absolute score that can be returned
	 */
	public abstract int maxAbsoluteScore();

	/**
	 * Returns <CODE>true</CODE> if this scoring scheme supports partial
	 * matches, <CODE>false</CODE> otherwise. A partial match is a situation
	 * when two characters are not equal but, for any reason, are regarded as
	 * similar by this scoring scheme, which then returns a positive score. This
	 * is common when for scoring schemes that implement amino acid scoring
	 * matrices.
	 * 
	 * @return <CODE>true</CODE> if this scoring scheme supports partial
	 *         matches, <CODE>false</CODE> otherwise
	 */
	public abstract boolean isPartialMatchSupported();

	public abstract Probability sim(T a, T b);

}
