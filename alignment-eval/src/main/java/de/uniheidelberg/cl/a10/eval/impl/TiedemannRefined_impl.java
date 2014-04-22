package de.uniheidelberg.cl.a10.eval.impl;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.reiter.statistics.Util;

public class TiedemannRefined_impl<T> extends AlignmentEvaluation_impl<T> {

	@Override
	public SingleResult evaluateFiltered(final Alignment<T> gold,
			final Alignment<T> silver, final Object name) {
		double p = 0.0, r = 0.0;

		for (Link<T> fa : gold.getAlignments()) {
			double pi = 0.0;
			double ri = 0.0;
			for (Link<T> fa_silver : silver.getAlignments()) {
				int overlap = fa.overlap(fa_silver);
				if (overlap > 1) {
					pi += (double) overlap
							/ (double) fa_silver.getElements().size();
					ri += (double) overlap / (double) fa.getElements().size();
				}
			}
			r += ri;
			p += pi;
		}

		SingleResult_impl res = new SingleResult_impl(name);
		res.setPrecision(p / gold.getAlignments().size());
		res.setRecall(r / gold.getAlignments().size());
		double fscore = Util.fscore(1.0, res.p(), res.r());
		res.setScore(Evaluation.FSCORE, fscore);

		return res;
	}

}