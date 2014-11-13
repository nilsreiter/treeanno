package de.uniheidelberg.cl.a10.data2.alignment.impl;

import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;

public class AlignmentIdProvider_impl implements AlignmentIdProvider {
	int id = 0;

	@Override
	public String getNextAlignmentId() {
		return "al" + id++;
	}

}