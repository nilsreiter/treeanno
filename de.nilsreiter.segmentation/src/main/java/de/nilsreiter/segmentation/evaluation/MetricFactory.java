package de.nilsreiter.segmentation.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.segmentation.evaluation.impl.BreakDifference_impl;
import de.nilsreiter.segmentation.evaluation.impl.WindowDifference_impl;

public class MetricFactory {

	public static Metric getMetric(Class<? extends Metric> mClass,
			Class<? extends Annotation> annoClass) {
		if (mClass.equals(BreakDifference.class))
			return new BreakDifference_impl(annoClass);
		if (mClass.equals(WindowDifference.class))
			return new WindowDifference_impl(annoClass);
		return null;

	}
}
