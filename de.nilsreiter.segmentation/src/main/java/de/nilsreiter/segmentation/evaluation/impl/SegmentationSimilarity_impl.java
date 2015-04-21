package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Map;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.util.PythonInterpreter;

import de.nilsreiter.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl implements SegmentationSimilarity {
	Class<? extends Annotation> annoType;

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {
		System.setProperty("python.path", "");
		// System.setProperty("python.import.site", "true");

		System.setProperty(
				"python.path",
				"/Users/reiterns/Documents/Java/de.nilsreiter.segmentation/src/main/resources/python/segeval-2.0.11");
		annoType = annotationType;
	}

	public boolean init(JCas gold) {
		PythonInterpreter interpreter = null;
		try {
			PythonInterpreter.initialize(System.getProperties(),
					System.getProperties(), new String[0]);
			interpreter = new PythonInterpreter();
			interpreter.exec("import sys");
			interpreter.exec("print sys.path");
			interpreter.exec("import segeval");
			interpreter.set("seg1", "(5,3,1)");
			interpreter.set("seg2", "(6,3,2)");
			interpreter.exec("print seg1");
			interpreter.exec("segeval.segmentation_similarity(seg1, seg2)");
			// No operation necessary
		} finally {
			// IOUtils.closeQuietly(interpreter);
		}
		return true;
	}

	public Map<String, Double> score(JCas gold, JCas silver) {

		return null;
	}

}
