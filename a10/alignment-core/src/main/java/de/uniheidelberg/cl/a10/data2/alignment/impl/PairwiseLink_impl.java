package de.uniheidelberg.cl.a10.data2.alignment.impl;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.PairwiseLink;

public class PairwiseLink_impl<T extends HasDocument> extends Link_impl<T>
		implements PairwiseLink<T> {

	AlignmentType alignmentType;

	public PairwiseLink_impl(final String id) {
		super(id);
	}

	public PairwiseLink_impl(final String id, final T e1, final T e2,
			final double score) {
		super(id);
		this.elements.add(e1);
		this.elements.add(e2);
		this.score = score;
	}

	@Override
	public T getElement(final int i) {
		return elements.get(i);
	}

	/**
	 * @return the element1
	 */
	@Override
	public T getElement1() {
		return elements.get(0);
	}

	/**
	 * @param element1
	 *            the element1 to set
	 */
	@Override
	public void setElement1(final T element1) {
		this.elements.add(0, element1);
	}

	/**
	 * @return the element2
	 */
	@Override
	public T getElement2() {
		return elements.get(1);
	}

	/**
	 * @param element2
	 *            the element2 to set
	 */
	@Override
	public void setElement2(final T element2) {
		this.elements.add(1, element2);
	}

	/**
	 * @return the alignmentType
	 */

	public AlignmentType getAlignmentType() {
		return alignmentType;
	}

	/**
	 * @param alignmentType
	 *            the alignmentType to set
	 */
	public void setAlignmentType(final AlignmentType alignmentType) {
		this.alignmentType = alignmentType;
	}

	@Override
	public boolean isPairwise() {
		return true;
	}
}
