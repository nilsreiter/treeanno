package de.uniheidelberg.cl.a10.data2;

import java.util.Iterator;

import de.uniheidelberg.cl.a10.HasId;

public interface Corpus extends HasId, Iterable<Document> {
	public Document getDocument(String documentId);

	@Override
	public Iterator<Document> iterator();
}