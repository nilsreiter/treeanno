package de.uniheidelberg.cl.a10.data2.impl;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;

public abstract class AnnotationObjectInDocument_impl extends
		AnnotationObject_impl implements HasDocument,
		AnnotationObjectInDocument {

	Document ritualDocument;

	public AnnotationObjectInDocument_impl(final String id) {
		super(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniheidelberg.cl.a10.data2.DataObjectInDocument#getRitualDocument()
	 */
	@Override
	public Document getRitualDocument() {
		return ritualDocument;
	}

	/**
	 * This method returns a position of the object in the document (e.g., token
	 * position). This is not always the exact number. If the object is a token,
	 * the character position of start or end might be returned. Within the same
	 * class, the objects are sortable according to this number.
	 * 
	 * If the objects are unsorted, 0 is returned.
	 * 
	 * @return
	 */
	@Override
	public abstract int indexOf();

	/**
	 * @param ritualDocument
	 *            the ritualDocument to set
	 */
	public void setRitualDocument(final Document ritualDocument) {
		this.ritualDocument = ritualDocument;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof AnnotationObjectInDocument_impl))
			return false;
		AnnotationObjectInDocument_impl doi = (AnnotationObjectInDocument_impl) o;
		return this.id.equals(doi.id)
				&& (this.ritualDocument == null || this.ritualDocument
						.equals(doi.ritualDocument));
		// return this.hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, id);
		if (this.getRitualDocument() != null)
			result = HashCodeUtil
					.hash(result, this.getRitualDocument().getId());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.DataObjectInDocument#getRelated()
	 */
	@Override
	public abstract Set<AnnotationObjectInDocument> getRelated();

	@Override
	public Set<AnnotationObjectInDocument> getClosure() {
		Set<AnnotationObjectInDocument> set = getRelated();
		int newsize = set.size(), oldsize = set.size();
		do {
			Set<AnnotationObjectInDocument> s2 = new HashSet<AnnotationObjectInDocument>();
			for (AnnotationObjectInDocument dao : set) {
				s2.addAll(dao.getRelated());
			}
			set.addAll(s2);
			oldsize = newsize;
			newsize = set.size();
		} while (newsize > oldsize);
		return set;
	}

	@Override
	public String getGlobalId() {
		return this.getRitualDocument().getId() + "-" + this.getId();
	}

}
