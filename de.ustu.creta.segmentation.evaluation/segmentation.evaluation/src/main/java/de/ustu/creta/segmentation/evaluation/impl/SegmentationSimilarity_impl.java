package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.uniheidelberg.cl.reiter.util.Counter;
import de.ustu.creta.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl implements SegmentationSimilarity {
	Class<? extends Annotation> boundaryType;

	int windowSize = 2;

	TranspositionWeightingFunction tpFunction =
			new TranspositionWeightingFunction() {
		@Override
		public double getWeight(Transposition tp) {
			return tp.getMass();
		}
	};

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

	protected boolean[] getBoundaryString(int[] massString) {
		boolean[] boundaries = new boolean[sum(massString)];
		Arrays.fill(boundaries, false);
		int index = 0;
		for (int i = 0; i < massString.length - 1; i++) {
			index += massString[i];
			boundaries[index] = true;
		}
		return boundaries;
	}

	public int sum(int[] array) {
		int s = 0;
		for (int i = 0; i < array.length; i++) {
			s += array[i];
		}
		return s;
	}

	public List<Integer> getPotentialSubstitions(boolean[][] boundaries) {
		List<Integer> substOperations = new LinkedList<Integer>();
		for (int i = 0; i < boundaries[0].length; i++) {

			if (boundaries[0][i] ^ boundaries[1][i]) {
				substOperations.add((boundaries[0][i] ? i : -i));
			}
		}
		return substOperations;
	}

	public Counter<Transposition> getTranspositions(
			List<Integer> substOperations) {
		Counter<Transposition> potTranspositions = new Counter<Transposition>();
		// finding possible transpositions

		Iterator<Integer> iterator = substOperations.iterator();
		while (iterator.hasNext()) {
			int j = iterator.next();
			if (iterator.hasNext()) {
				int i = iterator.next();
				if (Math.abs(i) - Math.abs(j) < getWindowSize() && i * j <= 0) {
					potTranspositions.add(
							new Transposition(Math.abs(j), Math.abs(i)), i - j);
				}
			}

		}
		/*
		 * Integer j = null;
		 * 
		 * for (Integer i : substOperations) {
		 * if (j != null && Math.abs(i) - Math.abs(j) < getWindowSize()
		 * && i * j < 0) {
		 * potTranspositions.add(
		 * new Transposition(Math.abs(j), Math.abs(i)), i - j);
		 * }
		 * 
		 * j = i;
		 * }
		 */
		return potTranspositions;
	}

	public boolean[][] getBoundaries(int[] ms1, int[] ms2) {
		boolean[][] boundaries = new boolean[2][];
		boundaries[0] = getBoundaryString(ms1);
		boundaries[1] = getBoundaryString(ms2);
		return boundaries;
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
			substOperations.remove(new Integer(tp.source));
			substOperations.remove(new Integer(tp.target));
			substOperations.remove(new Integer(-1 * tp.source));
			substOperations.remove(new Integer(-1 * tp.target));
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

	public class Transposition {
		int source, target;

		public Transposition(int s1, int s2) {
			this.source = s1;
			this.target = s2;
		}

		public int getMass() {
			return Math.max(source, target) - Math.min(target, source);
		}

		@Override
		public String toString() {
			return "(" + source + "," + target + ")";
		}
	}

	@Override
	public double score(JCas jcas1, JCas jcas2) {
		double mass = JCasUtil.select(jcas1, SegmentationUnit.class).size();
		double editDistance = getEditDistance(jcas1, jcas2);

		double d = (mass - 1.0 - editDistance) / (mass - 1.0);
		return d;
	}

	@Override
	public void setTranspositionPenaltyFunction(
			TranspositionWeightingFunction tpf) {
		tpFunction = tpf;
	}

	@Override
	public TranspositionWeightingFunction getTranspositionPenaltyFunction() {
		return tpFunction;
	}

	@Override
	public int getWindowSize() {
		return windowSize;
	}

	@Override
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

}
