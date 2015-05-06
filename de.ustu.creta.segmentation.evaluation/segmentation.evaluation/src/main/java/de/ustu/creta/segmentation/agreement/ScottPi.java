package de.ustu.creta.segmentation.agreement;

import org.apache.uima.jcas.JCas;

import de.ustu.creta.segmentation.evaluation.Metric;

public interface ScottPi extends Agreement {
	void setObservedAgreementMetric(Metric metric);

	double getObservedAgreement(JCas... jcas);

	double getChanceAgreement(JCas... jcas);

	Metric getObservedAgreementMetric();
}
