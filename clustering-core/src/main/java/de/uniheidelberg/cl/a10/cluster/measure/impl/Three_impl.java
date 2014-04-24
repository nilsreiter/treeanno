package de.uniheidelberg.cl.a10.cluster.measure.impl;

import de.uniheidelberg.cl.a10.cluster.measure.Three;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.reiter.util.Counter;

public class Three_impl implements Three {
	@Override
	public double getDocumentSimilarity(final Alignment<?> alignment) {
		double a = 0;
		Counter<Document> counter = new Counter<Document>();
		for (Link<?> link : alignment.getAlignments()) {
			if (link.getElements().size() > 1)
				a += (Double.isNaN(link.getScore()) ? 1.0 : link.getScore());
			for (Document rd : alignment.getDocuments()) {
				counter.add(rd, link.getElements(rd).size());
			}
		}
		// System.err.println(a);
		double b = counter.getMin().getFirst();
		if (a > b)
			return 1.0;
		return a / b;
	}
}
