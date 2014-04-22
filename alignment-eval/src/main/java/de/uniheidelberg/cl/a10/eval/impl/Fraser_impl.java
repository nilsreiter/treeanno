/**
 * 
 */
/**
 * @author reiter
 *
 */
package de.uniheidelberg.cl.a10.eval.impl;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.Fraser;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.reiter.statistics.Util;

public class Fraser_impl<T> extends AlignmentEvaluation_impl<T> implements
		Fraser<T> {
	@Override
	public SingleResult evaluateFiltered(final Alignment<T> gold,
			final Alignment<T> silver, final Object name) {

		int goodLinks = 0;
		int allGoldLinks = 0;
		int allSilverLinks = 0;
		Matrix<T, T, Boolean> visited = new MapMatrix<T, T, Boolean>(false);
		for (Link<T> fa : gold.getAlignments()) {
			for (T e1 : fa.getElements()) {
				for (T e2 : fa.getElements()) {
					if (e1 != e2 && !visited.get(e1, e2)) {
						visited.put(e1, e2, true);
						visited.put(e2, e1, true);
						if (silver.together(e1, e2)) {
							goodLinks++;
							fa.setDescription("tp");
							truePositiveList.add(fa);
						} else {
							falseNegativeList.add(fa);
						}
						allGoldLinks++;
					}
				}
			}
		}

		visited = new MapMatrix<T, T, Boolean>(false);
		for (Link<T> fa : silver.getAlignments()) {
			for (T e1 : fa.getElements()) {
				for (T e2 : fa.getElements()) {
					if (e1 != e2 && !visited.get(e1, e2)) {
						visited.put(e1, e2, true);
						visited.put(e2, e1, true);
						allSilverLinks++;
						if (gold.together(e1, e2)) {
							fa.setDescription("tp");
						} else {
							falsePositiveList.add(fa);
							fa.setDescription("fp");
						}
					}
				}
			}
		}

		SingleResult_impl r = new SingleResult_impl(name);
		if (allSilverLinks == 0) {
			r.setPrecision(1.0);
		} else {
			r.setPrecision((double) goodLinks / (double) allSilverLinks);
		}

		if (allGoldLinks == 0) {
			r.setRecall(1.0);
		} else {
			r.setRecall((double) goodLinks / (double) allGoldLinks);
		}

		double fscore = Util.fscore(1.0, r.p(), r.r());
		r.setScore(Evaluation.FSCORE, fscore);
		return r;
	}
}