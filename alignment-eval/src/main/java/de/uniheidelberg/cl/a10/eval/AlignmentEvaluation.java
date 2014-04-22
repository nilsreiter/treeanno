package de.uniheidelberg.cl.a10.eval;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public interface AlignmentEvaluation<T> {

	SingleResult evaluate(final Alignment<T> gold2, final Alignment<T> silver,
			final Object name);

	SingleResult evaluate(final Alignment<T> gold2, final Alignment<T> silver);

	Alignment<T> getAlignmentWithMarking();

}
