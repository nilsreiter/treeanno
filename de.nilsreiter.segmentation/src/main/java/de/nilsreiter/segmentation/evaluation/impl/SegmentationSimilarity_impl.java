package de.nilsreiter.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

import de.nilsreiter.segmentation.evaluation.SegmentationSimilarity;

public class SegmentationSimilarity_impl implements SegmentationSimilarity {
	Class<? extends Annotation> annoType;
	PythonInterpreter interpreter = null;

	public SegmentationSimilarity_impl(
			Class<? extends Annotation> annotationType) {

		annoType = annotationType;
	}

	public boolean init(JCas gold) {
		return ensureInterpreter();
	}

	protected boolean ensureInterpreter() {
		try {
			if (interpreter == null) {
				Properties props = new Properties();
				props.setProperty(
						"python.path",
						"/Users/reiterns/Documents/Java/de.nilsreiter.segmentation/src/main/resources/python/segeval-2.0.11");
				PythonInterpreter.initialize(props, props, new String[0]);
				interpreter = new PythonInterpreter();
				// interpreter.exec("import sys");
				// interpreter.exec("print sys.path");
				interpreter.exec("import segeval");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeQuietly(interpreter);
		}
	}

	public Map<String, Double> score(JCas gold, JCas silver) {
		ensureInterpreter();

		int[] goldMasses = new int[JCasUtil.select(gold, annoType).size() + 1];
		PyInteger[] silverMasses =
				new PyInteger[JCasUtil.select(silver, annoType).size() + 1];

		int index = 0;
		int i = 0;
		for (Annotation anno : JCasUtil.select(gold, annoType)) {
			goldMasses[i++] = anno.getBegin() - index;
			index = anno.getBegin();
		}
		goldMasses[i++] = gold.getDocumentText().length() - index;

		index = 0;
		i = 0;
		for (Annotation anno : JCasUtil.select(silver, annoType)) {
			silverMasses[i++] = new PyInteger(anno.getBegin() - index);
			index = anno.getBegin();
		}
		silverMasses[i++] =
				new PyInteger(silver.getDocumentText().length() - index);

		PyInteger[] goldMassesPy = new PyInteger[goldMasses.length];
		for (i = 0; i < goldMassesPy.length; i++) {
			goldMassesPy[i] = new PyInteger(goldMasses[i]);
		}

		PyTuple goldTuple = new PyTuple(goldMassesPy);
		PyTuple silverTuple = new PyTuple(silverMasses);
		interpreter.set("seg1", goldTuple);
		interpreter.set("seg2", silverTuple);
		interpreter.exec("print seg1");
		PyFloat obj =
				interpreter.eval("segeval.segmentation_similarity(seg1, seg2)")
				.__float__();

		Map<String, Double> r = new HashMap<String, Double>();
		r.put(getClass().getSimpleName(), obj.asDouble());
		return r;
	}

}
