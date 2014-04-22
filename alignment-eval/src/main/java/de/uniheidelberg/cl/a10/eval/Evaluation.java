package de.uniheidelberg.cl.a10.eval;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.eval.impl.Blanc_impl;
import de.uniheidelberg.cl.a10.eval.impl.Fraser_impl;
import de.uniheidelberg.cl.a10.eval.impl.Reiter_impl;
import de.uniheidelberg.cl.a10.eval.impl.TiedemannRefined_impl;
import de.uniheidelberg.cl.a10.eval.impl.Tiedemann_impl;

public class Evaluation {

	public static <T extends HasDocument> AlignmentEvaluation<T> getAlignmentEvaluation(
			final Style style) {
		switch (style) {
		case FRASER:
			return new Fraser_impl<T>();
		case TIEDEMANN:
			return new Tiedemann_impl<T>();
		case BLANC:
			return new Blanc_impl<T>();
		case TIEDEMANN_REFINED:
			return new TiedemannRefined_impl<T>();
		case REITER:
			return new Reiter_impl<T>();
		default:
			return null;
		}
	}

	public static String PRECISION = "0 precision";
	public static String RECALL = "1 recall";
	public static String FSCORE = "2 f-score";
}
