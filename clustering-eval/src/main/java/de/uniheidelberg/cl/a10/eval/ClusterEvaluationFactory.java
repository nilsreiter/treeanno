package de.uniheidelberg.cl.a10.eval;

import de.uniheidelberg.cl.a10.eval.impl.AdjustedRand_impl;
import de.uniheidelberg.cl.a10.eval.impl.ClusterBlanc_impl;
import de.uniheidelberg.cl.a10.eval.impl.ClusterEvaluation_impl;
import de.uniheidelberg.cl.a10.eval.impl.Rand_impl;

public class ClusterEvaluationFactory {
	@SuppressWarnings("deprecation")
	public static <T> ClusterEvaluation<T> getClusterEvaluation(
			final ClusterEvaluationStyle style) {
		switch (style) {
		case ClusterBlanc:
			return new ClusterBlanc_impl<T>();
		case Rand:
			return new Rand_impl<T>();
		case AdjustedRand:
			return new AdjustedRand_impl<T>();
		case AdjustedRand2:
		case Rand2:
		case VI:
		case NVI:
			return new ClusterEvaluation_impl<T>(style);
		default:
			throw new UnsupportedOperationException();
		}
	}

}