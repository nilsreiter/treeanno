package de.uniheidelberg.cl.a10.data;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class BaseText implements List<BaseSentence> {
	List<BaseSentence> sentences = new LinkedList<BaseSentence>();

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public boolean add(final BaseSentence e) {
		return sentences.add(e);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@Override
	public void add(final int index, final BaseSentence element) {
		sentences.add(index, element);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends BaseSentence> c) {
		return sentences.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(final int index,
			final Collection<? extends BaseSentence> c) {
		return sentences.addAll(index, c);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear() {
		sentences.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object o) {
		return sentences.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(final Collection<?> c) {
		return sentences.containsAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		return sentences.equals(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	@Override
	public BaseSentence get(final int index) {
		return sentences.get(index);
	}

	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	@Override
	public int hashCode() {
		return sentences.hashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(final Object o) {
		return sentences.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return sentences.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<BaseSentence> iterator() {
		return sentences.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(final Object o) {
		return sentences.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<BaseSentence> listIterator() {
		return sentences.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<BaseSentence> listIterator(final int index) {
		return sentences.listIterator(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	@Override
	public BaseSentence remove(final int index) {
		return sentences.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o) {
		return sentences.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(final Collection<?> c) {
		return sentences.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(final Collection<?> c) {
		return sentences.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public BaseSentence set(final int index, final BaseSentence element) {
		return sentences.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	@Override
	public int size() {
		return sentences.size();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<BaseSentence> subList(final int fromIndex, final int toIndex) {
		return sentences.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray() {
		return sentences.toArray();
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(final T[] a) {
		return sentences.toArray(a);
	}

	public void writeCoNLL09(final OutputStreamWriter sw,
			final boolean separateSentencesByEmptyLine) throws IOException {
		for (BaseSentence bs : this) {
			sw.write(bs.getSentenceData09().toString());
			if (separateSentencesByEmptyLine) {
				sw.write("\n");
			}
		}
	}
}
