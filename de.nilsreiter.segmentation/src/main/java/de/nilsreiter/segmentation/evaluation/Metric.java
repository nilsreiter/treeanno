package de.nilsreiter.segmentation.evaluation;

import org.apache.uima.jcas.JCas;

public interface Metric {
	double score(JCas gold, JCas silver);
}
