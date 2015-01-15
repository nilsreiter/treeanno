package de.nilsreiter.segmentation.evaluation.impl;

import java.util.List;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.segmentation.evaluation.BreakDifference;

public class BreakDifference_impl implements BreakDifference {

	public double score(JCas gold, JCas silver) {
		int length = gold.getDocumentText().length();

		int sum = 0;
		int n = 0;
		for (SegmentBoundary sb : JCasUtil.select(gold, SegmentBoundary.class)) {
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
}
