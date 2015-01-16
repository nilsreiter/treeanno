package de.nilsreiter.segmentation.evaluation;

import org.apache.uima.jcas.JCas;

public interface Metric {

	boolean init(JCas gold);

	double score(JCas gold, JCas silver);
}
