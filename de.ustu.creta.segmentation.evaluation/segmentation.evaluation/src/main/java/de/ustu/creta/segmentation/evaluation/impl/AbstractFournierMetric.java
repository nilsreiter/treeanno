package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.reiter.util.Counter;
import de.ustu.creta.segmentation.evaluation.FournierMetric;

public abstract class AbstractFournierMetric implements FournierMetric {

	int windowSize = 2;
	protected TranspositionWeightingFunction tpFunction =
			new TranspositionWeightingFunction() {
				@Override
				public double getWeight(Transposition tp) {
					return tp.getMass();
				}
			};

	@Override
	public int getWindowSize() {
		return windowSize;
	}

	@Override
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	@Override
	public void setTranspositionPenaltyFunction(TranspositionWeightingFunction tpf) {
		tpFunction = tpf;
	}

	@Override
	public TranspositionWeightingFunction getTranspositionPenaltyFunction() {
		return tpFunction;
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

	public Counter<Transposition> getTranspositions(List<Integer> substOperations) {
		Counter<Transposition> potTranspositions = new Counter<Transposition>();
		// finding possible transpositions
	
		Iterator<Integer> iterator = substOperations.iterator();
		while (iterator.hasNext()) {
			int j = iterator.next();
			if (iterator.hasNext()) {
				int i = iterator.next();
				if (Math.abs(i) - Math.abs(j) < getWindowSize() && i * j <= 0) {
					potTranspositions.add(new Transposition_impl(Math.abs(j),
							Math.abs(i)), i - j);
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
		boundaries[0] = SegmentationUtil.getBoundaryString(ms1);
		boundaries[1] = SegmentationUtil.getBoundaryString(ms2);
		return boundaries;
	}

	public class Transposition_impl implements Transposition {
		int source, target;

		public Transposition_impl(int s1, int s2) {
			this.source = s1;
			this.target = s2;
		}

		@Override
		public int getMass() {
			return Math.max(source, target) - Math.min(target, source);
		}

		@Override
		public String toString() {
			return "(" + source + "," + target + ")";
		}

		@Override
		public int getSource() {
			return source;
		}

		@Override
		public int getTarget() {
			return target;
		}
	}
}
