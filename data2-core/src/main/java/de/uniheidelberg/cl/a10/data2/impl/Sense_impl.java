package de.uniheidelberg.cl.a10.data2.impl;

import de.uniheidelberg.cl.a10.data2.Sense;


public class Sense_impl extends AnnotationObject_impl implements Sense {

	String wordNetId;

	public Sense_impl(final String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see de.uniheidelberg.cl.a10.data2.impl.Sense#getWordNetId()
	 */
	@Override
	public String getWordNetId() {
		return wordNetId;
	}

	/**
	 * @param wordNetId
	 *            the wordNetId to set
	 */
	public void setWordNetId(final String wordNetId) {
		this.wordNetId = wordNetId;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj.getClass() != Sense_impl.class)
			return false;
		return (this.wordNetId.equalsIgnoreCase(((Sense) obj).getWordNetId()));

	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(this.getId() + ": " + this.getWordNetId());
		return s.toString();
	}

}
