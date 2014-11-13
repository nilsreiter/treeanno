package de.uniheidelberg.cl.a10.eval.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.eval.Blanc;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.reiter.statistics.Util;

public class Blanc_impl<T extends HasDocument> extends
		AlignmentEvaluation_impl<T> implements Blanc<T> {

	@Override
	public SingleResult evaluateFiltered(final Alignment<T> gold,
			final Alignment<T> silver, final Object name) {
		Collection<T> objects = new HashSet<T>();
		objects.addAll(silver.getObjects());
		objects.addAll(gold.getObjects());
		/*
		 * for (T obj : gold.getObjects()) { if
		 * (silver.getObjects().contains(obj)) { System.err.print("."); } }
		 */

		Matrix<T, T, Boolean> visited = new MapMatrix<T, T, Boolean>(false);

		/*
		 * right coref link
		 */
		int rc = 0;

		/*
		 * wrong coref link
		 */
		int wc = 0;

		/*
		 * right non-coref link
		 */
		int rn = 0;

		/*
		 * wrong non-coref link
		 */
		int wn = 0;

		for (T element1 : objects) {
			for (T element2 : objects) {
				if (element1 != element2 && !visited.get(element1, element2)) {
					Set<T> els = new HashSet<T>();
					els.add(element1);
					els.add(element2);
					if (silver.together(element1, element2)
							&& gold.together(element1, element2)) {

						rc++;
					} else if (silver.together(element1, element2)
							&& !gold.together(element1, element2)) {

						wc++;
					} else if (!silver.together(element1, element2)
							&& gold.together(element1, element2)) {

						wn++;
					} else if (!silver.together(element1, element2)
							&& !gold.together(element1, element2)) {

						rn++;
					}
					visited.put(element1, element2, true);
					visited.put(element2, element1, true);
				}
			}
		}

		// TODO: Boundary cases need to be implemented
		// System.err.println("rc = " + rc);
		// System.err.println("wc = " + wc);
		// System.err.println("rn = " + rn);
		// System.err.println("wn = " + wn);
		SingleResult res = new SingleResult_impl(name);

		double p_n = ensureNumber((double) rn / (double) (rn + wn));
		double r_n = ensureNumber((double) rn / (double) (rn + wc));
		double f_n = Util.fscore(1.0, p_n, r_n);

		double p_c = ensureNumber((double) rc / (double) (rc + wc));
		double r_c = ensureNumber((double) rc / (double) (rc + wn));
		double f_c = Util.fscore(1.0, p_c, r_c);

		double p = ensureNumber((p_n + p_c) / 2);
		double r = ensureNumber((r_n + r_c) / 2);
		double f = (f_n + f_c) / 2;

		res.setScore(Evaluation.PRECISION, (Double.isNaN(p) ? 0.0 : p));
		res.setScore(Evaluation.RECALL, (Double.isNaN(r) ? 0.0 : r));
		res.setScore(Evaluation.FSCORE, (Double.isNaN(f) ? 0.0 : f));

		res.setScore(NC_PRECISION, p_n);
		res.setScore(NC_RECALL, r_n);
		res.setScore(NC_FSCORE, f_n);
		res.setScore(C_PRECISION, p_c);
		res.setScore(C_RECALL, r_c);
		res.setScore(C_FSCORE, f_c);
		return res;
	}

	protected double ensureNumber(final double d) {
		if (Double.isNaN(d))
			return 0.0;
		return d;
	}

}