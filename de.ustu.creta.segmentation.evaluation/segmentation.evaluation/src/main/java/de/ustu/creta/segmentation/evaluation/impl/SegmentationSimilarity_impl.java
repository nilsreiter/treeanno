package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Arrays;
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

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {

		boundaryType = annotationType;
	}

	@Override
	public boolean init(JCas gold) {
		return true;
	}

	@Override
	public Map<String, Double> score(JCas jcas1, JCas jcas2) {
		int n = 2;
		int length1 = JCasUtil.select(jcas1, SegmentationUnit.class).size();
		int length2 = JCasUtil.select(jcas2, SegmentationUnit.class).size();
		if (length1 != length2) {
			throw new RuntimeException("Numbers of segmentation units differ.");
		}

		int[] massString1 = SegmentationUtil.getMassTuple(jcas1, boundaryType);
		int[] massString2 = SegmentationUtil.getMassTuple(jcas2, boundaryType);

		int[][] boundaries = new int[2][];
		boundaries[0] = new int[length1];
		Arrays.fill(boundaries[0], 0);
		int index = 0;
		for (int i = 0; i < massString1.length - 1; i++) {
			index += massString1[i];
			boundaries[0][index] = 1;
		}
		boundaries[1] = new int[length2];
		Arrays.fill(boundaries[1], 0);
		index = 0;
		for (int i = 0; i < massString2.length - 1; i++) {
			index += massString2[i];
			boundaries[1][index] = 1;
		}

		// finding possible substituion operations
		List<Integer> substOperations = new LinkedList<Integer>();
		Counter<Transposition> potTranspositions = new Counter<Transposition>();
		for (int i = 0; i < boundaries[0].length; i++) {
			if (boundaries[0][i] != boundaries[1][i]) {
				substOperations.add(i);
			}
		}
		Integer j = null;
		for (Integer i : substOperations) {

			if (j != null && i - j < n) {
				potTranspositions.add(new Transposition(j, i), i - j);
			}

			j = i;
		}
		int editDistance = substOperations.size();
		int lEnd = 0;
		for (Transposition tp : potTranspositions.keySet()) {
			// substOperations.remove(new Integer(tp.source));
			// substOperations.remove(new Integer(tp.target));
			if (Math.min(tp.source, tp.target) > lEnd)
				editDistance -= tp.getMass();
			lEnd = Math.max(tp.target, tp.source);
		}

		return null;

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

	protected int sum(int[] array) {
		int s = 0;
		for (int i = 0; i < array.length; i++) {
			s += array[i];
		}
		return s;
	}

	public int getEditDistance(JCas jcas1, JCas jcas2, int window) {
		int n = window;
		int length1 = JCasUtil.select(jcas1, SegmentationUnit.class).size();
		int length2 = JCasUtil.select(jcas2, SegmentationUnit.class).size();
		if (length1 != length2) {
			throw new RuntimeException("Numbers of segmentation units differ.");
		}

		int[] massString1 = SegmentationUtil.getMassTuple(jcas1, boundaryType);
		int[] massString2 = SegmentationUtil.getMassTuple(jcas2, boundaryType);

		boolean[][] boundaries = new boolean[2][];
		boundaries[0] = getBoundaryString(massString1);
		boundaries[1] = getBoundaryString(massString2);

		// finding possible substitution operations
		List<Integer> substOperations = new LinkedList<Integer>();
		Counter<Transposition> potTranspositions = new Counter<Transposition>();
		for (int i = 0; i < boundaries[0].length; i++) {

			if (boundaries[0][i] ^ boundaries[1][i]) {
				substOperations.add((boundaries[0][i] ? i : -i));
			}
		}

		// finding possible transpositions

		Integer j = null;
		for (Integer i : substOperations) {

			if (j != null && Math.abs(i) - Math.abs(j) < n && i * j < 0) {
				potTranspositions.add(
						new Transposition(Math.abs(j), Math.abs(i)), i - j);
			}

			j = i;
		}
		int editDistance = substOperations.size();
		int lEnd = 0;
		for (Transposition tp : potTranspositions.keySet()) {
			// substOperations.remove(new Integer(tp.source));
			// substOperations.remove(new Integer(tp.target));
			if (Math.min(tp.source, tp.target) > lEnd)
				editDistance -= tp.getMass();
			lEnd = Math.max(tp.target, tp.source);
		}
		return editDistance;
	}

	public class Transposition {
		int source, target;

		public Transposition(int s1, int s2) {
			this.source = s1;
			this.target = s2;
		}

		public int getMass() {
			return Math.max(source, target) - Math.min(target, source) + 1;
		}

		@Override
		public String toString() {
			return "(" + source + "," + target + ")";
		}
	}
}
