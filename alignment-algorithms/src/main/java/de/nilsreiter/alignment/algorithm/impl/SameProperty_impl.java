package de.nilsreiter.alignment.algorithm.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.nilsreiter.alignment.algorithm.Baseline;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;

public abstract class SameProperty_impl<T extends HasDocument & HasTokens>
implements Baseline<T> {

	public abstract String getProperty(T t);

	@Override
	@Deprecated
	public Alignment<T> getAlignment(final List<List<T>> sequences) {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		Alignment<T> alignment = new Alignment_impl<T>("SameLemma");
		Map<String, Set<T>> alignments = new HashMap<String, Set<T>>();
		for (int s = 0; s < sequences.size(); s++) {
			for (int i = 0; i < sequences.get(s).size(); i++) {
				String lemma = this.getProperty(sequences.get(s).get(i));
				// String pos = sequences.get(s).get(i).getTarget()
				// .getPartOfSpeech();
				String key = lemma;// + pos;
				if (!alignments.containsKey(key)) {
					alignments.put(key, new HashSet<T>());
				}
				alignments.get(key).add(sequences.get(s).get(i));
			}
		}
		// System.err.println(alignments);
		for (Set<T> aligned : alignments.values()) {
			alignment.addAlignment(idp.getNextAlignmentId(), aligned);
		}
		return alignment;
	}

	@Override
	public Alignment<T> align(final List<T> seq1, List<T> seq2) {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		Alignment<T> alignment = new Alignment_impl<T>("SameLemma");
		Map<String, Set<T>> alignments = new HashMap<String, Set<T>>();

		for (int i = 0; i < seq1.size(); i++) {
			String lemma = this.getProperty(seq1.get(i));
			// String pos = sequences.get(s).get(i).getTarget()
			// .getPartOfSpeech();
			String key = lemma;// + pos;
			if (!alignments.containsKey(key)) {
				alignments.put(key, new HashSet<T>());
			}
			alignments.get(key).add(seq1.get(i));
		}
		for (int i = 0; i < seq2.size(); i++) {
			String lemma = this.getProperty(seq2.get(i));
			// String pos = sequences.get(s).get(i).getTarget()
			// .getPartOfSpeech();
			String key = lemma;// + pos;
			if (!alignments.containsKey(key)) {
				alignments.put(key, new HashSet<T>());
			}
			alignments.get(key).add(seq2.get(i));
		}
		// System.err.println(alignments);
		for (Set<T> aligned : alignments.values()) {
			alignment.addAlignment(idp.getNextAlignmentId(), aligned);
		}
		return alignment;
	}

	@Override
	public Class<?> getConfigurationBean() {
		return Object.class;
	}
}
