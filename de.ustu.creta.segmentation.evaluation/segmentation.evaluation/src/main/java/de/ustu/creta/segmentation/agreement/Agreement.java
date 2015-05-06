package de.ustu.creta.segmentation.agreement;

import org.apache.uima.jcas.JCas;

public interface Agreement {
	public double agr(JCas... jcas);
}
