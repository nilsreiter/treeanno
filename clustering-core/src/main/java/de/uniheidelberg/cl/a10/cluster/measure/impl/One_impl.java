package de.uniheidelberg.cl.a10.cluster.measure.impl;

import de.uniheidelberg.cl.a10.cluster.measure.One;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;

public class One_impl implements One {

	@Override
	public double getDocumentSimilarity(final Alignment<?> alignment) {
		int a = 0;
		for (Link<?> link : alignment.getAlignments()) {
			if (link.getElements().size() > 1)
				a++;
		}
		return a / (double) alignment.getObjects().size();
	}
}
