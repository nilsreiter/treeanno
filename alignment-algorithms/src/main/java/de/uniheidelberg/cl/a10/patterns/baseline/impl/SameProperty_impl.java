package de.uniheidelberg.cl.a10.patterns.baseline.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniheidelberg.cl.a10.HasTarget;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.patterns.baseline.SameLemma;

public abstract class SameProperty_impl<T extends HasDocument & HasTarget>
		implements SameLemma<T> {

	public abstract String getProperty(T t);

	@Override
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

}
