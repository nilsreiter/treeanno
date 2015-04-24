package de.ustu.creta.segmentation.evaluation;

import java.util.Map;

import org.apache.uima.jcas.JCas;

public interface Metric {

	boolean init(JCas gold);

	Map<String, Double> score(JCas gold, JCas silver);
}
