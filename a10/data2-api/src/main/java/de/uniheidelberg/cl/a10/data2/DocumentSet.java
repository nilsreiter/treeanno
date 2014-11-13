package de.uniheidelberg.cl.a10.data2;

import java.util.Collection;
import java.util.List;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.HasTitle;

public interface DocumentSet extends HasId, HasTitle {

	public List<Document> getSet();

	int size();

	boolean contains(Object o);

	boolean add(Document e);

	boolean addAll(Collection<? extends Document> c);
}
