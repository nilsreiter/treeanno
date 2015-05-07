package de.ustu.creta.segmentation.agreement;

import org.apache.uima.jcas.JCas;

public interface CohensKappa extends Agreement {

	double getObservedAgreement(JCas... jcas);

	double getChanceAgreement(JCas... jcas);

}
