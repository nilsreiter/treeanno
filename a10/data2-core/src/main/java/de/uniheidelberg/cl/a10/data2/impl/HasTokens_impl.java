package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Token;

public abstract class HasTokens_impl extends AnnotationObjectInDocument_impl
		implements Iterable<Token>, Comparable<HasTokens>, HasTokens {

	List<Token> tokens;

	public HasTokens_impl(final String id) {
		super(id);
		this.init();
	}

	private void init() {
		tokens = new LinkedList<Token>();
	}

	/**
	 * @return the targetTokens
	 */
	public List<Token> getTokens() {
		return tokens;
	}

	/**
	 * @param targetTokens
	 *            the targetTokens to set
	 */
	public void setTokens(final List<Token> targetTokens) {
		this.tokens = targetTokens;
	}

	public void setTokens(final TreeSet<Token> targetTokens) {
		for (Token tok : targetTokens) {
			this.tokens.add(tok);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.IHasTokens#iterator()
	 */
	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(final int index, final Token element) {
		if (!tokens.contains(element))
			tokens.add(index, element);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(final Token e) {
		if (!tokens.contains(e))
			return tokens.add(e);
		return false;
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(final Collection<? extends Token> c) {
		return tokens.addAll(c);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	@Override
	public int numberOfTokens() {
		return tokens.size();
	}

	/**
	 * This method replaces the tokens <code>oldTokens</code> in the token list
	 * of this object by the token <code>newToken</code>. It will be inserted at
	 * the position of the first old token.
	 * 
	 * @param newToken
	 *            The token to be inserted.
	 * @param oldTokens
	 *            A collection of old tokens, to be removed from the token list.
	 */
	public void replace(final Token newToken, final Collection<Token> oldTokens) {
		int position = Integer.MAX_VALUE;
		for (Token token : oldTokens) {
			position = Math.min(position, tokens.indexOf(token));
		}
		for (Token token : oldTokens) {
			tokens.remove(token);
		}
		tokens.add(position, newToken);
	}

	@Override
	public List<Token> getTokensBetween(final int end, final int begin) {
		List<Token> r = new LinkedList<Token>();
		for (Token token : tokens) {
			if (token.getBegin() > end && token.getEnd() < begin) {
				r.add(token);
			}
		}
		return r;
	}

	public void sortTokens() {
		Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
		Arrays.sort(tokenArray, new Comparator<Token>() {

			@Override
			public int compare(final Token arg0, final Token arg1) {
				return Integer.valueOf(arg0.indexOf())
						.compareTo(arg1.indexOf());
			}
		});
		tokens.clear();
		for (Token token : tokenArray) {
			this.tokens.add(token);
		}
	}

	@Override
	public Token firstToken() {
		return this.tokens.get(0);
	}

	@Override
	public Token lastToken() {
		return this.tokens.get(this.tokens.size() - 1);
	}

	@Override
	public int indexOf() {
		return firstToken().indexOf();
	}

	@Override
	public int compareTo(final HasTokens ht) {
		return this.getTokens().get(0).compareTo(ht.getTokens().get(0));
	}

}
