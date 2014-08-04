package de.nilsreiter.alignment.algorithm.impl;

import java.util.List;

import de.nilsreiter.alignment.algorithm.NoAlignment;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.FullAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.FullAlignment_impl;

public class NoAlignment_impl<T extends HasDocument> implements NoAlignment<T> {

	@Override
	@Deprecated
	public Alignment<T> getAlignment(final List<List<T>> sequences) {
		FullAlignment<T> alignment =
				new FullAlignment_impl<T>("NoAlignmentBaseline");
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		for (List<T> seq : sequences) {
			alignment.getDocuments().add(seq.get(0).getRitualDocument());
			alignment.addSingletons(idp, seq);
		}
		return alignment;
	}

	@Override
	public Alignment<T> align(List<T> seq1, List<T> seq2) {
		FullAlignment<T> alignment =
				new FullAlignment_impl<T>("NoAlignmentBaseline");
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		alignment.getDocuments().add(seq1.get(0).getRitualDocument());
		alignment.addSingletons(idp, seq1);
		alignment.getDocuments().add(seq2.get(0).getRitualDocument());
		alignment.addSingletons(idp, seq2);
		return alignment;
	}

}
