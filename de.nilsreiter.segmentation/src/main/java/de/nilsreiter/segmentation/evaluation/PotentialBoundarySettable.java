package de.nilsreiter.segmentation.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

public interface PotentialBoundarySettable {

	void setPotentialBoundaryType(
			Class<? extends Annotation> potentialBoundaryType);

	Class<? extends Annotation> getPotentialBoundaryType();

}
