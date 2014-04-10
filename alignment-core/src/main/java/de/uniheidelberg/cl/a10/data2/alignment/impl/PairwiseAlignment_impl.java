package de.uniheidelberg.cl.a10.data2.alignment.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Link;

public class PairwiseAlignment_impl<T extends HasDocument> extends
		Alignment_impl<T> {

	List<PairwiseLink_impl<T>> alignments = null;

	public PairwiseAlignment_impl(final String id) {
		super(id);
		this.alignments = new LinkedList<PairwiseLink_impl<T>>();
	}

	@Override
	public List<PairwiseLink_impl<T>> getAlignments() {
		return this.alignments;
	}

	@Override
	public PairwiseLink_impl<T> addAlignment(final String id,
			final Collection<T> aligned) {
		Iterator<T> iter = aligned.iterator();
		T e1 = iter.next();
		T e2 = iter.next();
		return this.addAlignment(id, e1, e2);
	}

	public PairwiseLink_impl<T> addAlignment(final String id, final T e1,
			final T e2) {
		PairwiseLink_impl<T> r = new PairwiseLink_impl<T>(id);
		r.setElement1(e1);
		r.setElement2(e2);
		this.alignments.add(r);
		return r;
	}

	public PairwiseLink_impl<T> addAlignment(final String id,
			final AlignmentType type, final T e1, final T e2, final double score) {
		PairwiseLink_impl<T> r = new PairwiseLink_impl<T>(id);
		r.setElement1(e1);
		r.setElement2(e2);
		r.setAlignmentType(type);
		r.setScore(score);
		this.alignments.add(r);
		return r;
	}

	public PairwiseLink_impl<T> addAlignment(final String id,
			final AlignmentType type, final T e1, final T e2) {
		PairwiseLink_impl<T> r = new PairwiseLink_impl<T>(id);
		r.setElement1(e1);
		r.setElement2(e2);
		r.setAlignmentType(type);
		this.alignments.add(r);
		return r;
	}

	public PairwiseLink_impl<T> addAlignment(final PairwiseLink_impl<T> pa) {
		this.alignments.add(pa);
		return pa;
	}

	@Override
	public Collection<Link<T>> getAlignmentForObject(final T obj) {
		Collection<Link<T>> fas = new HashSet<Link<T>>();
		for (de.uniheidelberg.cl.a10.data2.alignment.PairwiseLink<T> fa : this
				.getAlignments()) {
			if (fa.getElement(0) == obj || fa.getElement(1) == obj)
				fas.add(fa);
		}
		return fas;
	}

	@Override
	public boolean isPairwise() {
		return true;
	}

	public Document getDoc1() {
		return this.getDocument(0);
	}

	public Document getDoc2() {
		return this.getDocument(1);
	}

}
