package de.uniheidelberg.cl.a10.data2.alignment;

import java.util.Collection;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.Document;

public interface Alignment<T> extends HasId {
	Collection<? extends Link<T>> getAlignments();

	Collection<Document> getDocuments();

	boolean contains(final Link<T> aa);

	boolean contains(T element);

	Alignment<T> filter(final Alignment<T> other);

	Collection<? extends Link<T>> getAlignmentForObject(T obj);

	boolean together(T a1, T a2);

	Link<T> addAlignment(final String id, final Collection<T> aligned);

	Document getDocument(final int i);

	/**
	 * Returns an alignment in which all elements from documents other than the
	 * two given are removed.
	 * 
	 * @param seq1
	 * @param seq2
	 * @return
	 */
	Alignment<T> getFilteredAlignment(final Document seq1, final Document seq2);

	Alignment<T> getPairwiseAlignmentDocument(final Document seq1,
			final Document seq2);

	Link<T> getLink(T obj1, T obj2);

	Collection<? extends Link<T>> getNonSingletonAlignments();
}
