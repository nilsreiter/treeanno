package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;

public class DocumentSet_impl implements DocumentSet {

	List<Document> set;
	String id;
	String title;

	public DocumentSet_impl(String id, Document... documents) {
		this.id = id;
		set = new LinkedList<Document>();
		for (Document doc : documents) {
			set.add(doc);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public List<Document> getSet() {
		return set;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	@Override
	public boolean add(Document e) {
		boolean b = set.add(e);
		Collections.sort(set, new Comparator<Document>() {

			@Override
			public int compare(Document o1, Document o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		return b;
	}

	@Override
	public boolean addAll(Collection<? extends Document> c) {
		boolean b = set.addAll(c);
		Collections.sort(set, new Comparator<Document>() {

			@Override
			public int compare(Document o1, Document o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		return b;
	}

}
