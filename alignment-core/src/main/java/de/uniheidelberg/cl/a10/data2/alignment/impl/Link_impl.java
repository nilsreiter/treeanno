package de.uniheidelberg.cl.a10.data2.alignment.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.Status;
import de.uniheidelberg.cl.a10.data2.alignment.Link;

public class Link_impl<T extends HasDocument> implements Link<T> {
	double score = Double.NaN;

	Status status = null;

	String description = null;

	List<T> elements = new LinkedList<T>();

	String id;

	public Link_impl(final String id) {
		this.id = id;
	}

	public Link_impl(final String id, final Collection<T> t) {
		this.id = id;
		elements.addAll(t);
	}

	/**
	 * @return the score
	 */
	@Override
	public double getScore() {
		return score;
	}

	/**
	 * @return
	 * @see java.util.Set#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}

	@Override
	public Collection<T> getElements() {
		return elements;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	@Override
	public void setScore(final double score) {
		this.score = score;
	}

	/**
	 * @return the status
	 */
	@Override
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final Status status) {
		this.status = status;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isPairwise() {
		return false;
	}

	@Override
	public int hashCode() {
		int result = 23;
		for (T e : this.getElements()) {
			result = result * 37 + e.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!Link_impl.class.isAssignableFrom(obj.getClass()))
			return false;
		@SuppressWarnings("unchecked")
		Link<T> other = (Link<T>) obj;
		return other.getElements().containsAll(this.getElements())
				&& this.getElements().containsAll(other.getElements());
	}

	/**
	 * Returns true if this object contains the given alignment, i.e., if every
	 * element of the given alignment is also contained in this alignment.
	 * 
	 * @param aa
	 * @return
	 */
	public boolean contains(final Link<T> aa) {
		if (this.hashCode() == aa.hashCode())
			return true;
		if (this.equals(aa))
			return true;
		return this.getElements().containsAll(aa.getElements());
	}

	public boolean contains(final T t) {
		return this.getElements().contains(t);
	}

	/**
	 * Calculates and returns the number of elements of the given alignment
	 * contained in this alignment.
	 * 
	 * @param aa
	 * @return
	 */
	@Override
	public int overlap(final Link<T> aa) {
		int r = 0;
		for (T elem : aa.getElements()) {
			if (this.getElements().contains(elem))
				r++;
		}
		return r;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.getId()).append(": ");
		b.append(this.getElements().toString());
		return b.toString();
	}

	/**
	 * @return
	 * @see java.util.Set#isEmpty()
	 */

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#remove(java.lang.Object)
	 */

	@Override
	public boolean remove(final Object o) {
		return elements.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */

	@Override
	public boolean removeAll(final Collection<?> c) {
		return elements.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */

	@Override
	public boolean retainAll(final Collection<?> c) {
		return elements.retainAll(c);
	}

	/**
	 * @return
	 * @see java.util.Set#size()
	 */

	@Override
	public int size() {
		return elements.size();
	}

	/**
	 * @return
	 * @see java.util.Set#toArray()
	 */

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	/**
	 * @param <D>
	 * @param a
	 * @return
	 * @see java.util.Set#toArray(D[])
	 */

	@Override
	public <D> D[] toArray(final D[] a) {
		return elements.toArray(a);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#get(int)
	 */

	/**
	 * @param e
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	@Override
	public boolean add(final T e) {
		return elements.add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends T> c) {
		return elements.addAll(c);
	}

	/**
	 * 
	 * @see java.util.Set#clear()
	 */
	@Override
	public void clear() {
		elements.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object o) {
		return elements.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(final Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public Collection<T> getElements(final Document doc) {
		Collection<T> coll = new HashSet<T>();
		for (T t : this.getElements()) {
			if (t.getRitualDocument().equals(doc))
				coll.add(t);
		}
		return coll;
	}

	public String getLabel() {
		return description;
	}

	public void setLabel(final String s) {
		description = s;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
