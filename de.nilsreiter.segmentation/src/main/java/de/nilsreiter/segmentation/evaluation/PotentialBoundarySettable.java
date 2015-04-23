package de.nilsreiter.segmentation.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

@Deprecated
public interface PotentialBoundarySettable {

	@Deprecated
	void setPotentialBoundaryType(
			Class<? extends Annotation> potentialBoundaryType);

	@Deprecated
	Class<? extends Annotation> getPotentialBoundaryType();

}
