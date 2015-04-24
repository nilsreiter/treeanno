package de.ustu.creta.segmentation.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

import de.ustu.creta.segmentation.evaluation.impl.BoundarySimilarity_impl;
import de.ustu.creta.segmentation.evaluation.impl.BreakDifference_impl;
import de.ustu.creta.segmentation.evaluation.impl.FleissKappaBoundarySimilarity_impl;
import de.ustu.creta.segmentation.evaluation.impl.PRF_impl;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationSimilarity_impl;
import de.ustu.creta.segmentation.evaluation.impl.WindowDifference_impl;

public class MetricFactory {

	@SuppressWarnings("unchecked")
	public static <T extends Metric> T getMetric(Class<T> mClass,
			Class<? extends Annotation> annoClass) {
		if (mClass.equals(BreakDifference.class))
			return (T) new BreakDifference_impl(annoClass);
		if (mClass.equals(WindowDifference.class))
			return (T) new WindowDifference_impl(annoClass);
		if (mClass.equals(PRF.class)) return (T) new PRF_impl(annoClass);
		if (mClass.equals(SegmentationSimilarity.class))
			return (T) new SegmentationSimilarity_impl(annoClass);
		if (mClass.equals(BoundarySimilarity.class))
			return (T) new BoundarySimilarity_impl(annoClass);
		if (mClass.equals(FleissKappaBoundarySimilarity.class))
			return (T) new FleissKappaBoundarySimilarity_impl(annoClass);
		return null;

	}
}
