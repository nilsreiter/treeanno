package de.uniheidelberg.cl.a10.data2.alignment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.HasTarget;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.FullAlignment_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class AlignmentFactory<T extends HasTarget & HasDocument> {

	SimilarityFunction<T> similarity = null;

	public Alignment<Token> getTokenAlignmentFromHMM(
			final HiddenMarkovModel_impl<T> hmm) {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		FullAlignment<Token> doc = new FullAlignment_impl<Token>(null);

		for (Integer state : hmm.getStates()) {
			if (hmm.getEventsForState(state).size() > 1) {
				Set<Token> targets = new HashSet<Token>();
				Set<T> events = new HashSet<T>();
				for (T f : hmm.getEventsForState(state)) {
					targets.add(f.getTarget());
					events.add(f);
				}
				Link<Token> link = doc.addAlignment(idp.getNextAlignmentId(),
						targets);
				if (similarity != null)
					link.setScore(getSimilarity(events));
			}
		}

		for (T evt : hmm.getEvents()) {
			doc.addSingleton(idp.getNextAlignmentId(), evt.getTarget());
		}

		if (doc.getAlignments().size() == 0) {
			Set<Document> docs = new HashSet<Document>();
			for (T event : hmm.getEvents()) {
				docs.add(event.getRitualDocument());
			}
			doc.getDocuments().addAll(docs);
		}

		return doc;

	}

	public Alignment<T> getAlignmentFromHMM(final HiddenMarkovModel_impl<T> hmm) {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		FullAlignment<T> doc = new FullAlignment_impl<T>(null);

		for (Integer state : hmm.getStates()) {
			if (hmm.getEventsForState(state).size() > 1)
				doc.addAlignment(idp.getNextAlignmentId(),
						hmm.getEventsForState(state));
		}

		for (T evt : hmm.getEvents()) {
			doc.addSingleton(idp.getNextAlignmentId(), evt);
		}

		return doc;
	}

	public double getSimilarity(final Collection<T> aligned) {
		double d = 0.0;
		int n = 0;
		for (T elem1 : aligned) {
			for (T elem2 : aligned) {
				if (!elem1.getRitualDocument()
						.equals(elem2.getRitualDocument())) {
					try {
						d += similarity.sim(elem1, elem2).getProbability();
						n++;
					} catch (IncompatibleException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return d / n;

	}

	public SimilarityFunction<T> getSimilarity() {
		return similarity;
	}

	public void setSimilarity(final SimilarityFunction<T> similarity) {
		this.similarity = similarity;
	}

	public static <T extends HasDocument> Alignment<T> fromPairwiseAlignment(
			final de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignment<T> pa,
			final Document text1, final Document text2) {
		Alignment<T> document = new Alignment_impl<T>("");
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		document.getDocuments().add(text1);
		document.getDocuments().add(text2);
		int length = pa.getScoreTagLine().size();
		for (int i = 0; i < length; i++) {
			T f1 = pa.getGappedSequence1().get(i);
			T f2 = pa.getGappedSequence2().get(i);

			Set<T> aligned = new HashSet<T>();
			if (f1 != null)
				aligned.add(f1);
			if (f2 != null)
				aligned.add(f2);
			document.addAlignment(idp.getNextAlignmentId(), aligned).setScore(
					pa.getScoreTagLine().get(i).getScore());
		}
		return document;
	}
}
