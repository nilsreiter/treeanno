package de.uniheidelberg.cl.a10.data2.alignment;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Status;

public interface Link<T> extends Set<T>, HasId {

	Collection<T> getElements();

	Collection<T> getElements(Document doc);

	@Override
	Iterator<T> iterator();

	Status getStatus();

	String getDescription();

	void setDescription(String d);

	double getScore();

	void setScore(double s);

	int overlap(final Link<T> aa);

}
