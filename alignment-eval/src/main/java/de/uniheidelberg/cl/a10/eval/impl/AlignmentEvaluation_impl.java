package de.uniheidelberg.cl.a10.eval.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluation;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.eval.Style;

public abstract class AlignmentEvaluation_impl<T> implements
		AlignmentEvaluation<T> {

	Style evaluationStyle = Style.FRASER;

	Alignment<T> alignmentWithMarking = null;

	protected List<Link<T>> truePositiveList = new LinkedList<Link<T>>();
	protected List<Link<T>> falseNegativeList = new LinkedList<Link<T>>();
	protected List<Link<T>> falsePositiveList = new LinkedList<Link<T>>();

	@Override
	public SingleResult evaluate(final Alignment<T> gold2,
			final Alignment<T> silver) {
		return this.evaluate(gold2, silver, null);
	}

	@Override
	public SingleResult evaluate(final Alignment<T> gold2,
			final Alignment<T> silver, final Object name) {
		Alignment<T> gold = gold2.filter(silver);

		return this.evaluateFiltered(gold, silver, name);
	}

	protected abstract SingleResult evaluateFiltered(final Alignment<T> gold,
			final Alignment<T> silver, final Object name);

	/**
	 * @return the truePositiveList
	 */
	public List<Link<T>> getTruePositiveList() {
		return truePositiveList;
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		truePositiveList.clear();
	}

	/**
	 * @return the falseNegativeList
	 */
	public List<Link<T>> getFalseNegativeList() {
		return falseNegativeList;
	}

	/**
	 * @return the falsePositiveList
	 */
	public List<Link<T>> getFalsePositiveList() {
		return falsePositiveList;
	}

	/**
	 * @return the evaluationStyle
	 */
	public Style getEvaluationStyle() {
		return evaluationStyle;
	}

	/**
	 * @param evaluationStyle
	 *            the evaluationStyle to set
	 */
	public void setEvaluationStyle(final Style evaluationStyle) {
		this.evaluationStyle = evaluationStyle;
	}

	@Override
	public Alignment<T> getAlignmentWithMarking() {
		return alignmentWithMarking;
	}

}
