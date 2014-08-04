package de.nilsreiter.alignment.algorithm.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.nilsreiter.alignment.algorithm.Baseline;
import de.nilsreiter.alignment.algorithm.Harmonic;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;

/**
 * General assumption: Only two sequences
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class Harmonic_impl<T extends HasDocument> implements Harmonic<T> {

	@Override
	public Alignment<T> getAlignment(final List<List<T>> sequences) {

		// 0. Setup
		Alignment<T> alignment =
				new Alignment_impl<T>(Baseline.class.getCanonicalName());
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();

		// 1. Find the shorter list
		int longerList = 1, shorterList = 0;
		if (sequences.get(1).size() < sequences.get(0).size()) {
			shorterList = 1;
			longerList = 0;
		}
		int n = sequences.get(shorterList).size(), m =
				sequences.get(longerList).size();

		double f = m / (double) n;
		Set<T> aligned = new HashSet<T>();

		// 2. Iterate over the events in the longer list
		int j = 0;
		for (int i = 0; i < m; i++) {
			double marker = (i + 1) % (f);
			// System.err.println("m = " + marker);
			// if the marker is < 1, we create a new alignment set
			if (marker < 1.0) {
				// System.err.println(aligned);
				if (j < n) aligned.add(sequences.get(shorterList).get(j));
				aligned.add(sequences.get(longerList).get(i));
				if (!aligned.isEmpty())
					alignment.addAlignment(idp.getNextAlignmentId(), aligned);
				aligned = new HashSet<T>();
				j++;

			} else {
				// else, we just continue with the old
				aligned.add(sequences.get(longerList).get(i));
			}
		}
		if (j < n) aligned.add(sequences.get(shorterList).get(j));
		// System.err.println(aligned);
		if (!aligned.isEmpty())
			alignment.addAlignment(idp.getNextAlignmentId(), aligned);
		// System.err.println(alignment.getAlignments());
		return alignment;
	}

	@Override
	public Alignment<T> align(final List<T> seq1, List<T> seq2) {

		// 0. Setup
		Alignment<T> alignment =
				new Alignment_impl<T>(Baseline.class.getCanonicalName());
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();

		// 1. Find the shorter list
		int longerList = 2, shorterList = 1;
		if (seq2.size() < seq1.size()) {
			shorterList = 2;
			longerList = 1;
		}
		List<T> shortList = (shorterList == 1 ? seq1 : seq2), longList =
				(longerList == 1 ? seq1 : seq2);
		int n = shortList.size(), m = longList.size();

		double f = m / (double) n;
		Set<T> aligned = new HashSet<T>();

		// 2. Iterate over the events in the longer list
		int j = 0;
		for (int i = 0; i < m; i++) {
			double marker = (i + 1) % (f);
			// System.err.println("m = " + marker);
			// if the marker is < 1, we create a new alignment set
			if (marker < 1.0) {
				// System.err.println(aligned);
				if (j < n) aligned.add(shortList.get(j));
				aligned.add(longList.get(i));
				if (!aligned.isEmpty())
					alignment.addAlignment(idp.getNextAlignmentId(), aligned);
				aligned = new HashSet<T>();
				j++;

			} else {
				// else, we just continue with the old
				aligned.add(longList.get(i));
			}
		}
		if (j < n) aligned.add(shortList.get(j));
		// System.err.println(aligned);
		if (!aligned.isEmpty())
			alignment.addAlignment(idp.getNextAlignmentId(), aligned);
		// System.err.println(alignment.getAlignments());
		return alignment;
	}
}
