package de.nilsreiter.alignment.algorithm.impl;

import java.util.List;

import de.nilsreiter.alignment.algorithm.WeightedHarmonic;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class WeightedHarmonic_impl<T extends HasDocument> extends
Harmonic_impl<T> implements WeightedHarmonic<T> {

	SimilarityFunction<T> similarity = null;

	@Override
	public Alignment<T> getAlignment(final List<List<T>> sequences) {
		Alignment<T> alignment = super.getAlignment(sequences);
		for (Link<T> link : alignment.getAlignments()) {
			double s = this.getAverageSimilarity(link);
			link.setScore(s);
		}
		return alignment;
	}

	@Override
	public void setSimilarityFunction(final SimilarityFunction<T> sfun) {
		similarity = sfun;
	}

	protected double getAverageSimilarity(final Link<T> link) {
		double d = 0.0;
		int n = 0;
		for (T obj1 : link.getElements()) {
			for (T obj2 : link.getElements()) {
				if (!obj1.getRitualDocument().equals(obj2.getRitualDocument())) {
					try {
						d += similarity.sim(obj1, obj2).getProbability();
						n++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return d / n;
	}

}
