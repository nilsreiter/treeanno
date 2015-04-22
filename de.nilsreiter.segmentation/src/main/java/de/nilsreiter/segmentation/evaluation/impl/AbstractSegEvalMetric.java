package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyDictionary;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

public abstract class AbstractSegEvalMetric {
	PythonInterpreter interpreter = null;

	protected boolean ensureInterpreter() {
		try {
			if (interpreter == null) {
				Properties props = new Properties();
				props.setProperty(
						"python.path",
						"/Users/reiterns/Documents/Java/de.nilsreiter.segmentation/src/main/resources/python/segeval-2.0.11");
				PythonInterpreter.initialize(props, props, new String[0]);
				interpreter = new PythonInterpreter();
				interpreter.exec("import segeval");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeQuietly(interpreter);
		}
	}

	PyTuple getMassTuple(JCas jcas, Class<? extends Annotation> boundaryType,
			Class<? extends Annotation> potBoundaries) {
		Collection<? extends Annotation> boundaries =
				JCasUtil.select(jcas, boundaryType);

		PyInteger[] masses = new PyInteger[boundaries.size() + 1];
		int i = 0;
		Annotation prevAnno = null;
		for (Annotation anno : boundaries) {
			if (i == 0) {
				masses[i++] =
						new PyInteger(JCasUtil.selectPreceding(potBoundaries,
								anno, Integer.MAX_VALUE).size());
			} else {
				masses[i++] =
						new PyInteger(JCasUtil.selectBetween(potBoundaries,
								prevAnno, anno).size() + 1);
			}
			prevAnno = anno;
		}

		if (prevAnno == null) {
			masses[i] =
					new PyInteger(JCasUtil.select(jcas, potBoundaries).size());
		} else
			masses[i] =
			new PyInteger(JCasUtil.selectFollowing(potBoundaries,
					prevAnno, Integer.MAX_VALUE).size() + 1);

		return new PyTuple(masses);
	}

	PyTuple getMassTuple(Collection<? extends Annotation> annotations,
			int length) {
		PyInteger[] silverMasses = new PyInteger[annotations.size() + 1];
		int index = 0;
		int i = 0;
		for (Annotation anno : annotations) {
			silverMasses[i++] = new PyInteger(anno.getBegin() - index);
			index = anno.getBegin();
		}
		silverMasses[i++] = new PyInteger(length - index);
		return new PyTuple(silverMasses);
	}

	double getPyFunctionValue(PyTuple seg1, PyTuple seg2, String function) {
		interpreter.set("seg1", seg1);
		interpreter.set("seg2", seg2);

		interpreter.exec("result = " + function + "(seg1, seg2)");
		PyObject obj = interpreter.get("result");
		return obj.asDouble();
	}

	double getPyFunctionValueFromDictionary(PyTuple seg1, PyTuple seg2,
			String function) {
		PyDictionary dict =
				new PyDictionary(new PyObject[] {
						new PyString("data"),
						new PyDictionary(new PyObject[] { new PyInteger(1),
								seg1, new PyInteger(2), seg2 }) });
		interpreter.set("dataset", dict);
		interpreter.exec("result = " + function + "(dataset)");
		PyObject obj = interpreter.get("result");
		return obj.asDouble();
	}

}
