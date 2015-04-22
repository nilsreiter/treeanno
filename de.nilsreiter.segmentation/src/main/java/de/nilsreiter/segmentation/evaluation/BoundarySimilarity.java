package de.nilsreiter.segmentation.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

public interface BoundarySimilarity extends Metric {

	void setPotentialBoundaryType(
			Class<? extends Annotation> potentialBoundaryType);

	Class<? extends Annotation> getPotentialBoundaryType();

}
