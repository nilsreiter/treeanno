package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.uniheidelberg.cl.reiter.util.Counter;
import de.ustu.creta.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl extends AbstractFournierMetric
		implements SegmentationSimilarity {
	Class<? extends Annotation> boundaryType;

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {
		boundaryType = annotationType;
	}

	@Override
	public boolean init(JCas gold) {
		return true;
	}

	@Override
	public Map<String, Double> scores(JCas jcas1, JCas jcas2) {
		double mass = JCasUtil.select(jcas1, SegmentationUnit.class).size();
		double editDistance = getEditDistance(jcas1, jcas2);

		double d = (mass - 1.0 - editDistance) / (mass - 1.0);

		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put(getClass().getSimpleName(), d);
		return map;

	}

	public double getEditDistance(JCas jcas1, JCas jcas2) {

		int length1 = JCasUtil.select(jcas1, SegmentationUnit.class).size();
		int length2 = JCasUtil.select(jcas2, SegmentationUnit.class).size();
		if (length1 != length2) {
			throw new RuntimeException("Numbers of segmentation units differ.");
		}

		int[] massString1 = SegmentationUtil.getMassTuple(jcas1, boundaryType);
		int[] massString2 = SegmentationUtil.getMassTuple(jcas2, boundaryType);

		boolean[][] boundaries = getBoundaries(massString1, massString2);

		// finding possible substitution operations
		List<Integer> substOperations =
				this.getPotentialSubstitions(boundaries);

		// finding possible transposition operations
		Counter<Transposition> potTranspositions =
				this.getTranspositions(substOperations);

		for (Transposition tp : potTranspositions.keySet()) {
			substOperations.remove(new Integer(tp.getSource()));
			substOperations.remove(new Integer(tp.getTarget()));
			substOperations.remove(new Integer(-1 * tp.getSource()));
			substOperations.remove(new Integer(-1 * tp.getTarget()));
		}
		double editDistance =
				getSubstOperationsWeight(substOperations)
						+ getTranspositionsWeight(potTranspositions.keySet());

		return editDistance;
	}

	protected double getTranspositionsWeight(Collection<Transposition> trans) {
		double d = 0.0;
		for (Transposition tp : trans) {
			d += tpFunction.getWeight(tp);
		}
		return d / getWindowSize();
	}

	protected double getSubstOperationsWeight(Collection<Integer> substOp) {
		return substOp.size();
	}

	@Override
	public double score(JCas jcas1, JCas jcas2) {
		double mass = JCasUtil.select(jcas1, SegmentationUnit.class).size();
		double editDistance = getEditDistance(jcas1, jcas2);

		double d = (mass - 1.0 - editDistance) / (mass - 1.0);
		return d;
	}

}
