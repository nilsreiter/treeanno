package de.uniheidelberg.cl.a10.data2.alignment.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SetHypergraph;

public class Alignment_impl<T extends HasDocument> implements Alignment<T> {
	List<Document> documents;
	Hypergraph<T, Link<T>> graph;

	String id;

	String title;

	public Alignment_impl(final String id) {
		this.id = id;
		documents = new ArrayList<Document>();
		graph = new SetHypergraph<T, Link<T>>();
	}

	@Override
	public Link<T> addAlignment(final String id, final Collection<T> aligned) {
		Link<T> al = new Link_impl<T>(id, aligned);

		// add vertices, if not existent
		for (T vertex : aligned) {
			if (!graph.containsVertex(vertex)) {
				graph.addVertex(vertex);
				if (!documents.contains(vertex.getRitualDocument()))
					documents.add(vertex.getRitualDocument());
			}
		}

		graph.addEdge(al, aligned);
		return al;
	}

	@Override
	public Document getDocument(final int i) {
		return documents.get(i);
	}

	@Override
	public List<Document> getDocuments() {
		return documents;
	}

	@Override
	public boolean together(final T obj1, final T obj2) {
		for (Link<T> aa : this.getAlignmentForObject(obj1)) {
			if (aa.getElements().contains(obj2)) return true;
		}
		return false;
	}

	@Override
	public Link<T> getLink(final T obj1, final T obj2) {
		for (Link<T> aa : this.getAlignmentForObject(obj1)) {
			if (aa.getElements().contains(obj2)) return aa;
		}
		return null;
	}

	public boolean isPairwise() {
		return false;
	}

	@Override
	public boolean contains(final Link<T> aa) {
		for (Link<T> als : getAlignments()) {
			if (als.containsAll(aa)) return true;
		}
		return false;

	}

	@Override
	public boolean contains(final T aa) {
		return graph.containsVertex(aa);

	}

	public Collection<? extends Link<T>> getCollection() {
		return this.getAlignments();
	}

	public SortedSet<? extends Link<T>> getSortedAlignments(
			final Comparator<Link<T>> comp) {
		SortedSet<Link<T>> ret = new TreeSet<Link<T>>(comp);
		ret.addAll(this.getAlignments());
		return ret;
	}

	/**
	 * This method filters this document by removing all alignables that are not
	 * contained in one of the other's documents.
	 * 
	 * @param other
	 */
	@Override
	public Alignment<T> filter(final Alignment<T> other) {
		Alignment<T> ret = new Alignment_impl<T>(this.getId());
		for (Link<T> aa : this.getAlignments()) {
			Collection<T> elements = new HashSet<T>();
			if (!aa.getElements().contains(null))
				for (T elem : aa.getElements()) {
					if (elem.getRitualDocument() == null
							|| other.getDocuments().contains(
									elem.getRitualDocument())) {
						elements.add(elem);
					}
				}
			ret.addAlignment(aa.getId(), elements);
		}
		return ret;
	}

	@Override
	public Collection<? extends Link<T>> getAlignments() {
		return graph.getEdges();
	}

	@Override
	public Collection<? extends Link<T>> getNonSingletonAlignments() {
		Set<Link<T>> ret = new HashSet<Link<T>>();
		for (Link<T> link : this.getAlignments()) {
			if (link.getElements().size() > 1) {
				ret.add(link);
			}
		}
		return ret;
	}

	@Override
	public Alignment<T> getFilteredAlignment(final Document seq1,
			final Document seq2) {
		Alignment_impl<T> ret = new Alignment_impl<T>(this.getId());
		ret.documents.add(seq1);
		ret.documents.add(seq2);
		for (Link<T> naa : this.getAlignments()) {
			Set<T> elements = new HashSet<T>();
			for (T e1 : naa.getElements()) {
				if (e1.getRitualDocument() == seq1
						|| e1.getRitualDocument() == seq2) {
					elements.add(e1);
				}
			}
			Set<Document> docs = new HashSet<Document>();
			for (T elem : elements) {
				docs.add(elem.getRitualDocument());
			}
			if (docs.size() > 1) ret.addAlignment(naa.getId(), elements);
		}
		return ret;
	}

	@Override
	public PairwiseAlignment_impl<T> getPairwiseAlignmentDocument(
			final Document seq1, final Document seq2) {
		PairwiseAlignment_impl<T> ret =
				new PairwiseAlignment_impl<T>(this.getId());
		ret.documents.add(seq1);
		ret.documents.add(seq2);
		for (Link<T> naa : this.getAlignments()) {
			for (T e1 : naa.getElements()) {
				if (e1.getRitualDocument() == seq1)
					for (T e2 : naa.getElements()) {
						if (e2.getRitualDocument() == seq2
								&& !ret.together(e1, e2))
							ret.addAlignment(naa.getId(), e1, e2);
					}
			}
		}
		return ret;
	}

	@Override
	public Collection<Link<T>> getAlignmentForObject(final T obj) {
		if (this.graph.containsVertex(obj))
			return this.graph.getIncidentEdges(obj);
		return new HashSet<Link<T>>();
	}

	public Link<T> addAlignment(final String id, final Collection<T> aligned,
			final String description, final double score) {
		Link<T> al = this.addAlignment(id, aligned);
		al.setDescription(description);
		al.setScore(score);

		return al;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Collection<? extends T> getObjects() {
		return this.graph.getVertices();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}
}
