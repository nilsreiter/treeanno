package de.ustu.creta.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.ustu.creta.segmentation.evaluation.BreakDifference;
import de.ustu.ims.segmentation.type.SegmentBoundary;

public class BreakDifference_impl implements BreakDifference {

	Class<? extends Annotation> annoType;

	public BreakDifference_impl(Class<? extends Annotation> annotationType) {
		annoType = annotationType;
	}

	@Override
	public double score(JCas gold, JCas silver) {
		int length = gold.getDocumentText().length();

		int sum = 0;
		int n = 0;
		for (Annotation sb : JCasUtil.select(gold, annoType)) {
			int pos = sb.getBegin();

			int window = 5;
			int distance = length;
			while (distance >= length
					&& (pos - window > 0 || pos + window < length)) {
				List<SegmentBoundary> cClosest =
						JCasUtil.selectCovered(silver, SegmentBoundary.class,
								pos - window, pos + window);
				for (SegmentBoundary cc : cClosest) {
					if (Math.abs(pos - cc.getBegin()) < distance) {
						distance = Math.abs(pos - cc.getBegin());
					}
				}

				window = window * 2;
			}
			sum += distance;
			n++;
		}

		return ((double) sum / (double) n);
	}

	@Override
	public Map<String, Double> scores(JCas gold, JCas silver) {

		HashMap<String, Double> res = new HashMap<String, Double>();
		res.put(this.getClass().getSimpleName(), score(gold, silver));
		return res;
	}

	@Override
	public boolean init(JCas gold) {
		// nothing happens here
		return true;
	}
}
