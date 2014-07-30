package de.uniheidelberg.cl.a10.data2.impl;

@Deprecated
public class DependencyFrame_impl extends Frame_impl {

	public DependencyFrame_impl(final Token_impl token) {
		super("df" + token.getId());
		this.add(token);
	}

}
