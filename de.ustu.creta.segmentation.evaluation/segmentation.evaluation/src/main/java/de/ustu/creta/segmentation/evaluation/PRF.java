package de.ustu.creta.segmentation.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

public interface PRF extends Metric {
	enum Average {
		Micro, Macro
	}

	void setAnnotationClass(Class<? extends Annotation> annoClass);

	void setFeatureName(String featureName);

	void setAverage(Average avg);

	Average getAverage();

	boolean isClassWise();

	void setClassWise(boolean classWise);
}
