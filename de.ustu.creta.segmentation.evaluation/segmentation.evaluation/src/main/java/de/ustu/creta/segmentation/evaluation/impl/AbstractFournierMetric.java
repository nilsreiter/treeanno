package de.ustu.creta.segmentation.evaluation.impl;

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
