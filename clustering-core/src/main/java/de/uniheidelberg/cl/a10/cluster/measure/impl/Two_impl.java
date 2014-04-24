package de.uniheidelberg.cl.a10.cluster.measure.impl;

import de.uniheidelberg.cl.a10.cluster.measure.Two;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.reiter.util.Counter;

public class Two_impl implements Two {

	@Override
	public double getDocumentSimilarity(final Alignment<?> alignment) {
		int a = 0;
		Counter<Document> counter = new Counter<Document>();
		for (Link<?> link : alignment.getAlignments()) {
			if (link.getElements().size() > 1)
				a++;
			for (Document rd : alignment.getDocuments()) {
				counter.add(rd, link.getElements(rd).size());
			}
		}
		System.err.println(a + "/" + counter.getMin().getFirst());
		return a / (double) counter.getMin().getFirst();
	}

}
