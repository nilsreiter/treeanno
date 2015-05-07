package de.ustu.creta.segmentation.agreement;

import org.apache.uima.jcas.JCas;

import de.ustu.creta.segmentation.evaluation.Metric;

public interface Agreement {
	public double agr(JCas... jcas);

	void setObservedAgreementMetric(Metric metric);

	Metric getObservedAgreementMetric();

}
