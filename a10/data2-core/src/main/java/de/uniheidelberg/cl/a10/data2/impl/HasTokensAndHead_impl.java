package de.uniheidelberg.cl.a10.data2.impl;

import de.uniheidelberg.cl.a10.data2.HasTokensAndHead;
import de.uniheidelberg.cl.a10.data2.Token;

public abstract class HasTokensAndHead_impl extends HasTokens_impl implements
		HasTokensAndHead {

	Token head = null;

	public HasTokensAndHead_impl(final String id) {
		super(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.HasTokensAndHead#getHead()
	 */
	@Override
	public Token getHead() {
		return head;
	}

	/**
	 * @param head
	 *            the head to set
	 */
	public void setHead(final Token head) {
		this.head = head;
	}

}
