package de.uniheidelberg.cl.a10.eval.impl;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.Reiter;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.reiter.statistics.SimpleStatistics;
import de.uniheidelberg.cl.reiter.statistics.Util;

public class Reiter_impl<T> extends AlignmentEvaluation_impl<T> implements
		Reiter<T> {

	@Override
	public SingleResult evaluateFiltered(final Alignment<T> gold,
			final Alignment<T> silver, final Object name) {
		SingleResult_impl res = new SingleResult_impl(name);

		SimpleStatistics ss = new SimpleStatistics();

		for (Link<T> fa : gold.getAlignments()) {
			if (silver.contains(fa)) {
				ss.tp();
				truePositiveList.add(fa);
			} else {
				ss.fn();
				falseNegativeList.add(fa);
			}
		}
		for (Link<T> fa : silver.getAlignments()) {
			if (!gold.contains(fa)) {
				ss.fp();
				fa.setDescription("fp");
				falsePositiveList.add(fa);
			} else {
				fa.setDescription("tp");
			}
		}
		res.setPrecision(ss.p());
		res.setRecall(ss.r());
		double fscore = Util.fscore(1.0, res.p(), res.r());
		res.setScore(Evaluation.FSCORE, fscore);

		return res;
	}
}