package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.uniheidelberg.cl.reiter.util.Counter;
import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;

public class BoundarySimilarity_impl extends AbstractFournierMetric implements
		BoundarySimilarity {
	Class<? extends Annotation> boundaryType;

	public BoundarySimilarity_impl(Class<? extends Annotation> annotationType) {
		boundaryType = annotationType;
	}

	@Override
	public boolean init(JCas gold) {
		return true;
	}

	@Override
	public double score(JCas gold, JCas silver) {
		int[] ms1 = SegmentationUtil.getMassTuple(gold, boundaryType);
		int[] ms2 = SegmentationUtil.getMassTuple(silver, boundaryType);
		boolean[][] b = getBoundaries(ms1, ms2);

		// finding (number of) matches
		int m = 0;
		for (int i = 0; i < b[0].length; i++) {
			m += (b[0][i] == b[1][i] && b[0][i] ? 1 : 0);
		}

		// finding possible substitution operations
		List<Substitution> substOperations = this.getPotentialSubstitions2(b);

		// finding possible transposition operations
		Counter<Transposition> potTranspositions =
				this.getTranspositions2(substOperations);

		for (Transposition tp : potTranspositions.keySet()) {
			substOperations.removeIf(new Predicate<Substitution>() {
				@Override
				public boolean test(Substitution t) {
					return (tp.getTarget() == t.getPosition() || tp.getSource() == t
							.getPosition());
				}
			});
		}

		double num =
				substOperations.size()
						+ getTranspositionsWeight(potTranspositions.keySet());
		double denom = substOperations.size() + potTranspositions.size() + m;

		return 1 - (num / denom);
	}

	protected double getTranspositionsWeight(Collection<Transposition> trans) {
		double d = 0.0;
		for (Transposition tp : trans) {
			d += tpFunction.getWeight(tp);
		}
		return d / getWindowSize();
	}

	@Override
	public Map<String, Double> scores(JCas gold, JCas silver) {

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(), score(gold, silver));
		return r;
	}

}
