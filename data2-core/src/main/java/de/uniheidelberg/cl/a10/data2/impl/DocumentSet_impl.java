package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;

public class DocumentSet_impl implements DocumentSet {

	Set<Document> set;
	String id;

	public DocumentSet_impl(String id, Document... documents) {
		this.id = id;
		set = new HashSet<Document>();
		for (Document doc : documents) {
			set.add(doc);
		}
	}

	@Override
	public String getId() {
		return id;
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
	public Object[] toArray() {
		return set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return set.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	@Override
	public void clear() {
		set.clear();
	}

	@Override
	public boolean equals(Object o) {
		return set.equals(o);
	}

	@Override
	public int hashCode() {
		return set.hashCode();
	}

	@Override
	public Spliterator<Document> spliterator() {
		return set.spliterator();
	}

	@Override
	public boolean removeIf(Predicate<? super Document> filter) {
		return set.removeIf(filter);
	}

	@Override
	public Stream<Document> stream() {
		return set.stream();
	}

	@Override
	public Stream<Document> parallelStream() {
		return set.parallelStream();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return set.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return set.removeAll(c);
	}

	@Override
	public Iterator<Document> iterator() {
		return set.iterator();
	}

	@Override
	public boolean add(Document e) {
		return set.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Document> c) {
		return set.addAll(c);
	}

}
