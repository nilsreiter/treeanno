package de.uniheidelberg.cl.a10.patterns.baseline.impl;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.FullAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.FullAlignment_impl;
import de.uniheidelberg.cl.a10.patterns.baseline.NoAlignment;

public class NoAlignment_impl<T extends HasDocument> implements NoAlignment<T> {

	@Override
	public Alignment<T> getAlignment(final List<List<T>> sequences) {
		FullAlignment<T> alignment = new FullAlignment_impl<T>(
				"NoAlignmentBaseline");
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		for (List<T> seq : sequences) {
			alignment.getDocuments().add(seq.get(0).getRitualDocument());
			alignment.addSingletons(idp, seq);
		}
		return alignment;
	}

}
