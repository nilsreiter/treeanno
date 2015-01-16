package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Iterator;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.segmentation.evaluation.WindowDifference;

public class WindowDifference_impl implements WindowDifference {
	Class<? extends Annotation> annoType;

	int windowSize = 10;

	public WindowDifference_impl(Class<? extends Annotation> annotationType) {
		annoType = annotationType;
	}

	public double score(JCas gold, JCas silver) {
		int wBegin = 0;
		int wEnd = wBegin + windowSize;
		int length = gold.getDocumentText().length();
		int sum = 0;
		do {
			int num_gold =
					JCasUtil.selectCovered(gold, annoType, wBegin, wEnd).size();
			int num_silver =
					JCasUtil.selectCovered(silver, annoType, wBegin, wEnd)
							.size();
			if (num_gold != num_silver) sum++;
			// sum += Math.abs(num_gold - num_silver);
			wBegin = wEnd + 1;
			wEnd = wBegin + windowSize;
		} while (wEnd <= length);

		return (double) sum / (double) (length - windowSize);
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public boolean init(JCas gold) {
		int pos = 0;
		Iterator<? extends Annotation> iter = JCasUtil.iterator(gold, annoType);
		int n = 0;
		int length = 0;
		while (iter.hasNext()) {
			Annotation anno = iter.next();
			length += anno.getBegin() - pos;
			n++;
			pos = anno.getBegin();
		}
		this.setWindowSize((int) Math.floor((double) length / (double) (n + 1)));
		return true;
	}
}