package de.uniheidelberg.cl.a10.patterns.sequencealignment;

public abstract class SequenceAlignmentAlgorithm<T, U> {

	protected T gap;
	protected U gapTag;
	protected U matchTag;
	protected U misMatchTag;
	U approximateMatchTag;
	/**
	 * Indicates if the <CODE>MATCH_TAG</CODE> tag should be used or not. If it
	 * is <CODE>true</CODE>, the alignment algorithm should write the
	 * <CODE>MATCH_TAG</CODE> tag in the score tag line of the alignment
	 * whenever a match occurs between characters of the two sequences. If it is
	 * <CODE>false</CODE> the matching character should be written instead. This
	 * flag is updated whenever a scoring scheme is set to this
	 * <CODE>PairwiseAlignmentAlgorithm</CODE> by the
	 * <CODE>setScoringScheme</CODE> method.
	 * 
	 * @see #MATCH_TAG
	 * @see #useMatchTag
	 * @see #setScoringScheme
	 */
	protected boolean useMatchTag = true;

	/**
	 * @return the gap
	 */
	public T getGap() {
		return gap;
	}

	/**
	 * @param gap
	 *            the gap to set
	 */
	public void setGap(final T gap) {
		this.gap = gap;
	}

	/**
	 * @return the gap_tag
	 */
	public U getGapTag() {
		return gapTag;
	}

	/**
	 * @param gap_tag
	 *            the gap_tag to set
	 */
	public void setGapTag(final U gap_tag) {
		this.gapTag = gap_tag;
	}

	/**
	 * @return the matchTag
	 */
	public U getMatchTag() {
		return matchTag;
	}

	/**
	 * @param matchTag
	 *            the matchTag to set
	 */
	public void setMatchTag(final U matchTag) {
		this.matchTag = matchTag;
	}

	/**
	 * @return the approximateMatchTag
	 */
	public U getApproximateMatchTag() {
		return approximateMatchTag;
	}

	/**
	 * @param approximateMatchTag
	 *            the approximateMatchTag to set
	 */
	public void setApproximateMatchTag(final U approximateMatchTag) {
		this.approximateMatchTag = approximateMatchTag;
	}

	/**
	 * @return the misMatchTag
	 */
	public U getMisMatchTag() {
		return misMatchTag;
	}

	/**
	 * @param misMatchTag
	 *            the misMatchTag to set
	 */
	public void setMisMatchTag(final U misMatchTag) {
		this.misMatchTag = misMatchTag;
	}

	/**
	 * @return the useMatchTag
	 */
	public boolean isUseMatchTag() {
		return useMatchTag;
	}

	/**
	 * @param useMatchTag
	 *            the useMatchTag to set
	 */
	public void setUseMatchTag(final boolean useMatchTag) {
		this.useMatchTag = true;
		throw new UnsupportedOperationException();
	}

}
