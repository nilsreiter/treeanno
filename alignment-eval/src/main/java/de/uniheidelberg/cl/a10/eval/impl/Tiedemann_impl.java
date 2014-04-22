package de.uniheidelberg.cl.a10.eval.impl;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.eval.Tiedemann;
import de.uniheidelberg.cl.reiter.statistics.Util;

public class Tiedemann_impl<T> extends AlignmentEvaluation_impl<T> implements
		Tiedemann<T> {
	@Override
	public SingleResult evaluateFiltered(final Alignment<T> gold,
			final Alignment<T> silver, final Object name) {
		double p = 0.0, r = 0.0;
		for (Link<T> fa : gold.getAlignments()) {
			double q = 0.0;
			int aligned = 0;
			for (Link<T> fa_silver : silver.getAlignments()) {
				int overlap = fa.overlap(fa_silver); // number of overlapping
														// tokens in
														// (partially) correct
														// link proposals
				if (overlap > 1) {
					q += overlap;
					aligned += fa_silver.getElements().size();
				}
			}
			double q_r = q / fa.getElements().size();
			double q_p = 0.0;
			if (aligned > 0)
				q_p = q / aligned;
			r += q_r;
			p += q_p;
		}

		SingleResult_impl res = new SingleResult_impl(name);
		res.setPrecision(p / silver.getAlignments().size());
		res.setRecall(r / gold.getAlignments().size());
		double fscore = Util.fscore(1.0, res.p(), res.r());
		res.setScore(Evaluation.FSCORE, fscore);

		return res;
	}
}