package de.uniheidelberg.cl.a10.data2.impl;

import de.uniheidelberg.cl.a10.data2.Token;

public class DependencyFrameElement_impl extends FrameElm_impl {

	public DependencyFrameElement_impl(final Frame_impl frame, final Token token) {
		super("dfe" + frame.getId() + "_" + token.getId());
		this.add(token);
		this.setName(token.getDependencyRelation());
		this.setFrame(frame);
	}

}
