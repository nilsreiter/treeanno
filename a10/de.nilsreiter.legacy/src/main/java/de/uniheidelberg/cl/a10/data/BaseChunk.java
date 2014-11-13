package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.reiter.util.Range;

public class BaseChunk extends Range {
	BaseSentence sentence;
	String category;
	BaseToken head;

	public BaseChunk(final BaseSentence bs, final Integer e1, final Integer e2) {
		super(e1, e2);
		this.sentence = bs;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(final String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('[').append(this.getCategory()).append(' ');
		for (int i = this.getElement1(); i <= this.getElement2(); i++) {
			BaseToken bt = this.sentence.get(i);
			if (bt == this.head) {
				b.append('_');
			}
			b.append(bt.getWord());
			if (bt == this.head) {
				b.append('_');
			}
			b.append(' ');
		}
		b.deleteCharAt(b.length() - 1);
		b.append(']');
		return b.toString();
	}

	/**
	 * @return the head
	 */
	public BaseToken getHead() {
		return head;
	}

	/**
	 * @param head
	 *            the head to set
	 */
	public void setHead(final BaseToken head) {
		this.head = head;
	}

}
