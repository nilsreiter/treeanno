package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.uniheidelberg.cl.a10.data2.Corpus;
import de.uniheidelberg.cl.a10.data2.Document;

public class Corpus_impl implements Corpus {
	Map<String, Document> documentMap;
	String id;

	public Corpus_impl(String cid) {
		id = cid;
		documentMap = new HashMap<String, Document>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Document getDocument(String documentId) {
		return documentMap.get(documentId);
	}

	@Override
	public Iterator<Document> iterator() {
		return this.documentMap.values().iterator();
	}
}
